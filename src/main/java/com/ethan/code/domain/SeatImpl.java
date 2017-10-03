package com.ethan.code.domain;

public class SeatImpl implements Seat{

    protected Status status;

    protected int column;

    protected int row;

    protected String emailAddress;

    protected int score;

    public SeatImpl(int row, int column, Status status, int score) {
        this.status = status;
        this.column = column;
        this.row = row;
        this.score = score;
    }

    public boolean isActive() {

        return status == Status.UNBOOKED;
    }

    public void changeToHold() {

        this.status = Status.HOLD;
    }

    public void changeToReserved() {

        this.status = Status.RESERVED;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    @Override
    public String toString() {
        return String.valueOf(this.score);
    }
}
