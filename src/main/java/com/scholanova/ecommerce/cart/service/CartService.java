package com.scholanova.ecommerce.cart.service;

import com.scholanova.ecommerce.cart.entity.Cart;
import com.scholanova.ecommerce.cart.exception.CartException;

public interface CartService {

    public Cart addProductToCart(Cart cart, Long productId, int quantity) throws CartException;

    public Cart changeProductQuantity(Cart cart, Long productId, int quantity) throws CartException;
}
