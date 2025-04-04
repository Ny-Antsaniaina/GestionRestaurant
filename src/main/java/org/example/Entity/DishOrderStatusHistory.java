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

    public DishOrderStatusHistory() {

    }
}