package com.pratik.major.paymentPartner;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class PaypalConfig {
    @Value("${paypal.client-id}")
    public String clientId;
    @Value("${paypal.client-secret}")
    public String clientSecret;
    @Value("${paypal.mode}")
    public String mode;

    /*@Bean
    public APIContext apiContext(){
        return new APIContext(clientId,clientSecret,mode);
    }*/
    @Bean
    public APIContext apiContext(){
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
        apiContext.setRequestId(UUID.randomUUID().toString()); // Generate a unique request ID
        return apiContext;
    }
}
