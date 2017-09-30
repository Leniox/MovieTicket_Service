package com.ethan.code.domain;

public interface Seat {

    public Status getStatus();

    public void setStatus(Status status);

    public int getColumn();

    public void setColumn(int column);

    public int getRow();

    public void setRow(int row);

    public Customer getCustomer();

    public void setCustomer(Customer customer);

}
