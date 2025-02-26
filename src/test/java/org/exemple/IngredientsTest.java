package org.exemple;

import org.example.DAO.CrudOperation.IngredientCrudOperation;
import org.example.Entity.Ingredient;
import org.example.Entity.Unity;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientsTest {
    IngredientCrudOperation subject = new IngredientCrudOperation();
    @Test
    public void test_get_all_ingredients() {
    List<Ingredient> expected = List.of(
        Saucisse(),
            Huile(),
            Oeuf()
    );
    List<Ingredient> actual = subject.findAll(1,3);
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void test_find_ingredient_by_id() {
        Ingredient excepted = subject.findById(1);
        Ingredient actual = Saucisse();
        System.out.println(actual);
        System.out.println(excepted);
        assertEquals(excepted, actual);
    }

    public Ingredient Saucisse(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Saucisse");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(20);
        ingredient.setUnity(Unity.G);
        return ingredient;
    }

    public Ingredient Huile(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2);
        ingredient.setName("Huile");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(10000);
        ingredient.setUnity(Unity.L);
        return ingredient;
    }

    public Ingredient Oeuf(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3);
        ingredient.setName("Oeuf");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(1000);
        ingredient.setUnity(Unity.U);
        return ingredient;
    }
}
