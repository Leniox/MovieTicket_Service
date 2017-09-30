package com.ethan.code.domain;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VenueImpl implements Venue {

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
                        seats[x][y] = new SeatImpl(x, y, Status.UNBOOKED);
                    }));
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
}
