package com.arshmed.customerservice.event;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerCreationListener {

    private final CustomerService customerService;

    @RabbitListener(queues = "${rabbitmq.queue.customer-creation}")
    public void handleCustomerCreation(UserCreatedEvent event) {
        log.info("Event received to create customer profile for authId: {}", event.authId());
        
        var request = new CustomerRequest(
                null,
                event.firstname(),
                event.lastname(),
                event.email(),
                null, 
                "I",
                null,
                event.authId()
        );
        
        customerService.createCustomer(request);
        log.info("Customer profile successfully created for authId: {}", event.authId());
    }
}