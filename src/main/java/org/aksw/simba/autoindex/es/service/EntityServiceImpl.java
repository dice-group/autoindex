package org.aksw.simba.autoindex.es.service;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.repository.EntityRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class EntityServiceImpl implements EntityService {
	private final EntityRespository entityRepo;

	@Autowired
	public EntityServiceImpl(EntityRespository articleRepository) {
		this.entityRepo = articleRepository;

	}

	@Override
	public Entity save(Entity article) {
		return entityRepo.save(article);
	}

	@Override
	public Entity findOne(String id) {
		return entityRepo.findOne(id);
	}

	@Override
	public Iterable<Entity> findAll() {
		return entityRepo.findAll();
	}

	@Override
	public Page<Entity> findByUrl(String name, Pageable pageable) {
		return entityRepo.findByUrl(name, pageable);
	}

	@Override
	public Page<Entity> findByLabel(String name, Pageable pageable) {
		return entityRepo.findByLabel(name, pageable);
	}

	@Override
	public long count() {
		return entityRepo.count();
	}

	@Override
	public void delete(Entity article) {
		entityRepo.delete(article);
	}
}
