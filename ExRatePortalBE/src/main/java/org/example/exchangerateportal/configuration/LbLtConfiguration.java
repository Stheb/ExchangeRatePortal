package org.example.exchangerateportal.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LbLtConfiguration {

    @Value("${exchange.rate.base.url:https://www.lb.lt/webServices/FxRates/FxRates.asmx}")
    private String baseUrl;

    @Bean
    public WebClient lbLtWebClientBuilder() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}
