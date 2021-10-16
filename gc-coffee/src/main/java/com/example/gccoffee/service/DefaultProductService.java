package com.example.gccoffee.service;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.example.gccoffee.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(String productName, Category category, long price) {
        Product product = new Product(UUID.randomUUID(), productName, category, price);
        return productRepository.insert(product);
    }

    @Override
    @Transactional
    public Product createProduct(String productName, Category category, long price, String description) {
        Product product = new Product(UUID.randomUUID(), productName, category, price, description, LocalDateTime.now(), LocalDateTime.now());
        return productRepository.insert(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(UUID productId) {
        return productRepository.findById(productId).get();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID productId, String productName, Category category, long price, String description) {
        Product product = productRepository.findById(productId).get();
        product.setProductName(productName);
        product.setCategory(category);
        product.setPrice(price);
        product.setDescription(description);
        productRepository.update(product);
        return product;
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        productRepository.delete(productId);
    }

}
