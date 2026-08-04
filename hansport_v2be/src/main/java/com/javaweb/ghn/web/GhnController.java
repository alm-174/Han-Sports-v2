package com.javaweb.ghn.web;

import com.javaweb.ghn.config.GhnProperties;
import com.javaweb.ghn.dto.GhnMasterDataDTOs.District;
import com.javaweb.ghn.dto.GhnMasterDataDTOs.Province;
import com.javaweb.ghn.dto.GhnMasterDataDTOs.Ward;
import com.javaweb.ghn.dto.GhnOrderStatusDTOs;
import com.javaweb.ghn.dto.GhnShippingDTOs.CreateOrderRequest;
import com.javaweb.ghn.dto.GhnShippingDTOs.CreateOrderResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.FeeRequest;
import com.javaweb.ghn.dto.GhnShippingDTOs.FeeResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.LeadtimeResponse;
import com.javaweb.ghn.dto.GhnShippingDTOs.CancelResult;
import com.javaweb.ghn.dto.GhnShippingDTOs.OrderDetailResponse;
import com.javaweb.ghn.service.GhnAddressService;
import com.javaweb.ghn.service.GhnShippingService;
import com.javaweb.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ghn")
public class GhnController {

    private final GhnAddressService addressService;
    private final GhnShippingService shippingService;
    private final GhnProperties ghnProperties;
    private final OrderService orderService;

    public GhnController(GhnAddressService addressService,
                         GhnShippingService shippingService,
                         GhnProperties ghnProperties,
                         OrderService orderService) {
        this.addressService = addressService;
        this.shippingService = shippingService;
        this.ghnProperties = ghnProperties;
        this.orderService = orderService;
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getProvinces() {
        List<Province> provinces = addressService.getProvinces();
        return ResponseEntity.ok(provinces);
    }

    @PostMapping("/districts")
    public ResponseEntity<List<District>> getDistricts(@RequestBody IdRequest request) {
        List<District> districts = addressService.getDistricts(request.id());
        return ResponseEntity.ok(districts);
    }

    @PostMapping("/wards")
    public ResponseEntity<List<Ward>> getWards(@RequestBody IdRequest request) {
        List<Ward> wards = addressService.getWards(request.id());
        return ResponseEntity.ok(wards);
    }

    @PostMapping("/fee")
    public ResponseEntity<FeeResponse> fee(@RequestBody FeeRequest request) {
        // #region agent log
        try (var w = new java.io.FileWriter("debug-c76d45.log", true)) {
            w.write("{\"sessionId\":\"c76d45\",\"location\":\"GhnController.java:fee\",\"message\":\"fee request\",\"data\":{\"toDistrictId\":" + request.toDistrictId() + ",\"toWardCode\":\"" + (request.toWardCode() == null ? "" : request.toWardCode()) + "\"},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"D\"}\n");
        } catch (Exception ignored) {}
        // #endregion
        return ResponseEntity.ok(shippingService.calculateFee(request));
    }

    @PostMapping("/leadtime")
    public ResponseEntity<LeadtimeResponse> leadtime(@RequestBody LeadtimeRequest request) {
        return ResponseEntity.ok(shippingService.estimateLeadtime(request.toDistrictId(), request.toWardCode()));
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(shippingService.createOrder(request));
    }

    @PostMapping("/detail")
    public ResponseEntity<OrderDetailResponse> detail(@RequestBody OrderCodeRequest request) {
        return ResponseEntity.ok(shippingService.getOrderDetail(request.orderCode()));
    }

    @PostMapping("/detail-by-client-code")
    public ResponseEntity<OrderDetailResponse> detailByClientCode(@RequestBody ClientOrderCodeRequest request) {
        return ResponseEntity.ok(shippingService.getOrderDetailByClientCode(request.clientOrderCode()));
    }

    @PostMapping("/cancel")
    public ResponseEntity<List<CancelResult>> cancel(@RequestBody CancelRequest request) {
        return ResponseEntity.ok(shippingService.cancelOrders(request.orderCodes()));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(@RequestHeader(value = "X-Webhook-Token", required = false) String token,
                                               @RequestBody GhnOrderStatusDTOs payload) {
        if (ghnProperties.webhookToken() != null && !ghnProperties.webhookToken().isBlank()) {
            if (!ghnProperties.webhookToken().equals(token)) {
                return ResponseEntity.status(401).build();
            }
        }
        orderService.updateOrderStatusFromGhn(payload.clientOrderCode(), payload.orderCode(), payload.status());
        return ResponseEntity.ok().build();
    }

    public static record IdRequest(int id) {}
    public static record LeadtimeRequest(int toDistrictId, String toWardCode) {}
    public static record OrderCodeRequest(String orderCode) {}
    public static record ClientOrderCodeRequest(String clientOrderCode) {}
    public static record CancelRequest(List<String> orderCodes) {}
}
