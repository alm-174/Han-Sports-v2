package com.javaweb.service;

import com.javaweb.domain.*;
import com.javaweb.domain.request.ReqOrderDTO;
<<<<<<< HEAD
import com.javaweb.domain.request.ReqUpdateOrderStatusDTO;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.email.OrderEmailDTO;
import com.javaweb.domain.response.order.ResOrderDTO;
import com.javaweb.domain.response.orderdetail.ResOrderDetailDTO;
import com.javaweb.repository.*;
import com.javaweb.util.error.IdInvalidException;
=======
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.cartdetail.ResCartDetailDTO;
import com.javaweb.domain.response.order.ResOrderDTO;
import com.javaweb.domain.response.orderdetail.ResOrderDetailDTO;
import com.javaweb.domain.response.user.ResUserDTO;
import com.javaweb.repository.*;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
=======

import java.util.ArrayList;
import java.util.List;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderDetailRepository orderDetailRepository;
<<<<<<< HEAD
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository,
                        CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository,
                        ProductRepository productRepository, EmailService emailService) {
=======

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository, CartDetailRepository cartDetailRepository, OrderDetailRepository orderDetailRepository) {
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.orderDetailRepository = orderDetailRepository;
<<<<<<< HEAD
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    @Transactional
    public ResOrderDTO placeOrder(String email, ReqOrderDTO reqOrder) throws IdInvalidException {
        //get cart
        User currentUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Người dùng không tồn tại"));
        Cart cart = this.cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new IdInvalidException("Giỏ hàng đang trống"));

        List<CartDetail> allCartDetails = cart.getCartDetails();
        if (allCartDetails == null || allCartDetails.isEmpty()) {
            throw new IdInvalidException("Giỏ hàng đang trống");
        }

        // Lọc ra các sản phẩm được chọn để thanh toán
        List<CartDetail> orderItems = allCartDetails.stream()
                .filter(cd -> reqOrder.getCartDetailIds().contains(cd.getId()))
                .collect(Collectors.toList());

        if (orderItems.isEmpty()) {
            throw new IdInvalidException("Không có sản phẩm nào được chọn để thanh toán");
        }

        double sum = 0;
        for (CartDetail cd : orderItems) {
            Product product = cd.getProduct();
            if (product.getQuantity() < cd.getQuantity()) {
                throw new IdInvalidException("Sản phẩm " + product.getName() + " không đủ tồn kho");
            }
            sum += cd.getPrice() * cd.getQuantity();
        }

        Order order = new Order();
        order.setUser(currentUser);
        order.setReceiverName(reqOrder.getReceiverName());
        order.setReceiverAddress(reqOrder.getReceiverAddress());
        order.setReceiverPhone(reqOrder.getReceiverPhone());
        order.setStatus("PENDING");
        order.setTotalPrice(sum);
        order = this.orderRepository.save(order);

        List<OrderDetail> savedOrderDetails = new ArrayList<>();
        for (CartDetail cartDetail : orderItems) {
            Product product = cartDetail.getProduct();
            product.setQuantity(product.getQuantity() - cartDetail.getQuantity());
            product.setSold(product.getSold() + cartDetail.getQuantity());
            this.productRepository.save(product);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setPrice(cartDetail.getPrice());
            orderDetail.setQuantity(cartDetail.getQuantity());
            savedOrderDetails.add(this.orderDetailRepository.save(orderDetail));
            
            // Xóa khỏi danh sách để orphanRemoval tự động xóa trong DB
            allCartDetails.remove(cartDetail);
        }
        order.setOrderDetails(savedOrderDetails);

        // Cập nhật lại số lượng loại sản phẩm trong giỏ hàng (sum)
        if (!allCartDetails.isEmpty()) {
            cart.setSum(allCartDetails.size());
            this.cartRepository.save(cart);
        } else {
            this.cartRepository.deleteById(cart.getId());
        }
        return this.convertToResOrderDTO(order);
    }

    @Transactional(readOnly = true)
=======
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

>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
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

