package org.example.DAO.CrudOperation;

import org.example.DAO.Mapper.EnumMapper;
import org.example.Entity.*;
import org.example.db.ConnectionDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DishCrudOperation implements CrudOperation<Dish> {
    private ConnectionDataBase dataSource = new ConnectionDataBase();
    Logger logger = Logger.getLogger(DishCrudOperation.class.getName());

    @Override
    public List<Dish> findAll(int page, int pageSize) {
        if (page < 1) {
            logger.warning("Page is less than 1");
            page = 1;
        }

        String sql = """
        SELECT d.id, d.name, d.unit_price, 
               i.id AS ingredient_id, i.name AS ingredient_name, 
               i.unity, di.quantity AS required_quantity, di.unity AS dish_unity
        FROM dish d 
        LEFT JOIN dish_ingredient di ON d.id = di.dish_id 
        LEFT JOIN ingredient i ON di.ingredient_id = i.id 
        ORDER BY d.id 
        LIMIT ? OFFSET ?;
    """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            logger.info("Executing query: " + sql);
            statement.setInt(1, pageSize);
            statement.setInt(2, pageSize * (page - 1));

            ResultSet resultSet = statement.executeQuery();
            List<Dish> dishes = new ArrayList<>();

            while (resultSet.next()) {
                String dishId = resultSet.getString("id");
                Dish dish = null;

                for (Dish d : dishes) {
                    if (d.getId().equals(dishId)) {
                        dish = d;
                        break;
                    }
                }

                if (dish == null) {
                    dish = new Dish();
                    dish.setId(dishId);
                    dish.setName(resultSet.getString("name"));
                    dish.setUnitPrice(resultSet.getInt("unit_price"));
                    dish.setIngredients(new ArrayList<>());
                    dishes.add(dish);
                }

                String ingredientId = resultSet.getString("ingredient_id");
                if (ingredientId != null) {
                    Ingredient ingredient = new Ingredient(
                            ingredientId,
                            resultSet.getString("name"),
                            null,
                            0.0,
                            Unity.valueOf(resultSet.getString("unity"))
                    );

                    DishIngredient dishIngredient = new DishIngredient(
                            ingredient,
                            resultSet.getDouble("required_quantity"),
                            Unity.valueOf(resultSet.getString("dish_unity"))
                    );

                    dish.getIngredients().add(dishIngredient);
                }
            }

            return dishes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dish findById(String id) {
        String sql = "select d.id , d.name , d.unit_price from dish d where d.id=?";

        Dish d = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            logger.info("Executing query" + sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("unit_price"),
                        getIngredientWithQuantity(id)
                );
                return dish;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return d;
    }

    @Override
    public List<Dish> saveAll(List<Dish> dishes) {
        List<Dish> list = new ArrayList<>();

        String sql = "insert into dish (id , name,unit_price) values (?,?,?) on conflict (id) do update set name = EXCLUDED.name,unit_price = EXCLUDED.unit_price";

        try(Connection connection = dataSource.getConnection()){
            logger.info("Executing query" + sql);
            for (Dish d : dishes) {
                try(PreparedStatement statement = connection.prepareStatement(sql)){
                    statement.setString(1,d.getId());
                    statement.setString(2,d.getName());
                    statement.setInt(3,d.getUnitPrice());
                    statement.executeUpdate();
                    list.add(d);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            logger.info( list.size() + " dishes have been saved");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Dish getListIngredientInDish(int id) {
        Dish dish = null;
        List<DishIngredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String dishSQL = "SELECT id, name, sale_price FROM dish WHERE id=?";
            PreparedStatement dishStatement = connection.prepareStatement(dishSQL);
            dishStatement.setInt(1, id);
            ResultSet resultSet = dishStatement.executeQuery();

            if (resultSet.next()) {
                dish = new Dish();
                dish.setId(resultSet.getString("id"));
                dish.setName(resultSet.getString("name"));
                dish.setUnitPrice(resultSet.getInt("sale_price"));
            } else {
                return null;
            }

            String dishIngredientSQL = "SELECT di.ingredient_id, di.quantity, di.unit, " +
                    "i.name, i.unit_price " +
                    "FROM dish_ingredient di " +
                    "JOIN ingredient i ON di.ingredient_id = i.id " +
                    "WHERE di.dish_id = ?";
            PreparedStatement ingredientStatement = connection.prepareStatement(dishIngredientSQL);
            ingredientStatement.setInt(1, id);
            ResultSet ingredientResultSet = ingredientStatement.executeQuery();

            while (ingredientResultSet.next()) {
                Ingredient ingredient = new Ingredient(
                        ingredientResultSet.getString("ingredient_id"),
                        ingredientResultSet.getString("name"),
                        null,
                        ingredientResultSet.getDouble("unit_price"),
                        EnumMapper.topicmaper(ingredientResultSet.getString("unit"))
                );

                DishIngredient dishIngredient = new DishIngredient(
                        ingredient,
                        ingredientResultSet.getDouble("quantity"),
                        EnumMapper.topicmaper(ingredientResultSet.getString("unit"))
                );

                ingredients.add(dishIngredient);
            }

            dish.setIngredients(ingredients);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching dish with ingredients", e);
        }

        return dish;
    }

    @Override
     public void deleteById(String id) {
         String sql = "delete from dish where id=?";

         try (Connection connection = dataSource.getConnection();
              PreparedStatement statement = connection.prepareStatement(sql)) {
             logger.info("Executing query: " + sql);
             statement.setString(1, id);
             int rowsAffected = statement.executeUpdate();

             if (rowsAffected > 0) {
                 logger.info("Dish with id " + id + " has been deleted.");
             } else {
                 logger.warning("No dish found with id " + id + ".");
             }
         } catch (SQLException e) {
             throw new RuntimeException("Error deleting dish with id " + id, e);
         }
     }


    public Ingredient findIngredientById(String id) {
        String sql = "SELECT id, name, unity FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"));
                    Ingredient ingredient = new Ingredient(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            ingredientPrice.getDate(),
                            ingredientPrice.getUnitPrice(),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Ingredient findIngredientById(String id, LocalDateTime dateTime) {
        String sql = "SELECT id, name, unity FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"), dateTime);
                    Ingredient ingredient = new Ingredient(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            ingredientPrice.getDate(),
                            ingredientPrice.getUnitPrice(),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public IngredientPrice getIngredientPrice(String ingredient_id){
        String sql = "SELECT price, date FROM ingredient_price_history WHERE ingredient_id = ? AND date <= CURRENT_DATE ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getInt("price"),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                    return ingredientPrice;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public IngredientPrice getIngredientPrice(String ingredient_id, LocalDateTime dateTime){
        String sql = "SELECT price, date FROM ingredient_price_history WHERE ingredient_id = ? AND date <= ? ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(dateTime));
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getInt("price"),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                    return ingredientPrice;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishIngredient> getIngredientWithQuantity(String id_dish) {
        List<DishIngredient> ingredientsWithQuantityList = new ArrayList<>();
        String sql = "SELECT ingredient_id, quantity, unity FROM dish_ingredient WHERE dish_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("ingredient_id"));
                    DishIngredient ingredientWithQuantity = new DishIngredient(
                            ingredient,
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    ingredientsWithQuantityList.add(ingredientWithQuantity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientsWithQuantityList;
    }
    public List<DishIngredient> getIngredientWithQuantity(String id_dish, LocalDateTime dateTime) {
        List<DishIngredient> ingredientsWithQuantityList = new ArrayList<>();
        String sql = "SELECT ingredient_id, quantity, unity FROM dish_ingredient WHERE dish_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("ingredient_id"), dateTime);
                    DishIngredient ingredientWithQuantity = new DishIngredient(
                            ingredient,
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    ingredientsWithQuantityList.add(ingredientWithQuantity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientsWithQuantityList;
    }

}
