package com.micro.order.config;

import com.micro.common.events.order.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderConfig {

    @Bean
    public Sinks.Many<OrderEvent> orderSink(){
    	System.out.println("OrderConfig.orderSink");
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sink){
    	System.out.println("OrderConfig.orderSupplier");
        return sink::asFlux;
    }

}
