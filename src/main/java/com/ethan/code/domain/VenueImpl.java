package com.ethan.code.domain;

import com.ethan.code.service.TicketServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VenueImpl implements Venue {

    private static final Logger logger = LogManager.getLogger(VenueImpl.class);

    public VenueImpl(int rowNum, int colNum) {
        System.out.println("creating a new Venue");
        seats = new Seat[rowNum][colNum];
    }

    protected Seat[][] seats;

    public void initializeSeats() {
//        Stream.of(seats).flatMap(Stream::of).forEach(System.out::println);
        IntStream.range(0, seats.length)
                 .forEach( x -> IntStream.range(0, seats[0].length)
                    .forEach( y -> {
                        seats[x][y] = new SeatImpl(x, y, Status.UNBOOKED, getSeatScore(x, y));
                    }));
    }

    public int getSeatScore(int rowNum, int colNum) {
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

    @Override
    public String toString() {
        return Arrays.deepToString(seats);
    }

}
