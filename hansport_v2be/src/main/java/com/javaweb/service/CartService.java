package com.javaweb.service;

import com.javaweb.domain.*;
import com.javaweb.domain.request.ReqAddProductToCartDTO;
import com.javaweb.domain.response.cart.ResCartDTO;
import com.javaweb.domain.response.cartdetail.ResCartDetailDTO;
import com.javaweb.repository.CartDetailRepository;
import com.javaweb.repository.CartRepository;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        //cart exist
        if(cart==null){
            // tạo mới cart
            Cart otherCart = new Cart();
            otherCart.setUser(currentUSer);
            otherCart.setSum(0);

            cart = this.cartRepository.save(otherCart);
        }



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
    }



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
        }
    }

    public boolean isCartDetailExist(long cartDetailId){
        return this.cartDetailRepository.existsById(cartDetailId);

    }

    public ResCartDTO convertToResCartDTO (Cart cart){
        ResCartDTO resCartDTO = new ResCartDTO();
        List<CartDetail> cartDetails = cart.getCartDetails();

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

        resCartDetailDTO.setProduct(productCartDetail);
        resCartDetailDTO.setCreatedAt(cd.getCreatedAt());
        resCartDetailDTO.setUpdatedAt(cd.getUpdatedAt());

        return resCartDetailDTO;

    }
}
