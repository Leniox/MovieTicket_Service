package com.ethan.code;

import com.ethan.code.domain.Venue;
import com.ethan.code.domain.VenueImpl;
import com.ethan.code.service.TicketService;
import com.ethan.code.service.TicketServiceImpl;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 * Hello world!
 *
 */
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    @Autowired
    protected static TicketService ticketService;

    public static void main( String[] args ) {

        ApplicationContext context = getAppContext();

        //App app = context.getBean("App", App.class);

        ticketService = (TicketServiceImpl) context.getBean("ticketService");
        ticketService.numSeatsAvailable();

        ticketService.getSeatHolds().add(ticketService.findAndHoldSeats(5, "waldenlaker@gmail.com"));

        ticketService.getSeatHolds().add(ticketService.findAndHoldSeats(3, "waldenlaker@gmail.com"));

        ticketService.getSeatHolds().add(ticketService.findAndHoldSeats(1, "waldenlaker@gmail.com"));

        try{
            ticketService.reserveSeats(0, "waldenlaker@gmail.com");

        } catch (IllegalArgumentException e) {
            logger.catching(e);
        } catch (Exception e) {
            logger.catching(e);
        }

        try{
            ticketService.reserveSeats(4, "waldenlaker@gmail.com");

        } catch (IllegalArgumentException e) {
            logger.catching(e);
        } catch (Exception e) {
            logger.catching(e);
        }

        try{
            ticketService.reserveSeats(2, "test@gmail.com");
        } catch (IllegalArgumentException e) {
            logger.catching(e);
        } catch (Exception e) {
            logger.catching(e);
        }

        try{
            Thread.sleep(2000);
            ticketService.reserveSeats(2, "waldenlaker@gmail.com");
        } catch (IllegalArgumentException e) {
            logger.catching(e);
        } catch (Exception e) {
            logger.catching(e);
        }


        logger.info("Hello World");

    }

    public static ApplicationContext getAppContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        return context;
    }

}
