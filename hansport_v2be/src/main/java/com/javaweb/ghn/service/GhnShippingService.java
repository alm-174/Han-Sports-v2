package com.javaweb.ghn.service;

import com.javaweb.ghn.config.GhnProperties;
import com.javaweb.ghn.dto.GhnResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.CancelResult;
import com.javaweb.ghn.dto.GhnShippingDTOs.CreateOrderRequest;
import com.javaweb.ghn.dto.GhnShippingDTOs.CreateOrderResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.FeeRequest;
import com.javaweb.ghn.dto.GhnShippingDTOs.FeeResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.LeadtimeResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.OrderDetailResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.ServiceOption;
import com.javaweb.ghn.exception.GhnApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GhnShippingService {

    private final RestTemplate restTemplate;
    private final GhnProperties properties;

    public GhnShippingService(RestTemplate restTemplate, GhnProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public List<ServiceOption> getAvailableServices(int fromDistrictId, int toDistrictId) {
        Map<String, Object> body = Map.of(
                "shop_id", properties.shopId(),
                "from_district", fromDistrictId,
                "to_district", toDistrictId
        );
        return execute(buildUrl("/shiip/public-api/v2/shipping-order/available-services"), HttpMethod.POST, body,
                new ParameterizedTypeReference<>() {
                });
    }

    public FeeResponse calculateFee(FeeRequest request) {
        Map<String, Object> body = buildFeePayload(request);
        return execute(buildUrl("/shiip/public-api/v2/shipping-order/fee"), HttpMethod.POST, body,
                new ParameterizedTypeReference<>() {
                });
    }

    public LeadtimeResponse estimateLeadtime(int toDistrictId, String toWardCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("from_district_id", properties.fromDistrictId());
        body.put("from_ward_code", properties.fromWardCode());
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);
        body.put("service_id", properties.defaultServiceId());
        return execute(buildUrl("/shiip/public-api/v2/shipping-order/leadtime"), HttpMethod.POST, body,
                new ParameterizedTypeReference<>() {
                });
    }

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("payment_type_id", request.paymentTypeId());
        body.put("note", request.note());
        body.put("required_note", request.requiredNote());
        body.put("return_phone", request.returnPhone());
        body.put("return_address", request.returnAddress());
        body.put("return_district_id", request.returnDistrictId());
        body.put("return_ward_code", request.returnWardCode());
        body.put("client_order_code", request.clientOrderCode());
        body.put("to_name", request.toName());
        body.put("to_phone", request.toPhone());
        body.put("to_address", request.toAddress());
        body.put("to_ward_code", request.toWardCode());
        body.put("to_district_id", request.toDistrictId());
        body.put("cod_amount", request.codAmount());
        body.put("content", request.content());
        body.put("coupon", request.coupon());
        body.put("service_id", properties.defaultServiceId());
        body.put("weight", properties.defaultWeight());
        body.put("length", properties.defaultLength());
        body.put("width", properties.defaultWidth());
        body.put("height", properties.defaultHeight());
        return execute(buildUrl("/shiip/public-api/v2/shipping-order/create"), HttpMethod.POST, body,
                new ParameterizedTypeReference<>() {
                });
    }

    public OrderDetailResponse getOrderDetail(String orderCode) {
        return execute(buildUrl("/shiip/public-api/v2/shipping-order/detail"), HttpMethod.POST,
                Map.of("order_code", orderCode), new ParameterizedTypeReference<>() {
                });
    }

    public OrderDetailResponse getOrderDetailByClientCode(String clientOrderCode) {
        return execute(buildUrl("/shiip/public-api/v2/shipping-order/detail-by-client-code"), HttpMethod.POST,
                Map.of("client_order_code", clientOrderCode), new ParameterizedTypeReference<>() {
                });
    }

    public List<CancelResult> cancelOrders(List<String> orderCodes) {
        return execute(buildUrl("/shiip/public-api/v2/switch-status/cancel"), HttpMethod.POST,
                Map.of("order_codes", orderCodes), new ParameterizedTypeReference<>() {
                });
    }

    private Map<String, Object> buildFeePayload(FeeRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("from_district_id", properties.fromDistrictId());
        body.put("from_ward_code", properties.fromWardCode());
        body.put("to_district_id", request.toDistrictId());
        body.put("to_ward_code", request.toWardCode());
        body.put("service_id", properties.defaultServiceId());
        body.put("weight", properties.defaultWeight());
        body.put("length", properties.defaultLength());
        body.put("width", properties.defaultWidth());
        body.put("height", properties.defaultHeight());
        body.put("insurance_value", request.insuranceValue());
        body.put("coupon", request.coupon());
        return body;
    }

    private <T> T execute(String url, HttpMethod method, Object body, ParameterizedTypeReference<GhnResponse<T>> responseType) {
        HttpEntity<?> requestEntity = body == null ? HttpEntity.EMPTY : new HttpEntity<>(body);
        ResponseEntity<GhnResponse<T>> response = restTemplate.exchange(url, method, requestEntity, responseType);
        GhnResponse<T> ghnResponse = response.getBody();
        if (ghnResponse == null || ghnResponse.code() == null || ghnResponse.code() != 200) {
            throw new GhnApiException("GHN API returned an invalid response: " + (ghnResponse == null ? "empty body" : ghnResponse.message()));
        }
        return ghnResponse.data() == null ? (T) Collections.emptyList() : ghnResponse.data();
    }

    private String buildUrl(String path) {
        String baseUrl = StringUtils.hasText(properties.baseUrl())
                ? properties.baseUrl()
                : "https://online-gateway.ghn.vn";
        return baseUrl.replaceAll("/+$", "") + path;
    }
}
