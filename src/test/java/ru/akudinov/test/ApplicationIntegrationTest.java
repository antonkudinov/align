package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akudinov.test.ims.repository.ProductRepository;
import ru.akudinov.test.model.Product;

/**
 * Created by akudinov on 03.10.16.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationIntegrationTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void useRepositories() {

        productRepository.save(new Product("Boots", "Nike"));
        productRepository.save(new Product("IPhone", "Apple"));


        productRepository.findAll().forEach(p -> log.info(p.toString()));
        for (Product product : productRepository.findAll()) {
            log.info("Hello " + product.toString());
        }
    }
}
