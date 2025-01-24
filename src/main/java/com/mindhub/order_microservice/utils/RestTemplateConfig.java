package com.mindhub.order_microservice.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        //restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
        //    @Override
        //    public void handleError(ClientHttpResponse response) throws IOException {
        //        System.out.println("xdxdxdxdxdxdxd");
        //    }
        //});

        return restTemplate;
    }
}
