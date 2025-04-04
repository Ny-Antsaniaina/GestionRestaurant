package org.example.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Order {
    private String id;
    private LocalDateTime createdAt;
    private StatusEnum status;
    private List<DishOrder> dishOrders = new ArrayList<>();

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.status = StatusEnum.CREATED;
    }

    public Order(String id, LocalDateTime createdAt, StatusEnum status, List<DishOrder> dishOrders) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.dishOrders = dishOrders != null ? dishOrders : new ArrayList<>();
    }

    public StatusEnum getActualStatus() {
        return status;
    }

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public double getTotalAmount() {
        return dishOrders.stream()
                .mapToDouble(dishOrder -> dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity())
                .sum();
    }

    public void addDishOrder(Dish dish, int quantity) {
        DishOrder dishOrder = new DishOrder();
        dishOrder.setId(UUID.randomUUID().toString());
        dishOrder.setDish(dish);
        dishOrder.setQuantity(quantity);
        dishOrder.setStatus(StatusEnum.CREATED);

        // Initialise l'historique des statuts
        DishOrderStatusHistory initialStatus = new DishOrderStatusHistory();
        initialStatus.setId(UUID.randomUUID().toString());
        initialStatus.setStatus(StatusEnum.CREATED);
        initialStatus.setChangedAt(LocalDateTime.now());

        dishOrder.setStatusHistory(new ArrayList<>());
        dishOrder.getStatusHistory().add(initialStatus);

        dishOrders.add(dishOrder);
    }
}