<<<<<<< HEAD
    @Transactional(readOnly = true)
    public ResultPaginationDTO fetchMyOrders(String email, Pageable pageable) throws IdInvalidException {
        User currentUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Người dùng không tồn tại"));
        Page<Order> orders = this.orderRepository.findByUser(currentUser, pageable);
        return this.convertToPaginationDTO(orders, pageable);
    }

    @Transactional
    public ResOrderDTO updateOrderStatus(ReqUpdateOrderStatusDTO req) throws IdInvalidException {
        Order order = this.orderRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Đơn hàng không tồn tại"));
        String status = req.getStatus().trim().toUpperCase();
        List<String> allowedStatus = Arrays.asList("PENDING", "PROCESSING", "SHIPPING", "COMPLETED", "CANCELLED");
        if (!allowedStatus.contains(status)) {
            throw new IdInvalidException("Trạng thái đơn hàng không hợp lệ");
        }
        order.setStatus(status);
        return this.convertToResOrderDTO(this.orderRepository.save(order));
    }

    @Transactional
        public void deleteOrder(String email, long id) throws IdInvalidException {
            User currentUser = this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new IdInvalidException("Người dùng không tồn tại"));

            Order order;
            boolean isAdmin = currentUser.getRole() != null && "ADMIN".equalsIgnoreCase(currentUser.getRole().getName());
            if (isAdmin) {
                order = this.orderRepository.findById(id)
                        .orElseThrow(() -> new IdInvalidException("Đơn hàng không tồn tại"));
            } else {
                order = this.orderRepository.findByUserAndId(currentUser, id)
                        .orElseThrow(() -> new IdInvalidException("Đơn hàng không tồn tại"));
            }
            this.orderRepository.delete(order);
        }

    private ResultPaginationDTO convertToPaginationDTO(Page<Order> orders, Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPagesize(pageable.getPageSize());
        meta.setPages(orders.getTotalPages());
        meta.setTotal(orders.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(orders.getContent()
                .stream().map(this::convertToResOrderDTO)
                .collect(Collectors.toList()));

        return resultPaginationDTO;
    }

    public void sendOrderEmail(long id){
        Optional<Order> order = this.orderRepository.findById(id);
        if(order.isPresent()){
            Order currentOrder =  order.get();
            OrderEmailDTO orderEmailDTO = new OrderEmailDTO();
            orderEmailDTO.setCustomerName(currentOrder.getReceiverName());
            orderEmailDTO.setAddress(currentOrder.getReceiverAddress());
            orderEmailDTO.setPhone(currentOrder.getReceiverPhone());
            orderEmailDTO.setTotalPrice(currentOrder.getTotalPrice());

            List<OrderEmailDTO.OrderItemEmailDTO> orderItemEmailDTOList = new ArrayList<>();
            List<OrderDetail> orderDetails = currentOrder.getOrderDetails();
            if(orderDetails != null && orderDetails.size() > 0 ){
                for (OrderDetail orderDetail : orderDetails) {
                    OrderEmailDTO.OrderItemEmailDTO orderItemEmailDTO = new OrderEmailDTO.OrderItemEmailDTO();
                    orderItemEmailDTO.setProductName(orderDetail.getProduct().getName());
                    orderItemEmailDTO.setQuantity(orderDetail.getQuantity());
                    orderItemEmailDTO.setPrice(orderDetail.getPrice());
                    orderItemEmailDTOList.add(orderItemEmailDTO);
                }
            }

            orderEmailDTO.setItems(orderItemEmailDTOList);

            this.emailService.sendEmailFromTemplateSync(
                    currentOrder.getUser().getEmail(),
                    "Xác nhận đơn hàng",
                    "order",
                    orderEmailDTO
            );

        }

=======
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
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
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

<<<<<<< HEAD
        List<OrderDetail> orderDetails = order.getOrderDetails() == null ? new ArrayList<>() : order.getOrderDetails();
=======
        List<OrderDetail> orderDetails = order.getOrderDetails();
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

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
<<<<<<< HEAD
        
        List<com.javaweb.domain.ProductImage> images = orderDetail.getProduct().getImages();
        if (images != null && !images.isEmpty()) {
            productCartDetail.setImage(images.get(0).getImageUrl());
        } else {
            productCartDetail.setImage(null);
        }
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

        resOrderDetailDTO.setProduct(productCartDetail);

        resOrderDetailDTO.setCreatedAt(orderDetail.getCreatedAt());
        resOrderDetailDTO.setUpdatedAt(orderDetail.getUpdatedAt());
        return resOrderDetailDTO;
    }


}
