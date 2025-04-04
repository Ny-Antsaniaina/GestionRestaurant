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
public class DishOrder {
    private String id;
    private Dish dish;
    private int quantity;
    private StatusEnum status;
    private List<DishOrderStatusHistory> statusHistory = new ArrayList<>();

    public DishOrder() {
        this.statusHistory.add(new DishOrderStatusHistory(UUID.randomUUID().toString(), this, StatusEnum.CREATED, LocalDateTime.now()));
    }

    public DishOrder(String id, Dish dish, int quantity, StatusEnum status, List<DishOrderStatusHistory> statusHistory) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
        this.status = status;
        this.statusHistory = statusHistory != null ? statusHistory : new ArrayList<>();
    }

    public StatusEnum getActualStatus() {
        return status;
    }

    public void updateStatus(StatusEnum newStatus) {
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new RuntimeException("Invalid status transition from " + this.status + " to " + newStatus);
        }

        this.status = newStatus;
        this.statusHistory.add(new DishOrderStatusHistory(
                UUID.randomUUID().toString(),
                this,
                newStatus,
                LocalDateTime.now()
        ));
    }

    private boolean isValidStatusTransition(StatusEnum currentStatus, StatusEnum newStatus) {
        switch (currentStatus) {
            case CREATED:
                return newStatus == StatusEnum.CONFIRMED;
            case CONFIRMED:
                return newStatus == StatusEnum.IN_PREPARATION;
            case IN_PREPARATION:
                return newStatus == StatusEnum.COMPLETED;
            case COMPLETED:
                return newStatus == StatusEnum.SERVED;
            default:
                return false;
        }
    }
}