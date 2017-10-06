package org.aksw.simba.autoindex.es.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

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

	/*
	 * @RequestMapping(value = "/query/url", method = RequestMethod.POST) public
	 * String searchName(Model model, HttpServletRequest request) {
	 * model.addAttribute("entities", new
	 * Gson().toJson(entityRepo.findByUrl(request.getParameter("urlQuery"))));
	 * return "results"; }
	 */

	@RequestMapping(value = "/query/url/{text}")
	public String searchName(Model model, @PathVariable final String text) {
		model.addAttribute("entities", new Gson().toJson(entityRepo.findByUrl(text)));
		return "results";
	}

	@RequestMapping(value = "/query/label", method = RequestMethod.POST)
	public String searchLabel(Model model, HttpServletRequest request) {
		model.addAttribute("entities", new Gson().toJson(entityRepo.findByLabel(request.getParameter("labelQuery"))));
		return "results";
	}

	@RequestMapping(value = "/query/all", method = RequestMethod.GET)
	public String searchAll(Model model) {
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = entityRepo.findAll();
		entities.forEach(entityList::add);
		model.addAttribute("entities", new Gson().toJson(entityList));
		return "results";
	}
}
