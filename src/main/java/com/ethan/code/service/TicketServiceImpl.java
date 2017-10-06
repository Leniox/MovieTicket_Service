package com.ethan.code.service;


import com.ethan.code.domain.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Stream;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LogManager.getLogger(TicketServiceImpl.class);

    @Autowired
    protected UtilService utilService;

    protected Venue venue;

    /**
     * used for hold the local maximum set while during the DFS search
     * It should be thread safe since that the @service in spring has a singleton scopre
     */
    protected Set<Seat> localMaxSet;

    /**
     * use a list to hold all SeatHold objects since there's no database
     */
    protected List<SeatHold> seatHolds;

    /**
     * Constructor with row number and column number
     * also initialize the venue and list of seatHolds
     * @param rowNum
     * @param columnNum
     */
    public TicketServiceImpl(int rowNum, int columnNum) {
        venue = new VenueImpl(rowNum, columnNum);
        venue.initializeSeats();
        seatHolds = new ArrayList<>();
    }

    /**
     * go through the seat matrix and count for the active the seats
     * @return
     */
    public int numSeatsAvailable() {
        return (int) Stream.of(venue.getSeats()).flatMap(Stream::of).filter(Seat::isActive).count();
    }

    /**
     * The implementation for find the best number of seats using DFS
     * Previous, I designed the score system for the seat, so each seat have a score. To find the best group of seats,
     * I use some common sense again. Because we always want to seat with your family or friends, so the best group of seats
     * should adjacent to each other.
     * With this assumption, the problem now is to find adjacent seats with highest total score with in available seats.
     * I solve the problem using DFS.
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        if (numSeats > numSeatsAvailable()) {
            throw new IllegalArgumentException("We not have enough available seats");
        }

        Set<Seat> bestSeats = findBestSeatsByDFS(numSeats);

        utilService.changeStatusToHold(bestSeats);

        logger.info(venue);

        SeatHold seatHold =  new SeatHoldImpl(bestSeats, customerEmail, utilService.getExpireSec());
        seatHolds.add(seatHold);

        return seatHold;
    }

    /**
     * A method used to hold a seat by its position, mainly for testing purpose
     * @param x
     * @param y
     * @param customerEmail
     * @return
     */
    public SeatHold holdSeatByPosition(int x, int y, String customerEmail) {
        if (numSeatsAvailable() == 0) {
            throw new IllegalArgumentException("We not have enough available seats");
        }

        Seat seat = venue.getSeats()[x][y];

        if (!seat.isActive()) {
            throw new IllegalArgumentException("The seat is not available");
        }

        Set<Seat> set = new HashSet<>();
        set.add(seat);
        utilService.changeStatusToHold(set);

        logger.info(venue);

        SeatHold seatHold =  new SeatHoldImpl(set, customerEmail, utilService.getExpireSec());
        seatHolds.add(seatHold);

        return seatHold;
    }

    /**
     * search for the best group of seats using DFS
     * it will return a set of adjacent seats with highest possible score
     * @param numSeats
     * @return
     */
    private Set<Seat> findBestSeatsByDFS(int numSeats) {

        Seat[][] seats = venue.getSeats();

        //use a matrix to record visited seat
        boolean[][] visited;

        //for every new search, reset it
        //it should be thread safe since the @service in spring is singleton
        localMaxSet = new HashSet<>();

        /**
         * start dfs from seat[0][0]
         */
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (seats[i][j].isActive()) {
                    //for every new search, reset the visited matrix
                    visited = new boolean[seats.length][seats[0].length];
                    searchByDFS(seats, visited, i, j, new HashSet<>(), numSeats);
                }
            }
        }

        logger.info(localMaxSet);

        return localMaxSet;
    }

    /**
     * the actual implementation of DFS
     * @param seats
     * @param visited
     * @param row
     * @param column
     * @param set
     * @param num
     */
    private void searchByDFS(Seat[][] seats, boolean[][] visited, int row, int column, Set<Seat> set, int num) {

        set.add(seats[row][column]);
        //mark it as visited
        visited[row][column] = true;


        if (set.size() == num) {
            //if the current set sum is the local maximum, then update the maximum
            if (localMaxSet.isEmpty() || localMaxSet.stream().mapToInt(Seat::getScore).sum() < set.stream().mapToInt(Seat::getScore).sum()) {
                localMaxSet = new HashSet<>(set);
            }
            return;
        }

        //do DFS
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

    /**
     * the method used to commit a SeatHold Object
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the
    seat hold is assigned
     * @return
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {

        Optional<SeatHold> seatHold = seatHolds.stream().filter(e -> e.getSetHoldId() == seatHoldId).findFirst();

        String confirmCode;

        // if there's no matching SeatHold or the SeatHold has already been commited
        if (seatHold.isPresent() && !seatHold.get().isCommitted()) {

            // if the email usd to commit the SeatHold is different from the email used to hold seats
            if(seatHold.get().getEmailAddress() == customerEmail) {

                //is the SeatHold has been expired
                if (!seatHold.get().isExpired()) {

                    utilService.changeStatusToReserved(seatHold.get().getHoldSeats());

                    //generate a 8-digit random confirmation code
                    confirmCode = RandomStringUtils.random(8, true, true).toUpperCase();

                    seatHold.get().setConfirmCode(confirmCode);

                } else {
                    throw new RuntimeException("Your hold for the seats have expired");
                }
            } else {
                throw new IllegalArgumentException("The email address to reserve ticket doesn't match the email address used to hold the seats");
            }
        } else {
            throw new IllegalArgumentException("There's no hold for seats match the id");
        }

        logger.info(venue);

        logger.info(confirmCode);

        return confirmCode;
    }

    public Venue getVenue() { return venue; }

    public void setVenue(Venue venue) { this.venue = venue; }

    public List<SeatHold> getSeatHolds() { return seatHolds; }

    public void setSeatHolds(List<SeatHold> seatHolds) { this.seatHolds = seatHolds; }

}
