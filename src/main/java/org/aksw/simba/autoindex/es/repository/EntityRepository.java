package org.aksw.simba.autoindex.es.repository;

import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.datasource.file.FileHandler;
import org.aksw.simba.autoindex.datasource.sparql.SparqlHandler;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.request.Request.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
@Service
public class EntityRepository{
	
	@Autowired
	ElasticSearchRepositoryInterface elasticSearchRepositoryInterface;
	private static final Logger log = LoggerFactory
            .getLogger(EntityRepository.class);
	public void save(ArrayList<Entity> list) {
		elasticSearchRepositoryInterface.save(list);
	}
	public List<Entity> search(String type, String label) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(prefixQuery("label", label)).build();
		return elasticSearchRepositoryInterface.search(searchQuery).getContent();
		
	}
	public List<Entity> findall(){
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = elasticSearchRepositoryInterface.findAll();
		entities.forEach(entityList::add);
		return entityList;
	}
	
	public void createIndex(Request request) throws IOException {
		String url = request.getUrl();
		String label = request.getDefaultGraph();
		RequestType requestType = request.getRequestType();
		String userId = request.getUserId();
		Boolean useLocalDataSource = request.isUseLocalDataSource();
		log.warn("URL=" + url + " , label=" + label + " , userId = " + userId + ", useLocalDataSource =" + useLocalDataSource);
		if(useLocalDataSource) { 
			//Read from Database-ms
			log.warn("Index from Database-MS, Not implemented yet");
			return;
		}
		ArrayList<Entity> entity_list = null;
		switch(requestType) {
			case URI : {
				SparqlHandler sparqlHandler = new SparqlHandler();
				entity_list = sparqlHandler.fetchFromSparqlEndPoint(request);
				elasticSearchRepositoryInterface.save(entity_list);
				System.out.println("After ES Save");
				return;
			}
			case RDF_FILE: {
				FileHandler fileHandler = new FileHandler();
				List<String> fileList = request.getFileList();
				for (String file : fileList ) {
					entity_list = fileHandler.indexInputFile(file);
					elasticSearchRepositoryInterface.save(entity_list);
				}
				return;
			}
			case LOCAL_DB: {
				log.warn("Index from Database-MS, Not implemented yet");
				return;
			}
			default :{
				log.warn("Not implemented yet");
				return;
			}
			
		}
		 
	}
}