package org.aksw.simba.autoindex.web;

//This is a class Handling REST Interfaces. 
//This should NOT know about Elastic Search or other operations. Moving this one level up.

import java.util.List;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/search")
public class SearchResourceController {

	@Autowired
	EntityRepository entityRepo;	
	@GetMapping(value = "/url/{text}")
	public List<Entity> searchUrl(@PathVariable final String text) {
		return entityRepo.search("url", "https://" + text);
	}
	@GetMapping(value = "/label/{label}")
	public List<Entity> searchLabel(@PathVariable final String label) {
		return entityRepo.search("label",  label);
	}
	@GetMapping(value = "/all")
	public List<Entity> searchAll() {
		return entityRepo.findall();
	}
}
