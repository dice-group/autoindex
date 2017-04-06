package org.aksw.simba.autoindex.es.repository;

import java.util.List;

import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EntityRespository extends
		ElasticsearchRepository<Entity, String> {

	List<Entity> findByUrl(String url);

	List<Entity> findByLabel(String label);
}