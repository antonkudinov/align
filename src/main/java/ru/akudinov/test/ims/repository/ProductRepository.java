package ru.akudinov.test.ims.repository;

import org.springframework.data.repository.CrudRepository;
import ru.akudinov.test.model.Product;

import java.util.List;

/**
 * Basic CRUD repository for products
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
    Product findByName(String name);
    List<Product> findByBrand(String brand);
}
