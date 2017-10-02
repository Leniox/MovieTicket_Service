package com.ethan.code.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatHoldImpl implements SeatHold {

    private static final Logger logger = LogManager.getLogger(SeatHoldImpl.class);

    private static AtomicInteger uniqueId = new AtomicInteger();

    protected int setHoldId;

    protected Set<Seat> holdSeats;

    protected Customer customer;

    protected String emailAddress;

    protected Timestamp holdAtTime;

    protected String confirmCode;

    protected long expiresMS;

    public SeatHoldImpl(Set<Seat> holdSeats, String emailAddress) {
        this.holdSeats = holdSeats;
        this.emailAddress = emailAddress;
        this.setHoldId = uniqueId.getAndIncrement();
        this.expiresMS = System.currentTimeMillis() + 1 * 1000;
        logger.info(this);
        logger.info(this.setHoldId);
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
        return System.currentTimeMillis() >= this.expiresMS;
    }

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getConfirmCode() { return confirmCode; }

    public void setConfirmCode(String confirmCode) { this.confirmCode = confirmCode; }

    public long getExpiresMS() { return expiresMS; }

    public void setExpiresMS(long expiresMS) { this.expiresMS = expiresMS; }
}
