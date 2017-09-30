package com.ethan.code;

import com.ethan.code.domain.Venue;
import com.ethan.code.domain.VenueImpl;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Hello world!
 *
 */
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) {

        Gson gson = new Gson();


//        System.out.println( "Hello World!" );
        logger.info("Hello World");

        Venue venue = new VenueImpl(16, 8);
        venue.initializeSeats();

        logger.info(gson.toJson(venue));

    }
}
