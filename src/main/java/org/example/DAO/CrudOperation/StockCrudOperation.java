package org.example.DAO.CrudOperation;



import com.zaxxer.hikari.util.DriverDataSource;
import org.example.Entity.Stock;
import org.example.db.ConnectionDataBase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StockCrudOperation implements CrudOperation<Stock> {
    private ConnectionDataBase dataSource = new ConnectionDataBase();
    @Override
    public List<Stock> findAll(int page, int pageSize) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Stock findById(String id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Stock> saveAll(List<Stock> list) {
        String sql = "INSERT INTO stock(id,movement,quantity,unity,date,id_ingredient) VALUES(?,?::movement,?,?::unit_enum,?,?) ON CONFLICT(id) DO NOTHING";
        List<Stock> successfullyInsertedStocks = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            for(Stock stock : list) {
                statement.setString(1, stock.getId());
                statement.setString(2, stock.getMovement().toString());
                statement.setDouble(3, stock.getQuantity());
                statement.setString(4, stock.getUnity().toString());
                statement.setTimestamp(5, Timestamp.valueOf(stock.getDate()));
                statement.setString(6, stock.getIngredient().getId());
                statement.addBatch();
            }

            int[] updateCounts = statement.executeBatch();
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] > 0) {
                    successfullyInsertedStocks.add(list.get(i));  // Only add if the update count is > 0
                }
            }

            return successfullyInsertedStocks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        throw new RuntimeException("Not implemented");
    }
}
