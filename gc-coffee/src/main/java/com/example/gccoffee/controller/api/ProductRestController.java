package com.example.gccoffee.controller.api;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.example.gccoffee.service.ProductService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @PostMapping("/api/v1/products")
    public Product createProduct(@RequestBody CreateProductRequest productRequest) {
        return productService.createProduct(
            productRequest.productName(),
            productRequest.category(),
            productRequest.price(),
            productRequest.description()
        );
    }

    @GetMapping("/api/v1/products")
    public List<Product> productList() {
        return productService.getAllProducts();
    }

    @GetMapping("/api/v1/products/{productId}")
    public Product getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    @GetMapping("/api/v1/products/get/{category}")
    public List<Product> productListByCategory(@PathVariable Optional<Category> category) {
        return category
            .map(productService::getProductsByCategory)
            .orElse(productService.getAllProducts());
    }

    @PutMapping("/api/v1/products/{productId}")
    public Product updateProduct(
        @PathVariable UUID productId,
        @RequestBody UpdateProductRequest productRequest) {

        return productService.updateProduct(
            productId,
            productRequest.productName(),
            productRequest.category(),
            productRequest.price(),
            productRequest.description());
    }

    @DeleteMapping("/api/v1/products/{productId}")
    public void deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
    }

}
