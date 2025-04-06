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
    private CommonCrudOperations commonCrudOperations = new CommonCrudOperations();
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
                        commonCrudOperations.getDishIngredient(id)
                );
                return dish;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return d;
    }

    public Dish findById(String id, LocalDateTime dateTime) {
        String sql = "SELECT id, name, unit_price FROM dish WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Dish dish = new Dish(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("unit_price"),
                            commonCrudOperations.getDishIngredient(id, dateTime)
                    );
                    return dish;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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
}