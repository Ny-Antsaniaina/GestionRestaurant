package org.exemple;

import org.example.DAO.CrudOperation.DishCrudOperation;
import org.example.DAO.CrudOperation.OrderCrudOperation;
import org.example.Entity.DishOrder;
import org.example.Entity.Order;
import org.example.Entity.StatusEnum;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {
    OrderCrudOperation subject = new OrderCrudOperation();

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
        Order order = new Order("9");
        order.addOrderDish(List.of(
                new DishOrder("8", dishDAO.findById("1"), 2),
                new DishOrder("9", dishDAO.findById("1"), 1)
        ));
    }
}
