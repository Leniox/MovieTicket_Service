package com.ethan.code.service;

import com.ethan.code.domain.Seat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UtilServiceImpl implements UtilService {

    private static final Logger logger = LogManager.getLogger(UtilServiceImpl.class);

    protected int expireSec;

    /**
     * util function to change the status of all seats in the set to Hold
     * @param set
     */
    public void changeStatusToHold(Set<Seat> set) {
        if (set == null || set.isEmpty()) return;
        else {
            set.forEach(Seat::changeToHold);
        }
    }

    /**
     * util function to change the status of all seats in the set to Reserved
     * @param set
     */
    public void changeStatusToReserved(Set<Seat> set) {
        if (set == null || set.isEmpty()) return;
        else {
            set.forEach(Seat::changeToReserved);
        }
    }

    public int getExpireSec() {
        return expireSec;
    }

    public void setExpireSec(int expireSec) {
        this.expireSec = expireSec;
    }

}
