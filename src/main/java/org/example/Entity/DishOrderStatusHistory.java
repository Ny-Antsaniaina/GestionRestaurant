package org.example.Entity;

import java.time.LocalDateTime;

public class DishOrderStatusHistory {
    private String id ;
    private DishOrder dishOrderId;
    private StatusEnum status;
    private LocalDateTime changed_at ;
}
