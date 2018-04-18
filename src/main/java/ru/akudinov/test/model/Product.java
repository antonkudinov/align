package ru.akudinov.test.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Entity
@Data
@ToString
@XmlRootElement
/**
 * Product entity
 */
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private final String name;
    private final String brand;
    private BigDecimal price;
    private int quantity;


    public Product() {
        this(null,null);
    }

    public Product(String name, String brand, BigDecimal price, int quantity) {
        this(name, brand);
        setPrice(price);
        setQuantity(quantity);
    }
}
