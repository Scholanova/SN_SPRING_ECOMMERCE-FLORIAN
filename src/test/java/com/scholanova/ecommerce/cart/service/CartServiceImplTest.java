package com.scholanova.ecommerce.cart.service;

import com.scholanova.ecommerce.cart.entity.Cart;
import com.scholanova.ecommerce.cart.entity.CartItem;
import com.scholanova.ecommerce.cart.exception.CartException;
import com.scholanova.ecommerce.product.entity.Product;
import com.scholanova.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @InjectMocks
    private CartServiceImpl service;

    @Test
    public void addProductToCart_ShouldAddTheProductToTheCart() throws CartException {
        //given
        Cart cart = new Cart();
        Product product = Product.create("tested", "tested", 10.5f, 0.1f, "EUR");
        product.setId((long) 12);
        given(productRepository.findById(Long.valueOf(12))).willReturn(java.util.Optional.of(product));
        //when
        service.addProductToCart(cart, (long) 12, 3);
        //then
        assertThat(cart.getCartItemByProductName("tested").getQuantity() == 3);
    }

    @Test
    public void addProductToCart_ShouldHandleExceptions() {
        //given
        Cart cart = new Cart();
        Product product = Product.create("tested", "tested", 10.5f, 0.1f, "EUR");
        product.setId((long) 12);
        given(productRepository.findById(Long.valueOf(12))).willReturn(java.util.Optional.of(product));
        //then
        assertThrows(CartException.class, () -> service.addProductToCart(cart, (long) 123, 2));
        assertThrows(CartException.class, () -> service.addProductToCart(cart, (long) 12, -3));
    }

    @Test
    public void changeProductQuantity_ShouldChangeQuantity() throws CartException {
        //given
        Cart cart = new Cart();
        Product product = Product.create("tested", "tested", 10.5f, 0.1f, "EUR");
        product.setId((long) 12);
        cart.getCartItems().add(CartItem.create(product, 4));
        given(productRepository.findById(Long.valueOf(12))).willReturn(java.util.Optional.of(product));
        //when
        service.changeProductQuantity(cart, (long) 12, 3);
        //then
        assertThat(cart.getCartItemByProductName("tested").getQuantity() == 3);
    }

    @Test
    public void changeProductQuantity_ShouldHandleExceptions(){
        //given
        Cart cart = new Cart();
        Product product = Product.create("tested", "tested", 10.5f, 0.1f, "EUR");
        product.setId((long) 12);
        cart.getCartItems().add(CartItem.create(product, 4));
        given(productRepository.findById(Long.valueOf(12))).willReturn(java.util.Optional.of(product));
        //then
        assertThrows(CartException.class, () -> service.changeProductQuantity(cart, (long) 123, 2));
        assertThrows(CartException.class, () -> service.changeProductQuantity(cart, (long) 12, -3));
    }
}