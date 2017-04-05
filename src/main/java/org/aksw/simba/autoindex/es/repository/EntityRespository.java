package org.aksw.simba.autoindex.es.repository;

import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRespository extends
		ElasticsearchRepository<Entity, String> {

	Page<Entity> findByUrl(String url, Pageable pageable);

	Page<Entity> findByLabel(String label,
			Pageable pageable);
}