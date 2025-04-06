package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DishOrderStatusHistory {
    private String id;
    private DishOrder dishOrder;
    private StatusEnum status;
    private LocalDateTime changedAt;

    public DishOrderStatusHistory(String id, StatusEnum status, LocalDateTime changedAt) {
        this.id = id;
        this.status = status;
        this.changedAt = changedAt;
    }

    public DishOrderStatusHistory() {
        this.id = null;
        this.status = null;
        this.changedAt = null;
    }

}