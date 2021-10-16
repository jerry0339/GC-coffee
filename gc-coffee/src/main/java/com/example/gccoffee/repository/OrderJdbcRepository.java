package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import com.example.gccoffee.model.OrderStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.gccoffee.JdbcUtils.toLocalDateTime;
import static com.example.gccoffee.JdbcUtils.toUUID;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Order insert(Order order) {
        jdbcTemplate.update(
            "INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) "
                +
                "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
            toOrderParamMap(order));
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query("select * from orders", orderRowMapper);
        return orders;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                    Collections.singletonMap("orderId", orderId.toString().getBytes()),
                    orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Order update(Order order) {
        int update = jdbcTemplate.update(
            "UPDATE orders SET email = :email, address = :address, postcode = :postcode, order_status = :orderStatus, created_at = :createdAt, updated_at = :updatedAt"
                + " WHERE order_id = UUID_TO_BIN(:orderId)",
            toOrderParamMap(order)
        );
        if(update != 1){
            throw new RuntimeException("Nothing was updated.");
        }
        return order;
    }

    @Override
    public void delete(UUID orderId) {
        jdbcTemplate.update(
            "DELETE FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
            Collections.singletonMap("orderId", orderId.toString().getBytes())
        );
    }

    private RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        List<OrderItem> orderItems = new ArrayList<>();
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Order(orderId, email, address, postcode, orderItems, orderStatus, createdAt, updatedAt);
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

}
