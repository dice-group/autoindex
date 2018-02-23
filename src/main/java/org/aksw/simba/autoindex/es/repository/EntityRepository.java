package org.aksw.simba.autoindex.es.repository;

import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;
import java.util.ArrayList;
import java.util.List;
import org.aksw.simba.autoindex.es.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import org.springframework.stereotype.Service;
@Service
public class EntityRepository{
	
	EntityRepository(){
		
	}	
	@Autowired
	ElasticSearchRepositoryInterface ES;
	
	public void save(ArrayList<Entity> list) {
		ES.save(list);
	}
	public List<Entity> search(String type, String label) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(prefixQuery("label", label)).build();
		return ES.search(searchQuery).getContent();
		
	}
	public List<Entity> findall(){
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = ES.findAll();
		entities.forEach(entityList::add);
		return entityList;
	}
}