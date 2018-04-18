package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akudinov.test.ims.repository.ProductRepository;
import ru.akudinov.test.model.Product;

import javax.ws.rs.core.GenericType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class RestApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ProductRepository repository;

	@Before
	public void init(){
		repository.deleteAll();
	}

	@Test
    public void testCreate(){
        Product product = new Product
                ("iPhone", "Apple");

        ResponseEntity<Product> entity = this.restTemplate.postForEntity("/product/create", product,
                Product.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assert.assertNotNull(entity.getBody().getId());
    }

    @Test
    public void testUpdate(){
        Product product = new Product
                ("iPhone", "Apple");


        product = this.restTemplate.postForObject("/product/create", product,  Product.class);
        Assert.assertNotNull(product.getId());

        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantity(10);
        this.restTemplate.put("/product/update", product);

        ResponseEntity<List> prodList = restTemplate.getForEntity("/product/list?name={name}", List.class,"iPhone");
        assertNotNull(prodList);
        Object object = prodList.getBody().get(0);

        assertNotNull(object);

        Map<String, Object> map = (Map<String, Object>) object;

        BigDecimal price = BigDecimal.valueOf((Double)map.get("price"));
        assertThat(price).isEqualTo(BigDecimal.valueOf(100d));

    }

	@Test
	public void testListByName(){
		Arrays.asList(
			new Product("iPhone", "Apple"),
			new Product("S9", "Samsung"),
			new Product("Mi6", "Xiaomi")
		).forEach(product -> {
					ResponseEntity<Product> o = this.restTemplate.postForEntity("/product/create", product, Product.class);
					log.info("Create product: {}", o.toString());
				}
			);

		ResponseEntity<List> entity = restTemplate.getForEntity("/product/list?name={name}", List.class, "S9");
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(1, entity.getBody().size());
	}

	@Test
	public void testListByBrand(){
		Arrays.asList(
				new Product("iPhone", "Apple"),
				new Product("S9", "Samsung"),
				new Product("Mi6", "Xiaomi")
		).forEach(product -> {
					ResponseEntity<Product> o = this.restTemplate.postForEntity("/product/create", product, Product.class);
					log.info("Create product: {}", o.toString());
				}
		);

		ResponseEntity<List> entity = restTemplate.getForEntity("/product/list?name={name}", List.class, "Samsung");
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(1, entity.getBody().size());
	}

	@Test
	public void testList(){
		Arrays.asList(
				new Product("iPhone", "Apple"),
				new Product("S9", "Samsung"),
				new Product("Mi6", "Xiaomi")
		).forEach(product ->
				this.restTemplate.postForEntity("/product/create", product, Product.class));

		ResponseEntity<List> entity = restTemplate.getForEntity("/product/list", List.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(3, entity.getBody().size());
	}

	@Test
	public void tesLeftOversList(){
		Arrays.asList(
				new Product("iPhone", "Apple", amount(200), 3),
				new Product("S9", "Samsung", amount(100), 10),
				new Product("Mi6", "Xiaomi", amount(20), 2)
		).forEach(product ->
				this.restTemplate.postForEntity("/product/create", product, Product.class));

		ResponseEntity<List> entity = restTemplate.getForEntity("/product/leftovers", List.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(2, entity.getBody().size());
	}

	private BigDecimal amount(int i) {
		return BigDecimal.valueOf(i);
	}

}
