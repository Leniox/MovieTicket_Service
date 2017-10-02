package com.ethan.code;

import com.ethan.code.service.TicketService;
import com.ethan.code.service.TicketServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(name="ticketService")
    public TicketService getTicketService() {
        return new TicketServiceImpl();
    }

    @Bean(name="customerService")
    public CustomerService getCustomerService() {
        return new CustomerServiceImpl();
    }


}
