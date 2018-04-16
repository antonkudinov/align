package ru.akudinov.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.akudinov.test.ims.repository.ProductRepository;
import ru.akudinov.test.model.Product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
/**
 * This service implements basic functionality of product service
 */
public class ProductService implements IProductService {
	@Autowired private ProductRepository productRepository;

	public Product create(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product update(Product product) {
		return productRepository.save(product);
	}

	@Override
	public void delete(Product product) {
		productRepository.delete(product);
	}

	@Override
	public List<Product> listByName(String name) {
		return name != null ?  Collections.singletonList(productRepository.findByName(name)) : list();
	}

	@Override
	public List<Product> listByBrand(String brand) {
		return Optional.ofNullable(brand)
				.map(p -> productRepository.findByBrand(brand))
				.orElse((List<Product>) productRepository.findAll());
	}

	@Override
	public List<Product> list() {
		return (List<Product>) productRepository.findAll();
	}

}
