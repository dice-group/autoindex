package org.aksw.simba.autoindex.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"org.aksw.*"})
public class SpringElasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticApplication.class, args);
	}
}
