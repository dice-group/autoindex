package org.aksw.simba.autoindex.es.load;

import java.util.ArrayList;
import java.util.List;

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

	
	public void loadAll() {
		SparqlEndpointHandler sh = new SparqlEndpointHandler();
		operations.putMapping(Entity.class);
		System.out.println("Loading Data");
		esrepo.save(sh.getResults());
		System.out.printf("Loading Completed");

	}
	
	@PostConstruct
	@Transactional
	private List<Entity> getData() {
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(new Entity("http://dbpedia.org/resource/Antibiotics ",
				"Antibiotic", 186.916));
		entities.add(new Entity(
				"http://dbpedia.org/resource/Anti-ballistic_missile ",
				"Anti-Ballistic Missile", 2.0));
		entities.add(new Entity("http://dbpedia.org/resource/Anthropology ",
				"Antropologie", 201.737));
		return entities;
	}
}
