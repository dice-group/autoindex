package org.aksw.simba.autoindex.es.load;

import javax.annotation.PostConstruct;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.aksw.simba.autoindex.input.SparqlEndpointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Loader {

	@Autowired
	ElasticsearchOperations operations;

	@Autowired
	EntityRespository esrepo;

	@PostConstruct
	@Transactional
	public void loadAll() {
		SparqlEndpointHandler sh = new SparqlEndpointHandler();
		operations.putMapping(Entity.class);
		System.out.println("*************************************************Loading Data*************************************************");
		esrepo.save(sh.getResults());
//		System.out.printf(sh.toString());
		System.out.println("*************************************************Loading Completed*************************************************");
	}

}
