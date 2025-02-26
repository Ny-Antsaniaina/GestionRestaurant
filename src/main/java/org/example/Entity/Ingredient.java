package org.example.Entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Ingredient {
    private int id;
    private String name;
    private LocalDateTime lastModifier;
    private double unitePrice;
    private Unity unity;

    public Ingredient(int id, String name, LocalDateTime lastModifier , double unitePrice, Unity unity) {
        this.id = id;
        this.name = name;
        this.lastModifier = lastModifier;
        this.unitePrice = unitePrice;
        this.unity=unity;
    }

    public Ingredient() {

    }
}
