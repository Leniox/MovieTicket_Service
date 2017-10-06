package com.ethan.code;

import com.ethan.code.domain.Seat;
import com.ethan.code.domain.Status;
import com.ethan.code.service.TicketService;
import com.ethan.code.service.TicketServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@PropertySource("classpath:application.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TicketServiceTest {

    private static final Logger logger = LogManager.getLogger(TicketServiceTest.class);

    @Autowired
    TicketService ticketService;

    @Autowired
    protected Environment env;

    @Test
    public void test1_IsTicketService() {

        assertThat(ticketService, instanceOf(TicketService.class));
    }

    @Test
    public void test2_VenueInitializationRowColumn() {

        assertThat(ticketService.getVenue().getRowNum(), equalTo(Integer.valueOf(env.getProperty("rowNum"))));
        assertThat(ticketService.getVenue().getColNum(), equalTo(Integer.valueOf(env.getProperty("columnNum"))));

    }

    @Test
    public void test3_VenueInitializationAvailableSeatsNum() {

        assertThat(ticketService.numSeatsAvailable(), equalTo(Integer.valueOf(env.getProperty("rowNum")) * Integer.valueOf(env.getProperty("columnNum"))));

    }

    @Test
    public void test4_VenueAllUnBooked() {

        Seat[][] seats = ticketService.getVenue().getSeats();

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                sb.append("_ ");
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }

        assertThat(ticketService.getVenue().toString(), equalTo(sb.toString()));

    }

    @Test
    public void test5_VenueAvailableNumberAfterHold() {

        ticketService.findAndHoldSeats(5, "test@test.com");
        assertThat(ticketService.numSeatsAvailable(), equalTo(Integer.valueOf(env.getProperty("rowNum")) * Integer.valueOf(env.getProperty("columnNum")) - 5 ));

    }

    @Test
    public void test6_SeatsArrangementAfterFirstSeatHold() {

        Seat[][] seats = ticketService.getVenue().getSeats();

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (i == 4 && (j >= 6 && j <= 10)) {
                    sb.append("# ");
                } else {
                    sb.append("_ ");
                }
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }

        assertThat(ticketService.getVenue().toString(), equalTo(sb.toString()));

    }

    @Test
    public void test7_SeatsArrangementAfterHoldSpecificSeat() {

        ticketService.holdSeatByPosition(4, 4, "test@test.com");
        ticketService.holdSeatByPosition(4, 12, "test@test.com");
        ticketService.holdSeatByPosition(3, 5, "test@test.com");
        ticketService.holdSeatByPosition(3, 6, "test@test.com");
        ticketService.holdSeatByPosition(3, 7, "test@test.com");
        ticketService.holdSeatByPosition(3, 8, "test@test.com");
        ticketService.holdSeatByPosition(5, 6, "test@test.com");
        ticketService.holdSeatByPosition(5, 7, "test@test.com");
        ticketService.holdSeatByPosition(5, 8, "test@test.com");
        ticketService.holdSeatByPosition(5, 9, "test@test.com");
        ticketService.holdSeatByPosition(5, 10, "test@test.com");

        assertThat(ticketService.numSeatsAvailable(), equalTo(Integer.valueOf(env.getProperty("rowNum")) * Integer.valueOf(env.getProperty("columnNum")) - 16 ));

        Seat[][] seats = ticketService.getVenue().getSeats();

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (i == 4 && ((j >= 6 && j <= 10) || j == 4 || j ==12 )) {
                    sb.append("# ");
                } else if (i == 3 && (j >= 5 && j <= 8)) {
                    sb.append("# ");
                } else if (i == 5 && (j >= 6 && j <= 10)) {
                    sb.append("# ");
                } else {
                    sb.append("_ ");
                }
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }

        assertThat(ticketService.getVenue().toString(), equalTo(sb.toString()));

    }

    @Test
    public void test8_SeatsArrangementAfterManyHolds() {

        ticketService.findAndHoldSeats(5, "test@test.com");

        assertThat(ticketService.numSeatsAvailable(), equalTo(Integer.valueOf(env.getProperty("rowNum")) * Integer.valueOf(env.getProperty("columnNum")) - 21 ));

        Seat[][] seats = ticketService.getVenue().getSeats();

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (i == 4 && ((j >= 6 && j <= 12) || j == 4 )) {
                    sb.append("# ");
                } else if (i == 3 && (j >= 5 && j <= 11)) {
                    sb.append("# ");
                } else if (i == 5 && (j >= 6 && j <= 11)) {
                    sb.append("# ");
                } else {
                    sb.append("_ ");
                }
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }

        assertThat(ticketService.getVenue().toString(), equalTo(sb.toString()));

    }

    @Test
    public void test9_CommitSeatHoldSuccessful() {

        int size = ticketService.getSeatHolds().size();

        ticketService.reserveSeats(size - 1, "test@test.com");

        Seat[][] seats = ticketService.getVenue().getSeats();

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for(int i = 0; i < seats.length; i++) {
            for(int j = 0; j < seats[0].length; j++) {
                if (i == 3 && (j >= 5 && j <= 8)) {
                    sb.append("# ");
                } else if (i == 3 && (j >= 9 && j <= 11)) {
                    sb.append("* ");
                } else if (i == 4 && (j == 4 || (j >= 6 && j <= 10) || j == 12 )) {
                    sb.append("# ");
                } else if ((i == 4 || i == 5) && j == 11) {
                    sb.append("* ");
                } else if (i == 5 && (j >= 6 && j <= 10)) {
                    sb.append("# ");
                } else {
                    sb.append("_ ");
                }
                if (j == seats[0].length - 1) sb.append('\n');
            }
        }

        assertThat(ticketService.getVenue().toString(), equalTo(sb.toString()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void test10_CommitThrowEmailNotConsistent() {

        int size = ticketService.getSeatHolds().size();

        ticketService.reserveSeats(size - 2, "test@testtest.com");

    }

    @Test(expected = IllegalArgumentException.class)
    public void test11_CommitThrowNoMatchedID() {

        int size = ticketService.getSeatHolds().size();

        ticketService.reserveSeats(size + 1, "test@test.com");

    }

    @Test(expected = RuntimeException.class)
    public void test12_HoldHasExpired() {

        try {
            Thread.sleep(6000);

            int size = ticketService.getSeatHolds().size();

            ticketService.reserveSeats(size - 3, "test@test.com");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
