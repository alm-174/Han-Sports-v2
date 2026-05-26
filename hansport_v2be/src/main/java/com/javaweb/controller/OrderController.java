package com.javaweb.controller;

import com.javaweb.domain.Order;
<<<<<<< HEAD
import com.javaweb.domain.request.ReqOrderDTO;
import com.javaweb.domain.request.ReqUpdateOrderStatusDTO;
=======
import com.javaweb.domain.User;
import com.javaweb.domain.request.ReqOrderDTO;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
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
<<<<<<< HEAD
    public ResponseEntity<ResOrderDTO> placeOrder(@RequestBody @Valid ReqOrderDTO redOrderDTO) throws com.javaweb.util.error.IdInvalidException {
=======
    public ResponseEntity<ResOrderDTO> placeOrder(@RequestBody @Valid ReqOrderDTO redOrderDTO) {
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        ResOrderDTO order = this.orderService.placeOrder(email, redOrderDTO);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders")
<<<<<<< HEAD
    public ResponseEntity<ResOrderDTO> updateOrder(@RequestBody @Valid ReqUpdateOrderStatusDTO order) throws com.javaweb.util.error.IdInvalidException
    {
        return ResponseEntity.ok().body(this.orderService.updateOrderStatus(order));
=======
    public ResponseEntity<Void> updateOrder(@RequestBody Order order)
    {
        return ResponseEntity.ok().body(null);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }

    @GetMapping("/orders")
    public ResponseEntity<ResultPaginationDTO> getAllOrders(@Filter Specification<Order> spec,
                                                            Pageable pageable)
    {
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.fetchAllOrders(spec, pageable));
    }

<<<<<<< HEAD
    @GetMapping("/orders/my")
    public ResponseEntity<ResultPaginationDTO> getMyOrders(Pageable pageable) throws com.javaweb.util.error.IdInvalidException
    {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        return ResponseEntity.status(HttpStatus.OK).body(this.orderService.fetchMyOrders(email, pageable));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) throws com.javaweb.util.error.IdInvalidException {
=======
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id){
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        this.orderService.deleteOrder(email, id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
