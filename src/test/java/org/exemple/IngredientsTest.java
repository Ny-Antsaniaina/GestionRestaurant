package org.exemple;

import org.example.DAO.CrudOperation.Criteria;
import org.example.DAO.CrudOperation.IngredientCrudOperation;
import org.example.Entity.Ingredient;
import org.example.Entity.QuantityStock;
import org.example.Entity.Unity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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


    private void sortIngredients(List<Ingredient> ingredients) {
        ingredients.sort(Comparator.comparing(Ingredient::getId));
    }



    @Test
    public void test_find_ingredient_by_id() {
        logger.info("  find ingredient by id  ");
        Ingredient excepted = subject.findById("1");
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
    public  void get_available_quantity_with_date(){
        Ingredient ingredient = subject.findById("1");
        QuantityStock expected = SaucisseStock();

        QuantityStock actual = ingredient.getAvalaibleQuantity(LocalDateTime.of(2025,02,24,8,0,0));
        System.out.println(actual);
        System.out.println(expected);
        assertTrue(expected.equals(actual));
    }

    @Test
    public  void get_available_quantity_without_date(){
        Ingredient ingredient = subject.findById("3");
        QuantityStock expected = OeufStock();

        QuantityStock actual = ingredient.getAvalaibleQuantity();
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected,actual);
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
        criteriaName.add(new Criteria("name", "e", "ILIKE"));


        List<Ingredient> expectedName = List.of(Saucisse(), Huile(), Oeuf());

        expectedName = expectedName.stream()
                .map(ingredient -> new Ingredient(ingredient.getId(), ingredient.getName().toLowerCase(), ingredient.getLastModifier(), ingredient.getUnitePrice(), ingredient.getUnity()))
                .sorted(Comparator.comparing(Ingredient::getName))
                .collect(Collectors.toList());


        List<Ingredient> actualName = subject.getFitersIngredient(criteriaName, null, null, 1, 3);


        actualName = actualName.stream()
                .map(ingredient -> new Ingredient(ingredient.getId(), ingredient.getName().toLowerCase(), ingredient.getLastModifier(), ingredient.getUnitePrice(), ingredient.getUnity()))
                .sorted(Comparator.comparing(Ingredient::getName))
                .collect(Collectors.toList());


        System.out.println("Sorted actualName: " + actualName);
        System.out.println("Sorted expectedName: " + expectedName);


        assertEquals(actualName.size(), expectedName.size(), "List sizes do not match!");


        for (int i = 0; i < actualName.size(); i++) {
            Ingredient expectedIngredient = expectedName.get(i);
            Ingredient actualIngredient = actualName.get(i);

            System.out.println("Comparing Ingredient " + (i + 1));
            System.out.println("Expected: " + expectedIngredient);
            System.out.println("Actual: " + actualIngredient);
            assertEquals(expectedIngredient.getId(), actualIngredient.getId(), "ID mismatch at index " + i);
            assertEquals(expectedIngredient.getName(), actualIngredient.getName(), "Name mismatch at index " + i);
            assertEquals(expectedIngredient.getLastModifier(), actualIngredient.getLastModifier(), "Last Modifier mismatch at index " + i);
            assertEquals(expectedIngredient.getUnitePrice(), actualIngredient.getUnitePrice(), "Price mismatch at index " + i);
            assertEquals(expectedIngredient.getUnity(), actualIngredient.getUnity(), "Unity mismatch at index " + i);
        }

        // Final equality check
        assertEquals(actualName, expectedName, "The lists are not equal after comparison.");


        logger.info("filter by interval price");
        List<Criteria> criteriaPrice = new ArrayList<>();
        criteriaPrice.add(new Criteria("price",2000,10000));
        List<Ingredient> expectedPrice = List.of(Huile());
        List<Ingredient> actualPrice = subject.getFitersIngredient(criteriaPrice,"price","ASC",1,3);
        System.out.println("actualPrice: " + actualPrice);
        System.out.println("expectedPrice: " + expectedPrice);
        assertEquals(expectedPrice,actualPrice);

        logger.info("filter by interval date");
        List<Criteria> criteriaDate = new ArrayList<>();
        criteriaDate.add(new Criteria("date", LocalDate.of(2024,12,01) , LocalDate.of(2025,02,02)));
        List<Ingredient> expectedDate = List.of(Saucisse(),Huile(),Oeuf(),Pain());
        List<Ingredient> actualDate = subject.getFitersIngredient(criteriaDate,null,null,1,5);
        System.out.println("actualDate: " + actualDate);
        System.out.println("expectedDate: " + expectedDate);
        assertTrue(actualDate.containsAll(expectedDate));

        logger.info("filter by pice");
        List<Criteria> price = new ArrayList<>();
        price.add(new Criteria("price",1000,"="));
        List<Ingredient> exeptedPrice = List.of(Oeuf(),Pain());
        List<Ingredient> actualPrice2 = subject.getFitersIngredient(price,"price","ASC",1,2);
        System.out.println("actualPrice2: " + actualPrice2);
        System.out.println("expectedPrice: " + exeptedPrice);
        assertTrue(actualPrice2.containsAll(exeptedPrice));
    }

    public Ingredient Saucisse(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId("1");
        ingredient.setName("Saucisse");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(20);
        ingredient.setUnity(Unity.G);
        return ingredient;
    }

    public Ingredient Huile(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId("2");
        ingredient.setName("Huile");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(10000);
        ingredient.setUnity(Unity.L);
        return ingredient;
    }

    public Ingredient Oeuf(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        ingredient.setName("Oeuf");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(1000);
        ingredient.setUnity(Unity.U);
        return ingredient;
    }

    public Ingredient Pain(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId("4");
        ingredient.setName("Pain");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(1000);
        ingredient.setUnity(Unity.U);
        return ingredient;
    }




    public QuantityStock SaucisseStock(){
        return createQuantityStock(10000.0, Unity.G);
    }

    public QuantityStock HuileStock(){
        return createQuantityStock(20.0, Unity.L);
    }

    public QuantityStock OeufStock(){
        return createQuantityStock(80.0, Unity.U);
    }

    public QuantityStock PainStock(){
        return createQuantityStock(30.0, Unity.U);
    }

    public QuantityStock createQuantityStock(Double quantity, Unity unity) {
        return new QuantityStock(quantity,unity);
    }
}
