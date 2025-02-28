package org.exemple;

import org.example.DAO.CrudOperation.Criteria;
import org.example.DAO.CrudOperation.IngredientCrudOperation;
import org.example.Entity.Ingredient;
import org.example.Entity.Unity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IngredientsTest {
    Logger logger = Logger.getLogger(IngredientsTest.class.getName());
    IngredientCrudOperation subject = new IngredientCrudOperation();
    @Test
    public void test_get_all_ingredients() {
        logger.info("get all ingredient");

        List<Ingredient> expected = new ArrayList<>(List.of(Saucisse(),Huile()));
        List<Ingredient> actual = subject.findAll(1, 2);


        sortIngredients(expected);
        sortIngredients(actual);

        System.out.println("Actual: " + actual);
        System.out.println("Expected: " + expected);

        assertEquals(expected, actual);
    }

    // Méthode pour trier la liste des ingrédients par ID
    private void sortIngredients(List<Ingredient> ingredients) {
        ingredients.sort(Comparator.comparing(Ingredient::getId));
    }



    @Test
    public void test_find_ingredient_by_id() {
        logger.info("  find ingredient by id  ");
        Ingredient excepted = subject.findById(1);
        Ingredient actual = Saucisse();
        System.out.println(actual);
        System.out.println(excepted);
        assertEquals(excepted, actual);
    }

    @Test
    public void test_saveAll_ingredients() {
        logger.info("saveAll ingredients");
        List<Ingredient> expected = List.of(
                Saucisse(),Huile()
        );
        List<Ingredient> actual = subject.saveAll(expected);
        System.out.println(actual);
        System.out.println(expected);
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void test_filter_ingredients_by_criteria() {
        logger.info("  filter by name  ");
     List<Criteria> criteria = new ArrayList<>();
     criteria.add(new Criteria("name" , "Saucisse","=" ));
     List<Ingredient> expected = List.of(Saucisse());
     List<Ingredient> actual = subject.getFitersIngredient(criteria,null,null,1,2);
     System.out.println(actual);
     System.out.println(expected);
     assertTrue(actual.containsAll(expected));


     logger.info("filter by name but with Ilike");
        List<Criteria> criteriaName = new ArrayList<>();
        criteriaName.add(new Criteria("name" , "e","ILIKE" ));
        List<Ingredient> expectedName = List.of(Saucisse(),Huile(),Oeuf());
        List<Ingredient> actualName = subject.getFitersIngredient(criteriaName,null,null,1,3);
        System.out.println("actualName: " + actualName);
        System.out.println("expectedName: " + expectedName);
       assertTrue(actualName.containsAll(expectedName));

        logger.info("filter by interval price");
        List<Criteria> criteriaPrice = new ArrayList<>();
        criteriaPrice.add(new Criteria("unit_price",1,1000));
        List<Ingredient> expectedPrice = List.of(Saucisse(),Oeuf(),Pain());
        List<Ingredient> actualPrice = subject.getFitersIngredient(criteriaPrice,"unit_price","ASC",1,3);
        System.out.println("actualPrice: " + actualPrice);
        System.out.println("expectedPrice: " + expectedPrice);
        assertTrue(actualPrice.containsAll(expectedPrice));

        logger.info("filter by interval date");
        List<Criteria> criteriaDate = new ArrayList<>();
        criteriaDate.add(new Criteria("last_modified", LocalDate.of(2024,12,01) , LocalDate.of(2025,02,02)));
        List<Ingredient> expectedDate = List.of(Saucisse(),Huile(),Oeuf(),Pain());
        List<Ingredient> actualDate = subject.getFitersIngredient(criteriaDate,null,null,1,5);
        System.out.println("actualDate: " + actualDate);
        System.out.println("expectedDate: " + expectedDate);
        assertTrue(actualDate.containsAll(expectedDate));

        logger.info("filter by pice");
        List<Criteria> price = new ArrayList<>();
        price.add(new Criteria("unit_price",1000,"="));
        List<Ingredient> exeptedPrice = List.of(Oeuf(),Pain());
        List<Ingredient> actualPrice2 = subject.getFitersIngredient(price,"unit_price","ASC",1,2);
        System.out.println("actualPrice2: " + actualPrice2);
        System.out.println("expectedPrice: " + exeptedPrice);
        assertTrue(actualPrice2.containsAll(exeptedPrice));
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

    public Ingredient Pain(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(4);
        ingredient.setName("Pain");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(1000);
        ingredient.setUnity(Unity.U);
        return ingredient;
    }
}
