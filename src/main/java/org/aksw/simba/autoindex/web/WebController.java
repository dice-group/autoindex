package org.aksw.simba.autoindex.web;


import java.io.IOException;

import org.aksw.simba.autoindex.datasource.sparql.SparqlHandler;
import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.request.SearchRequest;
import org.aksw.simba.autoindex.response.Response;
import org.aksw.simba.autoindex.utils.MultipartFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RestController
@RequestMapping(method = RequestMethod.POST , produces="application/json")
public class WebController {
	
	@Autowired
	private EntityRepository entityRepo;	
	
	@RequestMapping(value = "/index/create")
	public Response indexCreate(@RequestBody final Request request) {
		Response response;
		response = sendRequest(request);		
		return response;
	}
	
	@PostMapping(value = "/index/uploadFile")
	public Response indexCreateWithFileUpload(@RequestParam("file") final MultipartFile multipartFile, @RequestParam(value = "userId" , required=false) final String userId, RedirectAttributes redirectAttributes) throws IOException {
		MultipartFileHandler multipartFileHandler = new MultipartFileHandler();
		Response response;
		multipartFileHandler.store(multipartFile);
		Request request = new Request(multipartFileHandler.getFiles() , userId);
		response = sendRequest(request);
		multipartFileHandler.deleteAllFiles();	
		return response;
	}
	
	@RequestMapping(value = "index/delete")
	public void indexDelete(@RequestBody final Request request) {
		//TODO: Support Index Delete
		
	}
	
	@RequestMapping("/search")
	public Response searchUrl(@RequestBody final SearchRequest searchRequest) {
		return entityRepo.search(searchRequest);	
	}
	
	private Response sendRequest(Request request) {
		Response response;
		try {
			response = entityRepo.createIndex(request);
		} catch (IOException e) {
			response = new Response();
			response.setBoolean(false);
			e.printStackTrace();
			return response;
		}
		catch (Exception e) {
			response = new Response();
			response.setBoolean(false);
			e.printStackTrace();
			return response;
		}
		return response;
	}
	
	@RequestMapping("/getEntityQuery")
	public String getEntityQuery() {
		SparqlHandler sparqlHandler = new SparqlHandler();
		return sparqlHandler.getEntityQueryString();	
	}
	
	@RequestMapping("/getPropertyQuery")
	public String getPropertyQuery() {
		SparqlHandler sparqlHandler = new SparqlHandler();
		return sparqlHandler.getPropertyQueryString();	
	}
	
	@RequestMapping("/getClassQuery")
	public String getClassQuery() {
		SparqlHandler sparqlHandler = new SparqlHandler();
		return sparqlHandler.getClassQueryString();	
	}
}
