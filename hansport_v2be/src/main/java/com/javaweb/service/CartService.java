package com.javaweb.service;

import com.javaweb.domain.Cart;
import com.javaweb.domain.CartDetail;
import com.javaweb.domain.Product;
import com.javaweb.domain.User;
import com.javaweb.domain.request.ReqAddProductToCartDTO;
import com.javaweb.domain.response.cart.ResCartDTO;
import com.javaweb.domain.response.cartdetail.ResCartDetailDTO;
import com.javaweb.repository.CartDetailRepository;
import com.javaweb.repository.CartRepository;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.UserRepository;
<<<<<<< HEAD
import com.javaweb.util.error.IdInvalidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
=======
import org.springframework.stereotype.Service;

import java.util.ArrayList;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartDetailRepository cartDetailRepository, CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartDetailRepository = cartDetailRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

<<<<<<< HEAD
    public ResCartDTO getCart(String email) throws IdInvalidException {
        User currentUser = this.getUserOrThrow(email);
        Optional<Cart> cart = this.cartRepository.findByUser(currentUser);

        return cart.map(this::convertToResCartDTO).orElseGet(() -> this.convertEmptyCartDTO(currentUser));
    }


    @Transactional
    public ResCartDTO addProductToCart(String email, ReqAddProductToCartDTO  reqAddProductToCartDTO) throws IdInvalidException {
        User currentUser = this.getUserOrThrow(email);
        Cart cart = this.cartRepository.findByUser(currentUser).orElse(null);
=======
    public ResCartDTO getCart(String email){
        User currentUSer = this.userRepository.findByEmail(email).isPresent() ?
                this.userRepository.findByEmail(email).get() : null;
        Cart cart = this.cartRepository.findByUser(currentUSer).isPresent() ?
                this.cartRepository.findByUser(currentUSer).get() : null;

        return this.convertToResCartDTO(cart);
    }


    public ResCartDTO addProductToCart(String email, ReqAddProductToCartDTO  reqAddProductToCartDTO){
        User currentUSer = this.userRepository.findByEmail(email).isPresent() ?
                this.userRepository.findByEmail(email).get() : null;
        Cart cart = this.cartRepository.findByUser(currentUSer).isPresent() ?
                this.cartRepository.findByUser(currentUSer).get() : null;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

        //cart exist
        if(cart==null){
            // tạo mới cart
            Cart otherCart = new Cart();
<<<<<<< HEAD
            otherCart.setUser(currentUser);
=======
            otherCart.setUser(currentUSer);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
            otherCart.setSum(0);

            cart = this.cartRepository.save(otherCart);
        }



<<<<<<< HEAD
        Product realProduct = this.productRepository.findById(reqAddProductToCartDTO.getProductId())
                .orElseThrow(() -> new IdInvalidException("Sản phẩm không tồn tại"));

        long requestedQuantity = reqAddProductToCartDTO.getQuantity();

        // check sản phẩm đã từng được thêm vào giỏ hàng trước đây chưa ?
        CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
        long currentQuantity = oldDetail == null ? 0 : oldDetail.getQuantity();
        if (currentQuantity + requestedQuantity > realProduct.getQuantity()) {
            throw new IdInvalidException("Số lượng vượt quá tồn kho");
        }

        if (oldDetail == null) {
            CartDetail cd = new CartDetail();
            cd.setCart(cart);
            cd.setProduct(realProduct);
            cd.setPrice(realProduct.getPrice());
            cd.setQuantity(requestedQuantity);
            this.cartDetailRepository.save(cd);

            // update cart (sum);
            int s = cart.getSum() + 1;
            cart.setSum(s);
            cart = this.cartRepository.save(cart);

        } else {
            oldDetail.setQuantity(oldDetail.getQuantity() + requestedQuantity);
            this.cartDetailRepository.save(oldDetail);
        }

        Cart latestCart = this.cartRepository.findById(cart.getId()).orElse(cart);
        return this.convertToResCartDTO(latestCart);
=======
        Optional<Product>  optionalProduct = this.productRepository.findById(reqAddProductToCartDTO.getProductId());

        if (optionalProduct.isPresent()) {
            Product realProduct = optionalProduct.get();

            // check sản phẩm đã từng được thêm vào giỏ hàng trước đây chưa ?
            CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
            //
            if (oldDetail == null) {
                CartDetail cd = new CartDetail();
                cd.setCart(cart);
                cd.setProduct(realProduct);
                cd.setPrice(realProduct.getPrice());
                cd.setQuantity(reqAddProductToCartDTO.getQuantity());
                this.cartDetailRepository.save(cd);

                // update cart (sum);
                int s = cart.getSum() + 1;
                cart.setSum(s);
                cart = this.cartRepository.save(cart);
                return this.convertToResCartDTO(cart);

            } else {
                oldDetail.setQuantity(oldDetail.getQuantity() + reqAddProductToCartDTO.getQuantity());
                this.cartDetailRepository.save(oldDetail);
            }

        }


        return this.convertToResCartDTO(cart);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }



<<<<<<< HEAD
    @Transactional
    public void deleteCartDetail(String email, long cartDetailId) throws IdInvalidException {
        User currentUser = this.getUserOrThrow(email);
        CartDetail cartDetail = this.cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new IdInvalidException("Cart Detail không tồn tại"));

        Cart currentCart = cartDetail.getCart();
        if (currentCart == null || currentCart.getUser() == null || currentCart.getUser().getId() != currentUser.getId()) {
            throw new IdInvalidException("Bạn không có quyền xóa sản phẩm này khỏi giỏ hàng");
        }

        this.cartDetailRepository.deleteById(cartDetailId);

        if (currentCart.getSum() > 1) {
            int s = currentCart.getSum() - 1;
            currentCart.setSum(s);
            this.cartRepository.save(currentCart);
        } else {
            this.cartRepository.deleteById(currentCart.getId());
=======
    public void deleteCartDetail(long cartDetailId){
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();

            Cart currentCart = cartDetail.getCart();
            // delete cart-detail
            this.cartDetailRepository.deleteById(cartDetailId);

            // update cart
            if (currentCart.getSum() > 1) {
                // update current cart
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                this.cartRepository.save(currentCart);
            } else {
                // delete cart (sum = 1)
                this.cartRepository.deleteById(currentCart.getId());
            }
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        }
    }

    public boolean isCartDetailExist(long cartDetailId){
        return this.cartDetailRepository.existsById(cartDetailId);

    }

    public ResCartDTO convertToResCartDTO (Cart cart){
        ResCartDTO resCartDTO = new ResCartDTO();
<<<<<<< HEAD
        List<CartDetail> cartDetails = cart.getCartDetails() == null ? Collections.emptyList() : cart.getCartDetails();
=======
        List<CartDetail> cartDetails = cart.getCartDetails();
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

        List<ResCartDetailDTO> resCartDetailDTOS = new ArrayList<>();
        for(CartDetail cd : cartDetails)
        {
            resCartDetailDTOS.add(this.converToResCartDetailDTO(cd));
        }

        ResCartDTO.UserCart userCart = new ResCartDTO.UserCart();
        userCart.setId(cart.getUser().getId());
        userCart.setName(cart.getUser().getFullName());
        userCart.setEmail(cart.getUser().getEmail());

        resCartDTO.setId(cart.getId());
        resCartDTO.setSum(cart.getSum());
        resCartDTO.setCartDetails(resCartDetailDTOS);
        resCartDTO.setUser(userCart);
        resCartDTO.setCreatedAt(cart.getCreatedAt());
        resCartDTO.setUpdatedAt(cart.getUpdatedAt());

        return  resCartDTO;
    }

    public ResCartDetailDTO converToResCartDetailDTO(CartDetail cd){
        ResCartDetailDTO resCartDetailDTO = new ResCartDetailDTO();
        resCartDetailDTO.setId(cd.getId());
        resCartDetailDTO.setQuantity(cd.getQuantity());
        resCartDetailDTO.setPrice(cd.getPrice());

        //product cart
        ResCartDetailDTO.ProductCartDetail productCartDetail = new ResCartDetailDTO.ProductCartDetail();
        productCartDetail.setId(cd.getProduct().getId());
        productCartDetail.setName(cd.getProduct().getName());
<<<<<<< HEAD
        productCartDetail.setPrice(cd.getProduct().getPrice());
        
        List<com.javaweb.domain.ProductImage> images = cd.getProduct().getImages();
        if (images != null && !images.isEmpty()) {
            productCartDetail.setImage(images.get(0).getImageUrl());
        } else {
            productCartDetail.setImage(null);
        }
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

        resCartDetailDTO.setProduct(productCartDetail);
        resCartDetailDTO.setCreatedAt(cd.getCreatedAt());
        resCartDetailDTO.setUpdatedAt(cd.getUpdatedAt());

        return resCartDetailDTO;

    }
<<<<<<< HEAD

    private User getUserOrThrow(String email) throws IdInvalidException {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Người dùng không tồn tại"));
    }

    private ResCartDTO convertEmptyCartDTO(User user) {
        ResCartDTO resCartDTO = new ResCartDTO();
        ResCartDTO.UserCart userCart = new ResCartDTO.UserCart();
        userCart.setId(user.getId());
        userCart.setName(user.getFullName());
        userCart.setEmail(user.getEmail());
        resCartDTO.setId(0);
        resCartDTO.setSum(0);
        resCartDTO.setUser(userCart);
        resCartDTO.setCartDetails(new ArrayList<>());
        return resCartDTO;
    }
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
}
