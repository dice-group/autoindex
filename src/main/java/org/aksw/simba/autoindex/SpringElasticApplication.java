package org.aksw.simba.autoindex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"org.aksw.simba.autoindex.es.repository" ,  "org.aksw.simba.autoindex.web" , "org.aksw.simba.autoindex.datasource.sparql"})
@EnableJpaRepositories("ElasticsearchRepository")
public class SpringElasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticApplication.class, args);
	}
}
