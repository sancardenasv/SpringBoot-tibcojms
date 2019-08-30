package com.electroeing.tibcojms.conf.tibco;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordManagerConfiguration {
    @Value("${jms.tibco.credentials.password}")
    private String tibcoPassword;

    @Bean("tibcoPassword")
    String tibcoPassword() {
        return tibcoPassword;
    }

}
