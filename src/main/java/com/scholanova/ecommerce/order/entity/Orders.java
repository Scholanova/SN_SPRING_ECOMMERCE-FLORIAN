package com.scholanova.ecommerce.order.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scholanova.ecommerce.cart.entity.Cart;
import com.scholanova.ecommerce.order.exception.NotAllowedException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Clock;
import java.time.LocalDateTime;

@Entity(name="orders")
public class Orders {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column
    private String number;

    @Column
    private Date issueDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="cart_id", referencedColumnName = "id")
    private Cart cart;

    public Orders() {}

    public void createOrder(){
        this.cart = new Cart();
        this.status = OrderStatus.CREATED;
    }

    public void checkout(Clock clock) throws NotAllowedException {
        if ( this.status.equals(OrderStatus.CLOSED) ) {
            throw new NotAllowedException("Cannot checkout closed order");
        }

        if ( this.cart != null && this.cart.getCartItems().stream().map( item -> item.getQuantity()).mapToInt(Integer::intValue).sum() == 0) {
            throw new IllegalArgumentException("Cannot checkout order when cart got 0 items.");
        }

        this.issueDate = Date.valueOf(LocalDateTime.now(clock).toLocalDate());
        this.status = OrderStatus.PENDING;
    }

    public BigDecimal getDiscount(){
        BigDecimal total = this.cart.getTotalPrice();
        return total.compareTo(new BigDecimal(100)) == -1 ? new BigDecimal(0) : (total.multiply(new BigDecimal(5))).divideToIntegralValue(new BigDecimal(100));
    }

    public BigDecimal getOrderPrice(){
        return this.cart.getTotalPrice().subtract(this.getDiscount());
    }

    public void close(){
        this.status = OrderStatus.CLOSED;
    }


    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {return number;}

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getIssueDate() {return issueDate;}

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public OrderStatus getStatus() {return status;}

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Cart getCart() {return cart;}

    public void setCart(Cart cart) throws NotAllowedException {
        if ( this.status.equals(OrderStatus.CLOSED) ) {
            throw new NotAllowedException("Cannot change cart for a closed order.");
        }
        this.cart = cart;
    }
}
