package com.ethan.code.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.IntStream;


public class VenueImpl implements Venue {

    private static final Logger logger = LogManager.getLogger(VenueImpl.class);

    protected Seat[][] seats;

    //Constructor using row number and column number from property file
    public VenueImpl(int rowNum, int colNum) {
        System.out.println("creating a new Venue");
        seats = new Seat[rowNum][colNum];
    }

    /**
     * initialize all the seats in the venue with score
     */
    public void initializeSeats() {
        IntStream.range(0, seats.length)
                 .forEach( x -> IntStream.range(0, seats[0].length)
                    .forEach( y -> {
                        seats[x][y] = new SeatImpl(x, y, Status.UNBOOKED, getSeatScore(x, y));
                    }));
    }

    /**
     * the findAndHoldSeats() in TicketService needs to find best seats, but how do we define best?
     * Based on our common sense when watch a movie, the seats in the center of the venue are usually the best seat.
     * So I use a rate score system for all the seats. The seat in the center will be rated as 100 and the seat in four corners
     * will be rated as 0. The score changes linearly from center to edge.
     * So based on this algorithm, a 9 * 17 venue will be rated like:
     *   0  4  8  12 16 20 24 28 32 28 24 20 16 12  8  4  0
         16 20 24 28 32 36 40 44 48 44 40 36 32 28 24 20 16
         33 37 41 45 49 53 57 61 65 61 57 53 49 45 41 37 33
         50 54 58 62 66 70 74 78 82 78 74 70 66 62 58 54 50
         66 70 74 78 82 86 90 94 98 94 90 86 82 78 74 70 66
         50 54 58 62 66 70 74 78 82 78 74 70 66 62 58 54 50
         33 37 41 45 49 53 57 61 65 61 57 53 49 45 41 37 33
         16 20 24 28 32 36 40 44 48 44 40 36 32 28 24 20 16
         0  4  8  12 16 20 24 28 32 28 24 20 16 12 8  4  0
     * a 10 * 18 venue will be rated like:
     *   0  4  8  12 16 20 24 28 32 32 28 24 20 16 12  8  4  0
         16 20 24 28 32 36 40 44 48 48 44 40 36 32 28 24 20 16
         33 37 41 45 49 53 57 61 65 65 61 57 53 49 45 41 37 33
         50 54 58 62 66 70 74 78 82 82 78 74 70 66 62 58 54 50
         66 70 74 78 82 86 90 94 98 98 94 90 86 82 78 74 70 66
         66 70 74 78 82 86 90 94 98 98 94 90 86 82 78 74 70 66
         50 54 58 62 66 70 74 78 82 82 78 74 70 66 62 58 54 50
         33 37 41 45 49 53 57 61 65 65 61 57 53 49 45 41 37 33
         16 20 24 28 32 36 40 44 48 48 44 40 36 32 28 24 20 16
         0  4  8  12 16 20 24 28 32 32 28 24 20 16 12 8  4  0
     *
     * @param rowNum
     * @param colNum
     * @return
     */
    private int getSeatScore(int rowNum, int colNum) {
        int rowL = seats.length / 2;
        int colL = seats[0].length / 2;
        int rowScore = 0, colScore = 0;
        if (seats.length % 2 == 0) {
            if (rowNum < rowL) {
                rowScore = rowNum * (100 / (rowL -1));
            } else {
                rowScore = (seats.length - rowNum - 1) * (100 / (rowL -1));
            }
        } else {
            if (rowNum <= rowL) {
                rowScore = rowNum * (100 / rowL);
            } else {
                rowScore = (seats.length - rowNum - 1) * (100 / rowL);
            }
        }
        if (seats[0].length % 2 == 0) {
            if (colNum < colL) {
                colScore = colNum * (100 / (colL -1));
            } else {
                colScore = (seats[0].length - colNum - 1) * (100 / (colL -1));
            }
        } else {
            if (colNum <= colL) {
                colScore = colNum * (100 / colL);
            } else {
                colScore = (seats[0].length - colNum - 1) * (100 / colL);
            }
        }
        return (rowScore * 2 + colScore * 1) / 3;
    }

    public int getRowNum() {
        return seats.length;
    }

    public int getColNum() {
        return seats[0].length;
    }

    public Seat[][] getSeats() {
        return seats;
    }

    public void setSeats(Seat[][] seats) {
        this.seats = seats;
    }

    /**
     * function used to print the score matrix
     * @return
     */
    public String printSeatsScore() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                sb.append(seats[i][j].getScore() + " ");
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }
        return sb.toString();
    }

    /**
     * override the toString method for unit testing
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (seats[i][j].getStatus() == Status.UNBOOKED) sb.append("_ ");
                else if (seats[i][j].getStatus() == Status.HOLD) sb.append("# ");
                else sb.append("* ");
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }
        return sb.toString();
    }

}
