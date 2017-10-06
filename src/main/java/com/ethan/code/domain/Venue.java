package com.ethan.code.domain;

public interface Venue {

    public void initializeSeats();

    public int getRowNum();

    public int getColNum();

    public Seat[][] getSeats();

    public void setSeats(Seat[][] seats);

    public String printSeatsScore();
}
