package org.aksw.simba.autoindex.es.web;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;
import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/search")
public class SearchResource {

	@Autowired
	EntityRespository entityRepo;

	@GetMapping(value = "/url/{text}")
	public List<Entity> searchUrl(@PathVariable final String text) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(wildcardQuery("url", "https://*" + text))
				.build();

		return entityRepo.search(searchQuery).getContent();
	}

	@GetMapping(value = "/label/{label}")
	public List<Entity> searchLabel(@PathVariable final String label) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(prefixQuery("label", label)).build();
		return entityRepo.search(searchQuery).getContent();
	}

	@GetMapping(value = "/all")
	public List<Entity> searchAll() {
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = entityRepo.findAll();
		entities.forEach(entityList::add);
		return entityList;
	}
}
