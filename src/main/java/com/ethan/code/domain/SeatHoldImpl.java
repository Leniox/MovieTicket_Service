package com.ethan.code.domain;

import java.sql.Timestamp;
import java.util.Set;

public class SeatHoldImpl implements SeatHold {

    protected int setHoldId;

    protected Set<Seat> holdSeats;

    protected Customer customer;

    protected String emailAddress;

    protected Timestamp holdAtTime;

    protected boolean isExpired;

    public SeatHoldImpl(Set<Seat> holdSeats, Customer customer) {
        this.holdSeats = holdSeats;
        this.customer = customer;
    }

    public SeatHoldImpl(Set<Seat> holdSeats, String emailAddress) {
        this.holdSeats = holdSeats;
        this.emailAddress = emailAddress;
    }

    public int getSetHoldId() {
        return setHoldId;
    }

    public void setSetHoldId(int setHoldId) {
        this.setHoldId = setHoldId;
    }

    public Set<Seat> getHoldSeats() {
        return holdSeats;
    }

    public void setHoldSeats(Set<Seat> holdSeats) {
        this.holdSeats = holdSeats;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Timestamp getHoldAtTime() {
        return holdAtTime;
    }

    public void setHoldAtTime(Timestamp holdAtTime) {
        this.holdAtTime = holdAtTime;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
