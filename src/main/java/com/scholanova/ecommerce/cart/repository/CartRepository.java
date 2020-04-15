package com.scholanova.ecommerce.cart.repository;

import com.scholanova.ecommerce.cart.entity.Cart;
import com.scholanova.ecommerce.product.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CartRepository extends PagingAndSortingRepository<Cart, Long> {
    void addProductToCart(Cart cart, Product product, int quantity);
}
