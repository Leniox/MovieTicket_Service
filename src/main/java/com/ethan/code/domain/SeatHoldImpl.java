package com.ethan.code.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatHoldImpl implements SeatHold {

    private static final Logger logger = LogManager.getLogger(SeatHoldImpl.class);

    //use AtomicInteger to make sure the ID is unique
    private static AtomicInteger uniqueId = new AtomicInteger();

    protected int setHoldId;

    protected Set<Seat> holdSeats;

    protected String emailAddress;

    protected String confirmCode;

    protected long expiresMS;

    public SeatHoldImpl(Set<Seat> holdSeats, String emailAddress, int expiresSec) {
        this.holdSeats = holdSeats;
        this.emailAddress = emailAddress;
        this.setHoldId = uniqueId.getAndIncrement();
        //set the expire time
        this.expiresMS = System.currentTimeMillis() + expiresSec * 1000;
    }

    public boolean isCommitted() {
        return this.confirmCode != null && !this.confirmCode.isEmpty();
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

    public boolean isExpired() {
        return System.currentTimeMillis() >= this.expiresMS;
    }

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getConfirmCode() { return confirmCode; }

    public void setConfirmCode(String confirmCode) { this.confirmCode = confirmCode; }

    public long getExpiresMS() { return expiresMS; }

    public void setExpiresMS(long expiresMS) { this.expiresMS = expiresMS; }
}
