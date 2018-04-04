package org.aksw.simba.autoindex.web;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.aksw.simba.autoindex.es.repository.EntityRepository;
import org.aksw.simba.autoindex.request.Request;
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
@RequestMapping(method = RequestMethod.POST)
public class IndexController {
	
	@Autowired
	private EntityRepository entityRepo;	
	
	@RequestMapping(value = "/index/create")
	public void indexCreate(@RequestBody final Request request) {
		
			try {
				entityRepo.createIndex(request);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
	}
	@PostMapping(value = "/index/uploadFile")
	public void indexCreateWithFileUpload(@RequestParam("file") final MultipartFile multipartFile, @RequestParam(value = "userId" , required=false) final String userId, RedirectAttributes redirectAttributes) throws IOException {
		MultipartFileHandler multipartFileHandler = new MultipartFileHandler();
		try {
			
			multipartFileHandler.store(multipartFile);
			Request request = new Request(multipartFileHandler.getFiles() , userId);
			entityRepo.createIndex(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			multipartFileHandler.deleteAllFiles();
		}
	}
	@RequestMapping(value = "index/delete")
	public void indexDelete(@RequestBody final Request request) {
		//TODO: Support Index Delete
		
	}
	
}
