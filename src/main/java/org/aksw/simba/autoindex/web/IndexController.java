package org.aksw.simba.autoindex.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.aksw.simba.autoindex.request.Request;


@RestController
@RequestMapping(method = RequestMethod.POST)
public class IndexController {
	
	@Autowired
	private EntityRepository entityRepo;	
	
	@RequestMapping(value = "/index/create")
	public void indexCreate(@RequestBody final Request request) {
		try {
			entityRepo.createIndex(request);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "index/delete")
	public void indexDelete(@RequestBody final Request request) {
		//TODO: Support Index Delete
		
	}
}
