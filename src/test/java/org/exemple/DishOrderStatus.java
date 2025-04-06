package org.exemple;

import org.example.DAO.CrudOperation.DishOrderStatusHistoryCrudOperation;
import org.example.Entity.DishOrderStatusHistory;
import org.example.Entity.StatusEnum;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DishOrderStatus {
    DishOrderStatusHistoryCrudOperation subject = new DishOrderStatusHistoryCrudOperation();

    @Test
    public void get_all_dish_order_status_history() throws SQLException {
        List<DishOrderStatusHistory> expected = List.of(
                Order1()
        );

        List<DishOrderStatusHistory> actual = subject.findAll(1, 1);

        System.out.println(actual);
        System.out.println(expected);
        assertTrue(actual.equals(expected));
    }

    public DishOrderStatusHistory Order1(){
        return createDishOrderStatusHistory("2", StatusEnum.CREATED, LocalDateTime.of(2025, 02, 07, 10, 00, 00, 00));
    }

    public DishOrderStatusHistory createDishOrderStatusHistory(String id, StatusEnum orderStatus, LocalDateTime date) {
        return new DishOrderStatusHistory(id, orderStatus, date);
    }
}
