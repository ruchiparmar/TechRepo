package com.micro.payment.config;

import com.micro.common.events.order.OrderEvent;
import com.micro.common.events.order.OrderStatus;
import com.micro.common.events.payment.PaymentEvent;
import com.micro.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class PaymentConfig {

    @Autowired
    private PaymentService service;

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return flux -> flux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent event){
    	System.out.println("PaymentConfig.processPayment - Order Status" + event.getOrderStatus());
        if(event.getOrderStatus().equals(OrderStatus.ORDER_CREATED)){
        	System.out.println("PaymentConfig.processPayment - New Order");
            return Mono.fromSupplier(() -> this.service.newOrderEvent(event));
        }else{
        	System.out.println("PaymentConfig.processPayment - cancel Order");
            return Mono.fromRunnable(() -> this.service.cancelOrderEvent(event));
        }
    }

}
