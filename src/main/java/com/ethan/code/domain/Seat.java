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

    public String getEmailAddress();

    public void setEmailAddress(String emailAddress);

    public int getScore();

    public void setScore(int score);

    public boolean isActive();

    public void changeToHold();

    public void changeToReserved();

}
