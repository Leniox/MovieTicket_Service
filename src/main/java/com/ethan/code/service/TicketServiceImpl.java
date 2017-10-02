package com.ethan.code.service;

import com.ethan.code.App;
import com.ethan.code.domain.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LogManager.getLogger(TicketServiceImpl.class);

    protected Venue venue;

    protected Set<Seat> localMaxSet;

    public TicketServiceImpl() {
        Gson gson = new Gson();
        logger.info("in ticket service constructor");
        venue = new VenueImpl(9, 15);
        venue.initializeSeats();
        //logger.info(gson.toJson(venue));
        logger.info(venue);
    }

    public int numSeatsAvailable() {
        return (int) Stream.of(venue.getSeats()).flatMap(Stream::of).filter(Seat::isActive).count();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        if (numSeats > numSeatsAvailable()) {
            throw new IllegalArgumentException("We not have enough available seats");
        }

        findBestSeatsByDFS(numSeats);

        return null;
    }

    public Set<Seat> findBestSeatsByDFS(int numSeats) {
        Gson gson = new Gson();
        Seat[][] seats = venue.getSeats();
        boolean[][] visited;
        //logger.info(visited[0][0]);
        localMaxSet = new HashSet<>();
        //int localMax = 0;
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (seats[i][j].isActive()) {
                    visited = new boolean[seats.length][seats[0].length];
                    searchByDFS(seats, visited, i, j, new HashSet<>(), numSeats);
                }
            }
        }
        logger.info(localMaxSet + getLineNumber());
        localMaxSet.forEach(System.out::println);
        return localMaxSet;
    }

    private void searchByDFS(Seat[][] seats, boolean[][] visited, int row, int column, Set<Seat> set, int num) {

        set.add(seats[row][column]);
        visited[row][column] = true;

        if (set.size() == num) {
            if (localMaxSet.isEmpty() || localMaxSet.stream().mapToInt(Seat::getScore).sum() < set.stream().mapToInt(Seat::getScore).sum()) {
                localMaxSet = new HashSet<>(set);
                logger.info(localMaxSet + getLineNumber());
            }
            return;
        }

        if (row - 1 >= 0 && seats[row-1][column].isActive() && !visited[row-1][column]) {
            searchByDFS(seats, visited, row - 1, column, set, num);
            set.remove(seats[row-1][column]);
            visited[row - 1][column] = false;
        }
        if (row + 1 < seats.length && seats[row+1][column].isActive() && !visited[row+1][column]) {
            searchByDFS(seats, visited, row + 1, column, set, num);
            set.remove(seats[row+1][column]);
            visited[row + 1][column] = false;
        }
        if (column - 1 >= 0 && seats[row][column-1].isActive() && !visited[row][column-1]) {
            searchByDFS(seats, visited, row, column -1, set, num);
            set.remove(seats[row][column-1]);
            visited[row][column-1] = false;
        }
        if (column + 1 < seats[0].length && seats[row][column+1].isActive() && !visited[row][column+1]) {
            searchByDFS(seats, visited, row, column + 1, set, num);
            set.remove(seats[row][column+1]);
            visited[row][column+1] = false;
        }

        //visited[row][column] = false;

//        set.remove(seats[row][column]);

    }

    public SeatHold holdOneSpecificSeat(int row, int column, String customerEmail) {
        Seat seat = getSeatByPosition(row, column);
        seat.setEmailAddress(customerEmail);
        Set<Seat> holdSet = new HashSet<>();
        holdSet.add(seat);
        changeStatusToHold(holdSet);
//        SeatHold seatHold = new SeatHoldImpl(holdSet, customerEmail);
        return new SeatHoldImpl(holdSet, customerEmail);
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {
        return null;
    }

    public Seat getSeatByPosition(int row, int column) {
        return venue.getSeats()[row][column];
    }

    public void changeStatusToHold(Set<Seat> set) {
        if (set == null || set.isEmpty()) return;
        else {
            set.forEach(Seat::changeToHold);
        }
    }

    public Venue getVenue() { return venue; }

    public void setVenue(Venue venue) { this.venue = venue; }

    public static String getLineNumber() {
        return "Line Number " + String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber());
    }

}
