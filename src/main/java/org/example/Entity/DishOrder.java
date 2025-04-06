package org.example.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class DishOrder {
    private String id;
    private Dish dish;
    private int quantity;
    private Order order;
    private List<DishOrderStatusHistory> statusHistory = new ArrayList<>();

    public DishOrder() {
        this.statusHistory.add(new DishOrderStatusHistory(UUID.randomUUID().toString(), this, StatusEnum.CREATED, LocalDateTime.now()));
    }

    public DishOrder(String id, Dish dish, int quantity,  List<DishOrderStatusHistory> statusHistory) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
        this.statusHistory = statusHistory != null ? statusHistory : new ArrayList<>();
    }

    public DishOrder(String id, Dish byId, int quantity) {
        this.id = id;
        this.dish = byId;
        this.quantity = quantity;
        this.statusHistory = new ArrayList<>();
    }


    public DishOrderStatusHistory getActualStatus() {
        return statusHistory.stream()
                .max(Comparator.comparing(DishOrderStatusHistory::getChangedAt))
                .orElse(null);
    }

}