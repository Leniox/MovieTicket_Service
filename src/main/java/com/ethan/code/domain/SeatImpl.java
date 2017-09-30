package com.ethan.code.domain;

public class SeatImpl implements Seat{

    protected Status status;

    protected int column;

    protected int row;

    protected Customer customer;

    public SeatImpl(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public SeatImpl(int column, int row, Status status) {
        this.status = status;
        this.column = column;
        this.row = row;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
