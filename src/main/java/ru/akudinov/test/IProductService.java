package ru.akudinov.test;

import ru.akudinov.test.model.Product;

import java.util.List;

public interface IProductService {
    Product create(Product product);

    Product update(Product product);

    void delete(long productId);

    List<Product> listByName(String name);

    List<Product> listByBrand(String brand);

    List<Product> list();

    List<Product> leftOversList();
}
