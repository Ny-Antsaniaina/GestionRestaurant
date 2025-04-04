package org.exemple;

import org.example.DAO.CrudOperation.DishCrudOperation;
import org.example.DAO.CrudOperation.OrderCrudOperation;
import org.example.Entity.*;
import org.example.db.ConnectionDataBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private OrderCrudOperation orderCrudOperation = new OrderCrudOperation();
    private DishCrudOperation dishCrudOperation = new DishCrudOperation();

    @Test
    public void testCreateOrder() {

        Order order = new Order();
        Dish hotDog = dishCrudOperation.findById("1");
        assertNotNull(hotDog, "Dish should exist");
        order.addDishOrder(hotDog, 2);


        List<Order> savedOrders = orderCrudOperation.saveAll(List.of(order));


        assertEquals(1, savedOrders.size());
        Order savedOrder = savedOrders.get(0);
        assertEquals(2, savedOrder.getDishOrders().size());
    }
    @Test
    public void testConfirmOrder() {

        Order order = new Order();
        Dish hotDog = dishCrudOperation.findById("1");
        order.addDishOrder(hotDog, 1);
        orderCrudOperation.saveAll(List.of(order));


        orderCrudOperation.confirmOrder(order.getId());
        Order confirmedOrder = orderCrudOperation.findById(order.getId());


        assertEquals(StatusEnum.CONFIRMED, confirmedOrder.getStatus());
        assertEquals(StatusEnum.CONFIRMED, confirmedOrder.getDishOrders().get(0).getStatus());
    }

    @Test
    public void testUpdateOrderStatus() {

        Order order = new Order();
        Dish hotDog = dishCrudOperation.findById("1");
        order.addDishOrder(hotDog, 1);
        orderCrudOperation.saveAll(List.of(order));
        orderCrudOperation.confirmOrder(order.getId());


        orderCrudOperation.updateOrderStatus(order.getId(), StatusEnum.IN_PREPARATION);
        Order updatedOrder = orderCrudOperation.findById(order.getId());


        assertEquals(StatusEnum.IN_PREPARATION, updatedOrder.getStatus());
    }

    @Test
    public void testFindOrderById() {

        Order order = new Order();
        Dish hotDog = dishCrudOperation.findById("1");
        order.addDishOrder(hotDog, 1);
        orderCrudOperation.saveAll(List.of(order));

        Order foundOrder = orderCrudOperation.findById(order.getId());

        assertNotNull(foundOrder);
        assertEquals(order.getId(), foundOrder.getId());
        assertEquals(1, foundOrder.getDishOrders().size());
    }

    @Test
    public void testFindAllOrders() {

        Order order1 = new Order();
        Dish hotDog = dishCrudOperation.findById("1");
        order1.addDishOrder(hotDog, 1);
        orderCrudOperation.saveAll(List.of(order1));

        Order order2 = new Order();
        order2.addDishOrder(hotDog, 2);
        orderCrudOperation.saveAll(List.of(order2));


        List<Order> orders = orderCrudOperation.findAll(1, 2);


        assertTrue(orders.size() >= 2);
    }
}