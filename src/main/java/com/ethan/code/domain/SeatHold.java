package com.ethan.code.domain;

import java.sql.Timestamp;
import java.util.Set;

public interface SeatHold {

    public int getSetHoldId();

    public void setSetHoldId(int setHoldId);

    public Set<Seat> getHoldSeats();

    public void setHoldSeats(Set<Seat> holdSeats);

    public Customer getCustomer();

    public void setCustomer(Customer customer);

    public Timestamp getHoldAtTime();

    public void setHoldAtTime(Timestamp holdAtTime);

    public boolean isExpired();

    public void setExpired(boolean expired);
}
