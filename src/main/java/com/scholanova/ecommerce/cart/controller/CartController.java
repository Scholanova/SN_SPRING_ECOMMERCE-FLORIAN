package com.scholanova.ecommerce.cart.controller;

import com.scholanova.ecommerce.cart.entity.Cart;
import com.scholanova.ecommerce.cart.exception.CartException;
import com.scholanova.ecommerce.cart.repository.CartRepository;
import com.scholanova.ecommerce.cart.service.CartService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@EnableSpringDataWebSupport
@RequestMapping("/carts")
public class CartController {

    private CartRepository cartRepository;
    private CartService cartService;

    public CartController(CartRepository cartRepository, CartService cartService) {
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> getAllCarts(Pageable pageable) {
        return cartRepository.findAll(pageable).get().collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable("id") Cart cart) { return cart;}

    @PostMapping
    public Cart createCart() { return cartRepository.save(new Cart()); }

    @PostMapping("/{id}/items")
    public Cart addProductToCart(@PathVariable("id)") Cart cart, @RequestBody ItemWithQuantity payload) throws CartException {
        return cartService.addProductToCart(cart, payload.productId, payload.quantity);
    }

    @PutMapping("/{cartid}/items/{productId}/quantity")
    public Cart changeProductQuantity(@PathVariable("cartId") Cart cart, @PathVariable("productId") Long productId, @RequestBody int quantity) throws CartException {
        return cartService.changeProductQuantity(cart, productId, quantity);
    }

    public class ItemWithQuantity {
        Long productId;
        int quantity;
    }

}

