package com.javaweb.controller;

import com.javaweb.domain.Order;
import com.javaweb.domain.User;
import com.javaweb.domain.request.ReqOrderDTO;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.order.ResOrderDTO;
import com.javaweb.service.OrderService;
import com.javaweb.util.SecurityUtil;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<ResOrderDTO> placeOrder(@RequestBody @Valid ReqOrderDTO redOrderDTO) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        ResOrderDTO order = this.orderService.placeOrder(email, redOrderDTO);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders")
    public ResponseEntity<Void> updateOrder(@RequestBody Order order)
    {
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/orders")
    public ResponseEntity<ResultPaginationDTO> getAllOrders(@Filter Specification<Order> spec,
                                                            Pageable pageable)
    {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.fetchAllOrders(spec, pageable));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        this.orderService.deleteOrder(email, id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
