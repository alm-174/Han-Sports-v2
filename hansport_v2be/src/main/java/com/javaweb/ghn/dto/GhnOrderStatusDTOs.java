package com.javaweb.ghn.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GhnOrderStatusDTOs(
        @JsonProperty("client_order_code")
        @JsonAlias({"ClientOrderCode", "clientOrderCode"})
        String clientOrderCode,
        @JsonProperty("status")
        @JsonAlias({"Status", "status"})
        String status,
        @JsonProperty("trans_type")
        @JsonAlias({"TransType", "transType"})
        String transType,
        @JsonProperty("order_code")
        @JsonAlias({"OrderCode", "orderCode"})
        String orderCode,
        @JsonProperty("cod_amount")
        @JsonAlias({"CODAmount", "codAmount"})
        Integer codAmount,
        @JsonProperty("shipping_fee")
        @JsonAlias({"Fee", "shippingFee"})
        Integer shippingFee
) {
}
