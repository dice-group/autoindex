package org.aksw.simba.autoindex.es.repository;

import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.request.Request.RequestType;
import org.aksw.simba.autoindex.sparql.SparqlHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import org.springframework.stereotype.Service;
@Service
public class EntityRepository{
	
	@Autowired
	ElasticSearchRepositoryInterface ES;
	private static final Logger log = LoggerFactory
            .getLogger(EntityRepository.class);
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
	
	public void createIndex(Request request) {
		String url = request.getUrl();
		String label = request.getDefaultGraph();
		RequestType requestType = request.getRequestType();
		if (requestType.compareTo(RequestType.LOCAL_DB) == 0) {
			log.warn("Index from Database-MS, Not implemented yet");
			return;
		}
		if(label.isEmpty()) {
			//Use Default graph.
			//TODO:
		}
		String userId = request.getUserId();
		Boolean useLocalDataSource = request.isUseLocalDataSource();
		log.warn("URL=" + url + " , label=" + label + " , userId = " + userId + ", useLocalDataSource =" + useLocalDataSource);
		if(useLocalDataSource) { 
			//Read from Database-ms
			log.warn("Index from Database-MS, Not implemented yet");
			return;
		}
		SparqlHandler sparqlHandler = new SparqlHandler();
		sparqlHandler.RetrieveDataFromEndPoint(request);
	}
}