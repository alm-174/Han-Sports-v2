package com.javaweb.service;

import com.javaweb.repository.CartDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;

    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }


}
