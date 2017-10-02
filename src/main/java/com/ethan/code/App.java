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

        ticketService.findAndHoldSeats(7, "waldenlaker@gmail.com");


        //Gson gson = new Gson();

//        ticketService = new TicketServiceImpl();
//

//        System.out.println( "Hello World!" );
        logger.info("Hello World");

//        Venue venue = new VenueImpl(16, 8);
//        venue.initializeSeats();
//        ticketService.numSeatsAvailable();

//        logger.info(gson.toJson(venue));

    }

    public static ApplicationContext getAppContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        return context;
    }

}
