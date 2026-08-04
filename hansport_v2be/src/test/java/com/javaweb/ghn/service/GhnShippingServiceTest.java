package com.javaweb.ghn.service;

import com.javaweb.ghn.config.GhnProperties;
import com.javaweb.ghn.dto.GhnResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.FeeRequest;
import com.javaweb.ghn.dto.GhnShippingDTOs.FeeResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GhnShippingServiceTest {

    @Test
    void calculateFeeUsesConfiguredDefaultsWhenRequestOmitsShippingDimensions() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        GhnProperties properties = new GhnProperties(
                "https://example.test",
                "token",
                5317467,
                3303,
                "1B2716",
                "",
                53320,
                20,
                20,
                10,
                500
        );
        GhnShippingService service = new GhnShippingService(restTemplate, properties);
        FeeResponse expected = new FeeResponse(10000, 1000, 0, 0, 0);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(new ResponseEntity<>(new GhnResponse<>(200, "ok", expected), HttpStatus.OK));

        FeeRequest request = new FeeRequest(3303, "1B2716", 1500000, null);

        service.calculateFee(request);

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), captor.capture(), any(ParameterizedTypeReference.class));

        Map<String, Object> body = (Map<String, Object>) captor.getValue().getBody();
        assertEquals(53320, body.get("service_id"));
        assertEquals(500, body.get("weight"));
        assertEquals(20, body.get("length"));
        assertEquals(20, body.get("width"));
        assertEquals(10, body.get("height"));
        assertEquals(1500000, body.get("insurance_value"));
    }
}
