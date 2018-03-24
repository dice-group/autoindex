package org.aksw.simba.autoindex.web;

//This is a class Handling REST Interfaces. 
//This should NOT know about Elastic Search or other operations. Moving this one level up.

import java.util.List;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(method = RequestMethod.POST)
public class SearchResourceController {
	
	@Autowired
	EntityRepository entityRepo;	
	
	@RequestMapping("/searchByURL")
	public List<Entity> searchUrl(@RequestBody final String url) {
		return entityRepo.search("url",  url);
	}
	
	@RequestMapping("/searchByLabel")
	public List<Entity> searchLabel(@RequestBody final String label) {
		return entityRepo.search("label",  label);
	}
	@RequestMapping("/findAll")
	public List<Entity> searchAll() {
		return entityRepo.findall();
	}
}
