package org.example.Entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuantityStock {
    private double quantity;
    private Unity unity;
}
