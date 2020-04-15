package com.scholanova.ecommerce.order.entity;

import com.scholanova.ecommerce.cart.entity.Cart;
import com.scholanova.ecommerce.order.exception.NotAllowedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;


class OrdersTest {

    @Test
    public void checkout_ShouldSetTheDateAndTimeOfTodayInTheOrder() throws Exception {
        //given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Date date = Date.valueOf(LocalDateTime.now(clock).toLocalDate());
        Orders order = new Orders();
        //when
        order.checkout(clock);
        //then
        assertThat(order.getIssueDate()).isEqualTo(date);
    }

    @Test
    public void checkout_ShouldSetOrderStatusToPending() throws NotAllowedException {
        //given
        Orders order = new Orders();
        //when
        order.checkout(Clock.systemDefaultZone());
        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    public void checkout_ShouldThrowNotAllowedExceptionIfStatusIsClosed(){
        //given
        Orders order = new Orders();
        order.setStatus(OrderStatus.CLOSED);
        //then
        assertThrows(NotAllowedException.class, () -> order.checkout(Clock.systemDefaultZone()));
    }

    @Test
    public void checkout_ShouldThrowIllegalArgExceptionIfCartTotalItemsQuantityIsZERO() throws NotAllowedException {
        //given
        Orders order = new Orders();
        Cart cart = new Cart();
        order.setCart(cart);
        //then
        assertThrows(IllegalArgumentException.class, () -> order.checkout(Clock.systemDefaultZone()));
    }

    @Test
    public void setCart_ShouldThrowNotAllowedExceptionIfStatusIsClosed(){
        //given
        Orders order = new Orders();
        order.setStatus(OrderStatus.CLOSED);
        Cart cart = new Cart();
        //then
        assertThrows(NotAllowedException.class, () -> order.setCart(cart));
    }

    @Test
    public void createOrder_ShouldSetTheCartInTheOrder(){
        //given
        Orders order = new Orders();
        //when
        order.createOrder();
        //then
        assertThat(order.getCart()).isNotNull();
    }

    @Test
    public void createOrder_ShouldSetStatusToCreated(){
        //given
        Orders order = new Orders();
        //when
        order.createOrder();
        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    public void getDiscount_shouldReturnZEROIFCartTotalPriceIsLessThan100() throws NotAllowedException {
        //given
        Orders orders = new Orders();
        Cart cart = mock(Cart.class);
        orders.setCart(cart);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(99));
        //when
        BigDecimal res = orders.getDiscount();
        //then
        assertThat(res).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void getDiscount_shouldReturn5percentIfCartTotalPriceIsMoreOrEqual100() throws NotAllowedException {
        //given
        Orders orders = new Orders();
        Cart cart = mock(Cart.class);
        orders.setCart(cart);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(100));
        //when
        BigDecimal res = orders.getDiscount();
        //then
        assertThat(res).isEqualTo(new BigDecimal(5));
    }

    @Test
    public void getOrderPrice_shouldReturnTotalPriceWithDiscount() throws NotAllowedException {
        //given
        Orders orders = new Orders();
        Cart cart = mock(Cart.class);
        orders.setCart(cart);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(100));
        //when
        BigDecimal res = orders.getOrderPrice();
        //then
        assertThat(res).isEqualTo(new BigDecimal(95));
    }

    @Test
    public void close_ShouldSetStatusToClose(){
        //given
        Orders orders = new Orders();
        //when
        orders.close();
        //then
        assertThat(orders.getStatus()).isEqualTo(OrderStatus.CLOSED);
    }

}