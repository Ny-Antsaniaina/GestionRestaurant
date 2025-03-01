package org.example.Entity;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StockMovement {
    private Ingredient ingredient;
    private double quantity;
    private MovementType type;
    private LocalDateTime timestamp;
}

