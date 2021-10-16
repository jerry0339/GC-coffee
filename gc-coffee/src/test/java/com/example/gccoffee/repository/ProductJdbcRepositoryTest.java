package com.example.gccoffee.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest // @SpringBootApplication 붙은 클래스를 찾아줌
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test") // application-test.yaml 을 읽어옴
@TestInstance(Lifecycle.PER_CLASS) // 아래 주석에서 설명
class ProductJdbcRepositoryTest {

    @Autowired
    ProductJdbcRepository repository;

    // 아래의 @Test할때마다 해당 클래스의 인스턴스가 다시 생성되기 때문에,
    // Order(1)에서 insert된 id와 Order(3)에서 조회하는 id는 다를수가 있음
    // 따라서, 아래의 newProduct를 static으로 바꿔주던가
    // 아니면 테스트 클래스에 @TestInstance(Lifecycle.PER_CLASS) 를 붙여주어야 함
    // 즉, default는 @TestInstance(Lifecycle.PER_METHOD) 임

    @Test
    @Order(1)
    @DisplayName("Test")
    void testTest() {
        List<Product> all = repository.findAll();
        System.out.println("all = " + all.get(0).getProductName());
    }

    public final Product newProduct = new Product(UUID.randomUUID(), "new-product", Category.COFFEE_BEAN_PACKAGE, 1234L);

    @Test
    @Order(1)
    @DisplayName("상품을 추가할 수 있다.")
    void testInsert() {
        repository.insert(newProduct);
        List<Product> all = repository.findAll();
        assertThat(all.isEmpty(), is(false));
    }

    @Test
    @Order(2)
    @DisplayName("상품을 이름으로 조회할 수 있다.")
    void testFindByName() {
        Optional<Product> product = repository.findByName(newProduct.getProductName());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(3)
    @DisplayName("상품을 아이디로 조회할 수 있다.")
    void testFindById() {
        Optional<Product> product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("상품들을 카테고리로 조회할 수 있다.")
    void testFindByCategory() {
        List<Product> product = repository.findByCategory(Category.COFFEE_BEAN_PACKAGE);
        assertThat(product.isEmpty(), is(false));
    }

    @Test
    @Order(5)
    @DisplayName("상품을 수정할 수 있다.")
    void testUpdate() {
        newProduct.setProductName("updated-product");
        repository.update(newProduct);

        Optional<Product> product = repository.findById(newProduct.getProductId());
        assertThat(product.isEmpty(), is(false));
        assertThat(product.get(), samePropertyValuesAs(newProduct));
    }

    @Test
    @Order(6)
    @DisplayName("상품을 전체 삭제한다.")
    void testDeleteAll() {
        repository.deleteAll();
        List<Product> all = repository.findAll();
        assertThat(all.isEmpty(), is(true));
    }


}