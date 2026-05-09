package com.javaweb.service;

import com.javaweb.domain.*;
import com.javaweb.domain.request.ReqOrderDTO;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.cartdetail.ResCartDetailDTO;
import com.javaweb.domain.response.order.ResOrderDTO;
import com.javaweb.domain.response.orderdetail.ResOrderDetailDTO;
import com.javaweb.domain.response.user.ResUserDTO;
import com.javaweb.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public ResOrderDTO placeOrder(String email, ReqOrderDTO reqOrder) {
        //get cart
        User currentUSer = this.userRepository.findByEmail(email).isPresent() ?
                this.userRepository.findByEmail(email).get() : null;
        Cart cart = this.cartRepository.findByUser(currentUSer).isPresent() ?
                this.cartRepository.findByUser(currentUSer).get() : null;
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();

            if (cartDetails != null) {
                // create order
                Order order = new Order();
                order.setUser(currentUSer);
                order.setReceiverName(reqOrder.getReceiverName());
                order.setReceiverAddress(reqOrder.getReceiverAddress());
                order.setReceiverPhone(reqOrder.getReceiverPhone());
                order.setStatus("PENDING");

                double sum = 0;
                for (CartDetail cd : cartDetails) {
                    sum += cd.getPrice();
                }
                order.setTotalPrice(sum);
                order = this.orderRepository.save(order);

                // create orderDetail

                for (CartDetail cartDetail : cartDetails) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cartDetail.getProduct());
                    orderDetail.setPrice(cartDetail.getPrice());
                    orderDetail.setQuantity(cartDetail.getQuantity());
                    this.orderDetailRepository.save(orderDetail);
                }
                //delete cart
                for (CartDetail cartDetail : cartDetails) {
                    this.cartDetailRepository.deleteById(cartDetail.getId());
                }
                this.cartRepository.deleteById(cart.getId());
                return this.convertToResOrderDTO(order);
            }

        }
        return null;
    }

    public ResultPaginationDTO fetchAllOrders(Specification<Order> spec, Pageable pageable)
    {
        Page<Order> orders = this.orderRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPagesize(pageable.getPageSize());
        meta.setPages(orders.getTotalPages());
        meta.setTotal(orders.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        List<ResOrderDTO> listOrder = orders.getContent().
                stream().map(item -> this.convertToResOrderDTO(item))
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(listOrder);

        return resultPaginationDTO;
    }

    public void deleteOrder(String email, long id){
        User currentUSer = this.userRepository.findByEmail(email).isPresent() ?
                this.userRepository.findByEmail(email).get() : null;

        Order order = this.orderRepository.findByUserAndId(currentUSer, id).isPresent() ?
                this.orderRepository.findByUserAndId(currentUSer, id).get() : null;

        if(order != null){
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
            this.orderRepository.delete(order);
        }
    }

    public ResOrderDTO convertToResOrderDTO(Order order) {
        ResOrderDTO resOrderDTO = new ResOrderDTO();
        resOrderDTO.setId(order.getId());
        resOrderDTO.setReceiverName(order.getReceiverName());
        resOrderDTO.setReceiverAddress(order.getReceiverAddress());
        resOrderDTO.setReceiverPhone(order.getReceiverPhone());
        resOrderDTO.setTotalPrice(order.getTotalPrice());
        resOrderDTO.setStatus(order.getStatus());

        ResOrderDTO.UserOrder userOrder = new ResOrderDTO.UserOrder();
        userOrder.setId(order.getUser().getId());
        userOrder.setEmail(order.getUser().getEmail());
        userOrder.setName(order.getReceiverName());

        resOrderDTO.setUser(userOrder);

        List<OrderDetail> orderDetails = order.getOrderDetails();

        List<ResOrderDetailDTO> resOrderDetailDTOs = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            resOrderDetailDTOs.add(this.convertToResOrderDetailDTO(orderDetail));
        }

        resOrderDTO.setOrderDetails(resOrderDetailDTOs);

        resOrderDTO.setCreatedAt(order.getCreatedAt());
        resOrderDTO.setUpdatedAt(order.getUpdatedAt());
        return resOrderDTO;
    }

    public ResOrderDetailDTO convertToResOrderDetailDTO(OrderDetail orderDetail) {
        ResOrderDetailDTO resOrderDetailDTO = new ResOrderDetailDTO();
        resOrderDetailDTO.setId(orderDetail.getId());
        resOrderDetailDTO.setQuantity(orderDetail.getQuantity());
        resOrderDetailDTO.setPrice(orderDetail.getPrice());

        //product order
        ResOrderDetailDTO.ProductOrderDetail productCartDetail = new ResOrderDetailDTO.ProductOrderDetail();
        productCartDetail.setId(orderDetail.getProduct().getId());
        productCartDetail.setName(orderDetail.getProduct().getName());

        resOrderDetailDTO.setProduct(productCartDetail);

        resOrderDetailDTO.setCreatedAt(orderDetail.getCreatedAt());
        resOrderDetailDTO.setUpdatedAt(orderDetail.getUpdatedAt());
        return resOrderDetailDTO;
    }


}
