package com.ethan.code.service;

import com.ethan.code.domain.Seat;

import java.util.Set;

public interface UtilService {

    public void changeStatusToHold(Set<Seat> set);

    public void changeStatusToReserved(Set<Seat> set);

    public int getExpireSec();

    public void setExpireSec(int expireSec);

}
