package org.example.DAO.CrudOperation;

import org.example.db.ConnectionDataBase;
import org.example.Entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientCrudOperation implements CrudOperation<Ingredient> {
    ConnectionDataBase dataSource = new ConnectionDataBase();

    @Override
    public List<Ingredient> findAll(int page, int pageSize) {
        String sql = "SELECT id , name , unity FROM ingredient ORDER by id LIMIT ? OFFSET ?";

        if (page < 0) {
            throw new IllegalArgumentException("page cannot be less than zero");
        } else {
            List<Ingredient> list = new ArrayList<>();
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);) {
                statement.setInt(1, pageSize);
                statement.setInt(2, pageSize * (page - 1));
                try (ResultSet resultSet = statement.executeQuery();) {
                    while (resultSet.next()) {
                        IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"));
                        Ingredient ingredient = new Ingredient();
                        ingredient.setId(resultSet.getString("id"));
                        ingredient.setName(resultSet.getString("name"));
                        ingredient.setLastModifier(ingredientPrice.getDate());
                        ingredient.setUnitePrice(ingredientPrice.getUnitPrice());
                        ingredient.setUnity(Unity.valueOf(resultSet.getString("unity")));
                        list.add(ingredient);
                    }

                    return list;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Ingredient findById(String id) {
        String sql = "SELECT id , name , unity FROM ingredient WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    IngredientPrice ingredientPrice = getIngredientPrice(rs.getString("id"));
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getString("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setLastModifier(ingredientPrice.getDate());
                    ingredient.setUnitePrice(ingredientPrice.getUnitPrice());
                    ingredient.setUnity(Unity.valueOf(rs.getString("unity")));
                    return ingredient;
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public List<Ingredient> saveAll(List<Ingredient> list) {
        List<Ingredient> ingredients = new ArrayList<>();
        String ingredientSql = "INSERT INTO ingredient (id, name, unity) VALUES (?, ?, ?::unit_enum) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, unity = EXCLUDED.unity";
        String priceSql = "INSERT INTO ingredient_price_history (id, ingredient_id , price, date) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET price = EXCLUDED.price, date = EXCLUDED.date";


            try(Connection connection = dataSource.getConnection();
                PreparedStatement ingredientStatement = connection.prepareStatement(ingredientSql);
                PreparedStatement priceStatement = connection.prepareStatement(priceSql)){

                for(Ingredient ingredient : list) {
                    ingredientStatement.setString(1, ingredient.getId());
                    ingredientStatement.setString(2, ingredient.getName());
                    ingredientStatement.setString(3, ingredient.getUnity().toString());
                    ingredientStatement.executeUpdate();

                    priceStatement.setString(1, getPriceID(ingredient.getId()));
                    priceStatement.setString(2, ingredient.getId());
                    priceStatement.setDouble(3, ingredient.getUnitePrice());
                    priceStatement.setTimestamp(4, Timestamp.valueOf(ingredient.getLastModifier()));
                    priceStatement.executeUpdate();
                    Ingredient ingredientToDB = findById(ingredient.getId());
                    ingredients.add(ingredientToDB);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return ingredients;
    }

    

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM ingredient WHERE id = ?";
        try (Connection connection =dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Ingredient> getFitersIngredient(List<Criteria> criteria, String sortBy, String sortOrder, int page, int pageSize) {
        List<Ingredient> listIngredients = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT i.id, i.name, i.unity FROM ingredient i");

        // Join the ingredient_price_history table for price-based filtering
        sql.append(" LEFT JOIN ingredient_price_history iph ON i.id = iph.ingredient_id");

        sql.append(" WHERE 1=1");

        for (Criteria c : criteria) {
            if ("BETWEEN".equals(c.getOperator())) {
                sql.append(" AND ").append(c.getColumn()).append(" BETWEEN ? AND ?");
            } else if ("LIKE".equals(c.getOperator()) || "ILIKE".equals(c.getOperator())) {
                sql.append(" AND ").append(c.getColumn()).append(" ILIKE ?");
            } else {
                sql.append(" AND ").append(c.getColumn()).append(" ").append(c.getOperator()).append(" ?");
            }
        }

        // Add sorting if necessary
        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ").append(sortBy).append(" ").append((sortOrder != null ? sortOrder : "ASC"));
        }

        // Add pagination
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int i = 1;

            // Set the criteria parameters
            for (Criteria c : criteria) {
                if ("BETWEEN".equals(c.getOperator())) {
                    statement.setObject(i++, c.getValueIntervalMin());
                    statement.setObject(i++, c.getValueIntervalMax());
                } else if ("LIKE".equals(c.getOperator()) || "ILIKE".equals(c.getOperator())) {
                    statement.setString(i++, "%" + c.getValueIntervalMin() + "%");
                } else {
                    statement.setObject(i++, c.getValueIntervalMin());
                }
            }

            // Set pagination parameters
            statement.setInt(i++, pageSize);
            statement.setInt(i++, pageSize * (page - 1));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"));
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(resultSet.getString("id"));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setLastModifier(ingredientPrice.getDate());
                    ingredient.setUnitePrice(ingredientPrice.getUnitPrice());
                    ingredient.setUnity(Unity.valueOf(resultSet.getString("unity")));
                    listIngredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listIngredients;
    }



    public String getPriceID(String ingredient_id) {
        String sql = "SELECT id FROM ingredient_price_history WHERE id = ?";
        String id = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ingredient_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getString("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public IngredientPrice getIngredientPrice(String ingredient_id){
        String sql = "SELECT price, date FROM ingredient_price_history WHERE id = ? ORDER BY ABS(EXTRACT(EPOCH FROM date) - EXTRACT(EPOCH FROM ?::timestamp)) LIMIT 1;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice();
                    ingredientPrice.setUnitPrice(resultSet.getInt("price"));
                    ingredientPrice.setDate(resultSet.getTimestamp("date").toLocalDateTime());

                    return ingredientPrice;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public IngredientPrice getIngredientPrice(String ingredient_id, LocalDateTime date){
        String sql = "SELECT price, date FROM ingredient_price_history WHERE id = ? AND date <= ? ORDER BY ABS(EXTRACT(EPOCH FROM date) - EXTRACT(EPOCH FROM ?::timestamp)) LIMIT 1;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(date));
            statement.setTimestamp(3, Timestamp.valueOf(date));
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


    public List<Stock> getAvailableStocks(String ingredient_id){
        String sql = "SELECT id, movement, quantity, unity, date, id_ingredient FROM stock WHERE id_ingredient = ? AND date <= ?";
        List<Stock> stocks = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try(ResultSet resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    Ingredient ingredient = findById(resultSet.getString("id"));
                    Stock stock = new Stock(
                            resultSet.getString("id"),
                            Movement.valueOf(resultSet.getString("movement")),
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity")),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            ingredient
                    );
                    stocks.add(stock);
                }
            }
            return stocks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stock> getAvailableStocks(String ingredient_id, LocalDateTime date){
        String sql = "SELECT id, movement, quantity, unity, date, id_ingredient FROM stock WHERE id_ingredient = ? AND date <= ?";
        List<Stock> stocks = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(date));
            try(ResultSet resultSet = statement.executeQuery();){
                while(resultSet.next()){
                    Ingredient ingredient = findById(resultSet.getString("id"));
                    Stock stock = new Stock(
                            resultSet.getString("id"),
                            Movement.valueOf(resultSet.getString("movement")),
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity")),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            ingredient
                    );
                    stocks.add(stock);
                }
            }
            return stocks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}