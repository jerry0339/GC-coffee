package com.example.gccoffee.repository;

import static com.example.gccoffee.JdbcUtils.toUUID;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemJdbcRepository implements OrderItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<OrderItem> insertItems(Order order) {
        order.getOrderItems()
            .forEach(item ->
                jdbcTemplate.update(
                    "INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) "
                        +
                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                    toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(),
                        order.getUpdatedAt(), item)));
        return order.getOrderItems();
    }

    @Override
    public List<OrderItem> findByOrderId(UUID orderId) {
        return jdbcTemplate.query(
            "SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
            Collections.singletonMap("orderId", orderId.toString().getBytes()),
            orderItemRowMapper
        );
    }

    @Override
    public void update(Order order) {
        order.getOrderItems()
            .forEach(item ->
                jdbcTemplate.update(
                    "UPDATE order_items SET product_id = :productId, category = :category, price = :price, quantity = :quantity, created_at = :createdAt, updated_at = :updatedAt"
                        + " WHERE order_id = UUID_TO_BIN(:orderId)",
                    toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(),
                        order.getUpdatedAt(), item)));
    }

    @Override
    public void delete(UUID orderId) {
        jdbcTemplate.update(
            "DELETE FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
            Collections.singletonMap("orderId", orderId.toString().getBytes())
        );
    }

    public static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");
        return new OrderItem(productId, category, price, quantity);
    };

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt,
        LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("category", item.category().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }

}
