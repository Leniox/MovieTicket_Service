package com.ethan.code;

import com.ethan.code.domain.Seat;
import com.ethan.code.domain.SeatImpl;
import com.ethan.code.domain.Status;
import com.ethan.code.service.TicketService;
import com.ethan.code.service.UtilService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UtilServiceTest {

    @Autowired
    UtilService utilService;

    @Test
    public void test1_changeStatusToHold() {

        Set<Seat> set = new HashSet<>();

        Seat seat1 = new SeatImpl(2, 3, Status.UNBOOKED, 5);
        Seat seat2 = new SeatImpl(2, 4, Status.UNBOOKED, 6);
        Seat seat3 = new SeatImpl(2, 5, Status.UNBOOKED, 7);
        set.add(seat1);
        set.add(seat2);
        set.add(seat3);

        utilService.changeStatusToHold(set);

        set.forEach(seat -> {
            assertThat(seat.getStatus(), is(Status.HOLD));
        });

    }

    @Test
    public void test2_changeStatusToReserved() {

        Set<Seat> set = new HashSet<>();

        Seat seat1 = new SeatImpl(2, 3, Status.HOLD, 5);
        Seat seat2 = new SeatImpl(2, 4, Status.HOLD, 6);
        Seat seat3 = new SeatImpl(2, 5, Status.HOLD, 7);
        set.add(seat1);
        set.add(seat2);
        set.add(seat3);

        utilService.changeStatusToReserved(set);

        set.forEach(seat -> {
            assertThat(seat.getStatus(), is(Status.RESERVED));
        });

    }

}
