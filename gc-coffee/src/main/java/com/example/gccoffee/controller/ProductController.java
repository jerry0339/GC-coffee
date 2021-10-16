package com.example.gccoffee.controller;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.example.gccoffee.service.ProductService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        var products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("new-product")
    public String newProductPage() {
        return "new-product";
    }

    @PostMapping("/products")
    public String newProduct(CreateProductRequest createProductRequest) {
        productService.createProduct(
            createProductRequest.productName(),
            createProductRequest.category(),
            createProductRequest.price(),
            createProductRequest.description());
        return "redirect:/products"; // redirect:/~ 해당 경로로 이동 (view관련)
    }

    @GetMapping("products/update/{id}")
    public String updateProductForm(@PathVariable UUID id, Model model) {
        Product product = productService.getProduct(id);
        log.info("get project update is called.");
        model.addAttribute("product", product);
        return "update-product";
    }

    @PostMapping("products/update/{id}")
    public String updateProduct(@PathVariable UUID id, UpdateProductRequest updateProductRequest) {
        productService.updateProduct(
            id,
            updateProductRequest.productName(),
            updateProductRequest.category(),
            updateProductRequest.price(),
            updateProductRequest.description());
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable UUID id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }

}
