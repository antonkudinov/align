package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.akudinov.test.config.WebSecurityConfig;
import ru.akudinov.test.ims.repository.ProductRepository;
import ru.akudinov.test.model.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {RestApplication.class, WebSecurityConfig.class})
@Slf4j
public class RestApplicationTests {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private ProductRepository repository;


    @Autowired
    private WebApplicationContext context;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MockMvc mockMvc;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void init() {
        repository.deleteAll();


        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreate() throws Exception {
        Product product = new Product
                ("iPhone", "Apple");

        mockMvc
                .perform(
                        post("/product")
                                .contentType(contentType)
                                .content(json(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate() throws Exception {
        Product product = new Product
                ("iPhone", "Apple");


        mockMvc
                .perform(
                        post("/product")
                                .contentType(contentType)
                                .content(json(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty());


        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantity(10);

        mockMvc
                .perform(
                        put("/product/1")
                                .contentType(contentType)
                                .content(json(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("price").value("100"))
                .andExpect(jsonPath("quantity").value("10"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testListByName() throws Exception {
        createProductList();

        mockMvc
                .perform(
                        get("/product/list?name=S9")
                                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("S9"));
    }

    private void createProductList() {
        Arrays.asList(
                new Product("iPhone", "Apple"),
                new Product("S9", "Samsung"),
                new Product("Mi6", "Xiaomi")
        ).forEach(product -> {
                    try {
                        mockMvc
                                .perform(
                                        post("/product")
                                                .with(user("admin").roles("ADMIN"))
                                                .contentType(contentType)
                                                .content(json(product)))
                                .andExpect(status().isOk());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testListByBrand() throws Exception {
        createProductList();

        mockMvc
                .perform(
                        get("/product/list?brand=Samsung")
                                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("S9"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testList() throws Exception {
        createProductList();

        mockMvc
                .perform(
                        get("/product/list")
                                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length($)").value("3"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void tesLeftOversList() throws Exception {
        createProductList();

        mockMvc
                .perform(
                        get("/product/list/leftovers")
                                .contentType(contentType))
                .andExpect(status().isOk());
    }

    private BigDecimal amount(int i) {
        return BigDecimal.valueOf(i);
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
