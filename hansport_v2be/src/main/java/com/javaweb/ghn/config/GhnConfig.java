package com.javaweb.ghn.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties(GhnProperties.class)
public class GhnConfig {

    @Bean
    public RestTemplate ghnRestTemplate(GhnProperties props) {
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor headerInterceptor = (request, body, execution) -> {
            request.getHeaders().setAccept(List.of(MediaType.APPLICATION_JSON));
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            if (props.token() != null && !props.token().isBlank()) {
                request.getHeaders().set("Token", props.token());
            }
            if (props.shopId() != null && props.shopId() > 0) {
                request.getHeaders().set("ShopId", String.valueOf(props.shopId()));
            }
            return execution.execute(request, body);
        };
        restTemplate.setInterceptors(List.of(headerInterceptor));
        return restTemplate;
    }
}
