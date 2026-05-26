package com.javaweb.controller;

import com.javaweb.domain.Cart;
import com.javaweb.domain.request.ReqAddProductToCartDTO;
import com.javaweb.domain.response.cart.ResCartDTO;
import com.javaweb.service.CartService;
import com.javaweb.util.SecurityUtil;
import com.javaweb.util.annotation.ApiMessage;
import com.javaweb.util.error.IdInvalidException;
<<<<<<< HEAD
import jakarta.validation.Valid;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/add")
    @ApiMessage("Add product to cart")
<<<<<<< HEAD
    public ResponseEntity<ResCartDTO> addToCart(@RequestBody @Valid ReqAddProductToCartDTO reqAddProductToCartDTO) throws IdInvalidException
=======
    public ResponseEntity<ResCartDTO> addToCart(@RequestBody ReqAddProductToCartDTO reqAddProductToCartDTO)
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        ResCartDTO cart = this.cartService.addProductToCart(email, reqAddProductToCartDTO);
        return ResponseEntity.ok().body(cart);
    }

    @GetMapping("/carts")
    @ApiMessage("show cart")
<<<<<<< HEAD
    public ResponseEntity<ResCartDTO> getCart() throws IdInvalidException
=======
    public ResponseEntity<ResCartDTO> getCart()
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        ResCartDTO cart = this.cartService.getCart(email);

        return ResponseEntity.ok().body(cart);
    }

    @DeleteMapping("/carts/{id}")
    @ApiMessage("Delete cart detail")
    public ResponseEntity<Void> deleteCartDetail(@PathVariable long id) throws IdInvalidException {

        if(!this.cartService.isCartDetailExist(id)){
            throw new IdInvalidException("Cart Detail khng tồn tại");
        }

<<<<<<< HEAD
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        this.cartService.deleteCartDetail(email, id);
=======
        this.cartService.deleteCartDetail(id);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf


        return ResponseEntity.ok().body(null);
    }
}
