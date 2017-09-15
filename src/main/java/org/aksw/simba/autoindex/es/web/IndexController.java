package org.aksw.simba.autoindex.es.web;

import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@RequestMapping(value = "index/create")
	public void indexCreate() {
		elasticsearchTemplate.createIndex(Entity.class);
		elasticsearchTemplate.putMapping(Entity.class);
		elasticsearchTemplate.refresh(Entity.class);
	}

	@RequestMapping(value = "index/delete")
	public void indexDelete() {
		if (elasticsearchTemplate.indexExists(Entity.class)) {
			elasticsearchTemplate.deleteIndex(Entity.class);
		}

	}
}
