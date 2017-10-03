package com.ethan.code;

import com.ethan.code.service.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    private static final Logger logger = LogManager.getLogger(AppConfig.class);

    @Autowired
    protected Environment env;

    @Bean(name="utilService")
    public UtilService getUtilService() {
        UtilService utilService = new UtilServiceImpl();
        utilService.setExpireSec(Integer.valueOf(env.getProperty("expiration")));
        return utilService;
    }

    @Bean(name="ticketService")
    public TicketService getTicketService() {
        int rowNum = Integer.valueOf(env.getProperty("rowNum"));
        int columnNum = Integer.valueOf(env.getProperty("columnNum"));
        TicketService ticketService = new TicketServiceImpl(rowNum, columnNum);
        return ticketService;
    }



}
