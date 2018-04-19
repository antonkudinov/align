package ru.akudinov.test;

import io.jaegertracing.Configuration;
import io.jaegertracing.samplers.ProbabilisticSampler;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan
public class RestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new RestApplication()
				.configure(new SpringApplicationBuilder(RestApplication.class))
				.run(args);
	}


	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		return new Configuration("spring-boot", new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
				new Configuration.ReporterConfiguration())
				.getTracer();
	}

}
