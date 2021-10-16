package com.example.gccoffee.service;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product createProduct(String productName, Category category, long price); // ToDo : ?

    Product createProduct(String productName, Category category, long price, String description);

    List<Product> getAllProducts();

    Product getProduct(UUID productId);

    List<Product> getProductsByCategory(Category category);

    Product updateProduct(UUID productId, String productName, Category category, long price, String description);

    void deleteProduct(UUID productId);
}
