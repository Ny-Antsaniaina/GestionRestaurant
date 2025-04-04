package org.example.DAO.CrudOperation;

import org.example.Entity.*;
import org.example.db.ConnectionDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class OrderCrudOperation implements CrudOperation<Order> {
    private ConnectionDataBase dataSource = new ConnectionDataBase();
    private DishCrudOperation dishCrudOperation = new DishCrudOperation();
    private static final Logger logger = Logger.getLogger(OrderCrudOperation.class.getName());

    @Override
    public List<Order> findAll(int page, int pageSize) {
        String sql = "SELECT id, created_at, status FROM customer_order ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pageSize);
            statement.setInt(2, (page - 1) * pageSize);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getString("id"));
                order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                order.setStatus(StatusEnum.valueOf(resultSet.getString("status")));
                order.setDishOrders(getDishOrdersForOrder(order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch orders", e);
        }

        return orders;
    }

    @Override
    public Order findById(String id) {
        String sql = "SELECT id, created_at, status FROM customer_order WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getString("id"));
                order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                order.setStatus(StatusEnum.valueOf(resultSet.getString("status")));
                order.setDishOrders(getDishOrdersForOrder(order.getId()));
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch order with id: " + id, e);
        }

        return null;
    }
    @Override
    public List<Order> saveAll(List<Order> orders) {
        String orderSql = "INSERT INTO customer_order (id, created_at, status) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET status = EXCLUDED.status";

        String dishOrderSql = "INSERT INTO dish_order (id, order_id, dish_id, quantity, status) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET quantity = EXCLUDED.quantity, status = EXCLUDED.status";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement orderStmt = connection.prepareStatement(orderSql);
                 PreparedStatement dishOrderStmt = connection.prepareStatement(dishOrderSql)) {

                for (Order order : orders) {
                    // Sauvegarde de la commande
                    orderStmt.setString(1, order.getId());
                    orderStmt.setTimestamp(2, Timestamp.valueOf(order.getCreatedAt()));
                    orderStmt.setString(3, order.getStatus().toString());
                    orderStmt.executeUpdate();

                    // Sauvegarde des plats de la commande
                    for (DishOrder dishOrder : order.getDishOrders()) {
                        dishOrderStmt.setString(1, dishOrder.getId());
                        dishOrderStmt.setString(2, order.getId());
                        dishOrderStmt.setString(3, dishOrder.getDish().getId());
                        dishOrderStmt.setInt(4, dishOrder.getQuantity());
                        dishOrderStmt.setString(5, dishOrder.getStatus().toString());
                        dishOrderStmt.executeUpdate();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save orders", e);
        }
        return orders;
    }
    private void saveDishOrderStatusHistory(Connection connection, DishOrder dishOrder) throws SQLException {
        String sql = "INSERT INTO dish_order_status_history (id, dish_order_id, status, changed_at) " +
                "VALUES (?, ?, ?::dish_order_status_enum, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (DishOrderStatusHistory history : dishOrder.getStatusHistory()) {
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, dishOrder.getId());
                statement.setString(3, history.getStatus().toString());
                statement.setTimestamp(4, Timestamp.valueOf(history.getChangedAt()));
                statement.executeUpdate();
            }
        }
    }
    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM customer_order WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order with id: " + id, e);
        }
    }

    private List<DishOrder> getDishOrdersForOrder(String orderId) {
        String sql = "SELECT id, dish_id, quantity, status FROM dish_order WHERE order_id = ?";
        List<DishOrder> dishOrders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                DishOrder dishOrder = new DishOrder();
                dishOrder.setId(resultSet.getString("id"));
                dishOrder.setDish(dishCrudOperation.findById(resultSet.getString("dish_id")));
                dishOrder.setQuantity(resultSet.getInt("quantity"));
                dishOrder.setStatus(StatusEnum.valueOf(resultSet.getString("status")));
                dishOrder.setStatusHistory(getDishOrderStatusHistory(dishOrder.getId()));
                dishOrders.add(dishOrder);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch dish orders for order: " + orderId, e);
        }

        return dishOrders;
    }

    private List<DishOrderStatusHistory> getDishOrderStatusHistory(String dishOrderId) {
        String sql = "SELECT id, status, changed_at FROM dish_order_status_history WHERE dish_order_id = ? ORDER BY changed_at";
        List<DishOrderStatusHistory> statusHistory = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dishOrderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                DishOrderStatusHistory history = new DishOrderStatusHistory();
                history.setId(resultSet.getString("id"));
                history.setStatus(StatusEnum.valueOf(resultSet.getString("status")));
                history.setChangedAt(resultSet.getTimestamp("changed_at").toLocalDateTime());
                statusHistory.add(history);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch status history for dish order: " + dishOrderId, e);
        }

        return statusHistory;
    }

    private void saveDishOrderStatusHistory(DishOrder dishOrder) {
        String sql = "INSERT INTO dish_order_status_history (id, dish_order_id, status, changed_at) " +
                "VALUES (?, ?, ?::dish_order_status_enum, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (DishOrderStatusHistory history : dishOrder.getStatusHistory()) {
                statement.setString(1, UUID.randomUUID().toString());
                statement.setString(2, dishOrder.getId());
                statement.setString(3, history.getStatus().toString());
                statement.setTimestamp(4, Timestamp.valueOf(history.getChangedAt()));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save dish order status history", e);
        }
    }

    public void confirmOrder(String orderId) {
        Order order = findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() != StatusEnum.CREATED) {
            throw new RuntimeException("Order can only be confirmed from CREATED status");
        }

        // Check stock availability for all dishes in the order
        for (DishOrder dishOrder : order.getDishOrders()) {
            Dish dish = dishOrder.getDish();
            if (dish.getAvailableQuantity() < dishOrder.getQuantity()) {
                throw new RuntimeException("Not enough stock for dish: " + dish.getName() +
                        ". Available: " + dish.getAvailableQuantity() +
                        ", Required: " + dishOrder.getQuantity());
            }
        }

        // Update order status to CONFIRMED
        order.setStatus(StatusEnum.CONFIRMED);
        saveAll(List.of(order));
    }

    public void updateOrderStatus(String orderId, StatusEnum newStatus) {
        Order order = findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new RuntimeException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        saveAll(List.of(order));
    }

    private boolean isValidStatusTransition(StatusEnum currentStatus, StatusEnum newStatus) {
        switch (currentStatus) {
            case CREATED:
                return newStatus == StatusEnum.CONFIRMED;
            case CONFIRMED:
                return newStatus == StatusEnum.IN_PREPARATION;
            case IN_PREPARATION:
                return newStatus == StatusEnum.COMPLETED;
            case COMPLETED:
                return newStatus == StatusEnum.SERVED;
            default:
                return false;
        }
    }
}