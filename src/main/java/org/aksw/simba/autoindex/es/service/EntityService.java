package org.aksw.simba.autoindex.es.service;

import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityService {

	Entity save(Entity rdfUnit);

	Entity findOne(String id);

	Iterable<Entity> findAll();

	Page<Entity> findByUrl(String name, Pageable pageable);

	Page<Entity> findByLabel(String name, Pageable pageable);

	long count();

	void delete(Entity rdfUnit);

}
