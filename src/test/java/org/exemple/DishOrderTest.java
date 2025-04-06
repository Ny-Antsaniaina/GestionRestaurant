package org.exemple;

import org.example.DAO.CrudOperation.DishCrudOperation;
import org.example.DAO.CrudOperation.OrderCrudOperation;
import org.example.Entity.*;
import org.example.db.ConnectionDataBase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishOrderTest {
    OrderCrudOperation subject = new OrderCrudOperation();
    ConnectionDataBase connectionDataBase = new ConnectionDataBase();

    @Test
    public void getTotalAmount(){
        Order order = subject.findById("1");
        order.setDishOrders(subject.getDishOrdersForOrder(order.getId()));

        Double actual = order.getTotalAmount();
        assertTrue(actual.equals(30000.0));
    }

    @Test
    public void getActualStatus(){
        Order order = subject.findById("3");
        order.setDishOrders(subject.getDishOrdersForOrder(order.getId()));

        StatusEnum actual = order.getActualStatus();

        System.out.println(actual);
    }

    @Test
    public void addOrderDish(){
        DishCrudOperation dishDAO = new DishCrudOperation();
        Order order = new Order("5");
        order.addOrderDish(List.of(
                new DishOrder("6", dishDAO.findById("1"), 2),
                new DishOrder("7", dishDAO.findById("1"), 1)
        ));
    }
}