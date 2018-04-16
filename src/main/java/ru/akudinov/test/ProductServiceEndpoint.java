package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.akudinov.test.model.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Path("/product")
@Slf4j
/**
 * Endpoint for loan web service
 */
public class ProductServiceEndpoint {
	private final IProductService productService;

	@Autowired
	public ProductServiceEndpoint(IProductService loanService) {
		this.productService = loanService;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("create")
	public Response create(Product loan) {
		return Response.ok(productService.create(loan)).build();
	}

	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	@Path("update")
	public Response update(Product loan) {
		return Response.ok(productService.update(loan)).build();
	}

	@DELETE
	@Consumes("application/json")
	@Produces("application/json")
	@Path("update")
	public Response delete(Product loan) {
		productService.delete(loan);
		return Response.ok().build();
	}


	@GET
	@Produces("application/json")
	@Path("list")
	public Response list(@QueryParam("name") String name, @QueryParam("brand") String brand) {
		List<Product> res =  name != null ? productService.listByName(name)
				           : brand != null ? productService.listByBrand(brand)
				           : productService.list();
		return Response.ok(res).build();

	}

}
