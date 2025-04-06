package org.example.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.DAO.CrudOperation.DishOrderCrudOperation;
import org.example.DAO.CrudOperation.OrderCrudOperation;

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
    public Order(String id) {
        this.id = id;
        this.status = StatusEnum.CREATED;
        this.createdAt = LocalDateTime.now();
        this.dishOrders = new ArrayList<>();
    }

    public Order(String id, List<DishOrder> dishOrders) {
        this.id = id;
        this.status = StatusEnum.CREATED;
        this.createdAt = LocalDateTime.now();
        this.dishOrders = dishOrders;
    }

    public Order() {

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

    public Order addOrderDish(List<DishOrder> dishOrders) {
        OrderCrudOperation orderDAO = new OrderCrudOperation();
        DishOrderCrudOperation dishOrderDAO = new DishOrderCrudOperation();

        orderDAO.addOrder(this);
        dishOrders.forEach(d -> d.setOrder(this));
        dishOrderDAO.saveAll(dishOrders);

        return orderDAO.findById(this.id);
    }
}