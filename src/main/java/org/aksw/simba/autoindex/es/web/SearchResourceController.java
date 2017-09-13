package org.aksw.simba.autoindex.es.web;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SearchResourceController {

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homepage() {
		return "home";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String query() {
		return "query";
	}
	@Autowired
	EntityRespository entityRepo;

	@GetMapping(value = "/search/url/{text}")
	public List<Entity> searchName(@PathVariable final String text) {
		return entityRepo.findByUrl(text);
	}

	@GetMapping(value = "/search/label/{label}")
	public List<Entity> searchLabel(@PathVariable final String label) {
		return entityRepo.findByLabel(label);
	}

	@GetMapping(value = "/search/all")
	public List<Entity> searchAll() {
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = entityRepo.findAll();
		entities.forEach(entityList::add);
		return entityList;
	}
}
