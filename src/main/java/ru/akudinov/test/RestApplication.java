package ru.akudinov.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new RestApplication()
				.configure(new SpringApplicationBuilder(RestApplication.class))
				.run(args);
	}

}
