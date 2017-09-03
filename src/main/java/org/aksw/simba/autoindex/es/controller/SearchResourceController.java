package org.aksw.simba.autoindex.es.controller;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/search")
public class SearchResourceController {

	@Autowired
	EntityRespository entityRepo;

	@GetMapping(value = "/url/{text}")
	public List<Entity> searchName(@PathVariable final String text) {
		return entityRepo.findByUrl(text);
	}

	@GetMapping(value = "/label/{label}")
	public List<Entity> searchSalary(@PathVariable final String label) {
		return entityRepo.findByLabel(label);
	}

	@GetMapping(value = "/all")
	public List<Entity> searchAll() {
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = entityRepo.findAll();
		entities.forEach(entityList::add);
		return entityList;
	}
}
