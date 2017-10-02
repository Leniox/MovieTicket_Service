package com.ethan.code.service;

import com.ethan.code.App;
import com.ethan.code.domain.*;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LogManager.getLogger(TicketServiceImpl.class);

    protected Venue venue;

    protected Set<Seat> localMaxSet;

    protected List<SeatHold> seatHolds;

    public TicketServiceImpl() {
        venue = new VenueImpl(9, 15);
        venue.initializeSeats();
        seatHolds = new ArrayList<>();
    }

    public int numSeatsAvailable() {
        return (int) Stream.of(venue.getSeats()).flatMap(Stream::of).filter(Seat::isActive).count();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        if (numSeats > numSeatsAvailable()) {
            throw new IllegalArgumentException("We not have enough available seats");
        }

        Set<Seat> bestSeats = findBestSeatsByDFS(numSeats);
        changeStatusToHold(bestSeats);

        logger.info(venue);

        return new SeatHoldImpl(bestSeats, customerEmail);
    }

    public Set<Seat> findBestSeatsByDFS(int numSeats) {

        Seat[][] seats = venue.getSeats();
        boolean[][] visited;

        localMaxSet = new HashSet<>();

        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (seats[i][j].isActive()) {
                    visited = new boolean[seats.length][seats[0].length];
                    searchByDFS(seats, visited, i, j, new HashSet<>(), numSeats);
                }
            }
        }
        logger.info(localMaxSet + getLineNumber());

        return localMaxSet;
    }

    private void searchByDFS(Seat[][] seats, boolean[][] visited, int row, int column, Set<Seat> set, int num) {

        set.add(seats[row][column]);
        visited[row][column] = true;

        if (set.size() == num) {
            if (localMaxSet.isEmpty() || localMaxSet.stream().mapToInt(Seat::getScore).sum() < set.stream().mapToInt(Seat::getScore).sum()) {
                localMaxSet = new HashSet<>(set);
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

    }


    public String reserveSeats(int seatHoldId, String customerEmail) {

        Optional<SeatHold> seatHold = seatHolds.stream().filter(e -> e.getSetHoldId() == seatHoldId).findFirst();

        String confirmCode;

        if (seatHold.isPresent() && !seatHold.get().isCommitted()) {
            if(seatHold.get().getEmailAddress() == customerEmail) {
                if (!seatHold.get().isExpired()) {
                    changeStatusToReserved(seatHold.get().getHoldSeats());
                    confirmCode = RandomStringUtils.random(8, true, true);
                    seatHold.get().setConfirmCode(confirmCode);
                } else {
                    throw new RuntimeException("You hold for the seats have expired");
                }
            } else {
                throw new IllegalArgumentException("The email address to reserve ticket doesn't match the email address used to hold the seats");
            }
        } else {
            throw new IllegalArgumentException("There's no hold for seats match the id");
        }

        logger.info(venue);

        return confirmCode;
    }


    public void changeStatusToHold(Set<Seat> set) {
        if (set == null || set.isEmpty()) return;
        else {
            set.forEach(Seat::changeToHold);
        }
    }

    public void changeStatusToReserved(Set<Seat> set) {
        if (set == null || set.isEmpty()) return;
        else {
            set.forEach(Seat::changeToReserved);
        }
    }

    public Venue getVenue() { return venue; }

    public void setVenue(Venue venue) { this.venue = venue; }

    public List<SeatHold> getSeatHolds() { return seatHolds; }

    public void setSeatHolds(List<SeatHold> seatHolds) { this.seatHolds = seatHolds; }

    public static String getLineNumber() {
        return "Line Number " + String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber());
    }

}
