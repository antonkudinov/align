package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.akudinov.test.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
public class ProductServiceRestController {
    private final IProductService productService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public ProductServiceRestController(IProductService loanService) {
        this.productService = loanService;
    }

    @PostMapping("product")
    @ResponseBody
    public Product create(@RequestBody Product loan) {
        return productService.create(loan);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/product/{productId}", produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Product update(@PathVariable("productId") long productId, @RequestBody Product loan) {
        loan.setId(productId);
        return productService.update(loan);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/product/{productId}", produces = "application/json", consumes = "application/json")
    public void delete(@PathVariable("productId") long productId) {
        productService.delete(productId);
    }


    @GetMapping("/product/list")
    @ResponseBody
    public List<Product> list(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "brand", required = false) String brand) {
        return name != null ? productService.listByName(name)
                : brand != null ? productService.listByBrand(brand)
                : productService.list();
    }

    @GetMapping("/product/list/leftovers")
    public List<Product> listLeftOvers() {
        return productService.leftOversList();

    }

    @GetMapping(value = "/product/list/xls")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity getReportListAsXLS(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "brand", required = false) String brand) {
        return createXls(list(name, brand));
    }

    @GetMapping(value = "/product/list/leftovers/xls")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity getReportAsXLS() {
        return createXls(listLeftOvers());
    }

    private HttpEntity createXls(final List<Product> products) {
        final XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("leftovers");
        Row header = sheet.createRow(1);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Product");
        header.createCell(2).setCellValue("Brand");
        header.createCell(3).setCellValue("Price");
        header.createCell(4).setCellValue("Quantity");


        products.forEach(
                product -> {
                    Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                    row.createCell(0).setCellValue(product.getId());
                    row.createCell(1).setCellValue(product.getName());
                    row.createCell(2).setCellValue(product.getBrand());
                    row.createCell(3).setCellValue(amount(product.getPrice()));
                    row.createCell(4).setCellValue(product.getQuantity());
                }
        );


        byte[] bytes = getBytes(wb);

        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        httpHeader.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leftovers.xlsx");
        httpHeader.setContentLength(bytes.length);

        return new HttpEntity<>(bytes, httpHeader);
    }

    private byte[] getBytes(XSSFWorkbook wb) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            try {
                wb.write(bos);

            } finally {
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private double amount(BigDecimal price) {
        return price == null ? 0 : price.doubleValue();

    }


}
