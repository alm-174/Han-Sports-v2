package com.javaweb.ghn.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GhnShippingDTOs {

    public record ServiceOption(
            @JsonProperty("service_id") Integer serviceId,
            @JsonProperty("short_name") String shortName,
            @JsonProperty("service_type_id") Integer serviceTypeId
    ) {
    }

    public record FeeRequest(
            @JsonProperty("to_district_id") Integer toDistrictId,
            @JsonProperty("to_ward_code") String toWardCode,
            @JsonProperty("insurance_value") Integer insuranceValue,
            @JsonProperty("coupon") String coupon
    ) {
    }

    public record FeeResponse(
            @JsonProperty("service_fee") Integer serviceFee,
            @JsonProperty("insurance_fee") Integer insuranceFee,
            @JsonProperty("coupon_value") Integer couponValue,
            @JsonProperty("pick_station_fee") Integer pickStationFee,
            @JsonProperty("r2s_fee") Integer r2sFee
    ) {
    }

    public record LeadtimeResponse(
            @JsonProperty("leadtime") Long leadtime,
            @JsonProperty("order_date") Long orderDate,
            @JsonProperty("shift_date") Long shiftDate
    ) {
    }

    public record CreateOrderRequest(
            @JsonProperty("payment_type_id") Integer paymentTypeId,
            @JsonProperty("note") String note,
            @JsonProperty("required_note") String requiredNote,
            @JsonProperty("return_phone") String returnPhone,
            @JsonProperty("return_address") String returnAddress,
            @JsonProperty("return_district_id") Integer returnDistrictId,
            @JsonProperty("return_ward_code") String returnWardCode,
            @JsonProperty("client_order_code") String clientOrderCode,
            @JsonProperty("to_name") String toName,
            @JsonProperty("to_phone") String toPhone,
            @JsonProperty("to_address") String toAddress,
            @JsonProperty("to_ward_code") String toWardCode,
            @JsonProperty("to_district_id") Integer toDistrictId,
            @JsonProperty("cod_amount") Integer codAmount,
            @JsonProperty("content") String content,
            @JsonProperty("coupon") String coupon
    ) {
    }

    public record CreateOrderResponse(
            @JsonProperty("order_code") String orderCode,
            @JsonProperty("sort_code") String sortCode,
            @JsonProperty("trans_type") String transType,
            @JsonProperty("payment_type") Integer paymentType,
            @JsonProperty("expected_delivery_time") String expectedDeliveryTime
    ) {
    }

    public record OrderDetailResponse(
            @JsonProperty("order_code") String orderCode,
            @JsonProperty("status") String status,
            @JsonProperty("to_name") String toName,
            @JsonProperty("cod_amount") Long codAmount,
            @JsonProperty("leadtime") Long leadtime,
            @JsonProperty("finish_date") Long finishDate
    ) {
    }

    public record CancelResult(
            @JsonProperty("order_code") String orderCode,
            @JsonProperty("result") Boolean result,
            @JsonProperty("message") String message
    ) {
    }
}
