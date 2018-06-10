package org.aksw.simba.autoindex.es.repository;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.datasource.custom.CustomStringHandler;
import org.aksw.simba.autoindex.datasource.file.FileHandler;
import org.aksw.simba.autoindex.datasource.sparql.SparqlHandler;
import org.aksw.simba.autoindex.es.model.DataClass;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.request.Request.RequestType;
import org.aksw.simba.autoindex.request.SearchRequest;
import org.aksw.simba.autoindex.request.SearchRequest.Category;
import org.aksw.simba.autoindex.request.SearchRequest.Type;
import org.aksw.simba.autoindex.response.Bindings;
import org.aksw.simba.autoindex.response.Head;
import org.aksw.simba.autoindex.response.Response;
import org.aksw.simba.autoindex.response.Results;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class EntityRepository{
	
	private static final String categoryClass = "class";
	private static final String categoryProperty = "property";
	private static final String categoryEntity = "entity";
	
	@Autowired
	ElasticSearchRepositoryInterface elasticSearchRepositoryInterface;
	
	@Autowired
	public ElasticsearchTemplate elasticsearchTemplate;
	
	private static final Logger log = LoggerFactory
            .getLogger(EntityRepository.class);
	
	public void save(ArrayList<Entity> list) {
		elasticSearchRepositoryInterface.save(list);
	}
	
	public Response addHead(Response response) {
		Head head = response.getHead();
		head.add("label");
		head.add("uri");
		response.setHead(head);
		return response;
	}
	
	public Response createResponse(List<Entity> entityList) {
		Response response=new Response();
		response.setBoolean(true);
		response = addHead(response);
		
		Results results = response.getResults();
		log.warn("Length of output List=" + entityList.size());
		for (Entity entity : entityList) {
			Bindings bindings = new Bindings();
			bindings.setLabel(entity.getLabel());
			bindings.setUri(entity.getUrl());
			results.addBinding(bindings);
		}
		response.setResults(results);
		return response;
	}
	
	public String getCategory(Category category) {
		String returnString = "";
		switch(category) {
		case CLASS:{
			return categoryClass;
		}
		case PROPERTY :{
			return categoryProperty;
		}
		case ENTITY :{
			return categoryEntity;
		}
		case ALL :{
			return "all";
		}
		default:
			break;
		}
		return returnString;
	}
	
	public String getType(Type type) {
		String strType = "";
		switch(type) {
		case LABEL:{
			strType = "label";
			break;
		}
		case URI:{
			strType = "url";
			break;
		}
		default:
			break;
		}
		return strType;
	}
	
	public Response search(SearchRequest searchRequest) {	
		//TODO: Add exception Handling and generate Output Response accordingly. 
		//JSON must be sent at all times and not a 500 error
		String query = searchRequest.getQuery();
		Type type = searchRequest.getType();
		Category category = searchRequest.getCategory();
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		if(Category.NONE.equals(category) || Type.NONE.equals(type) || query.isEmpty()) {
			throw new IllegalArgumentException("Invalid Category or type or empty Query");
		}
		String strCategory = getCategory(category); 
		if("all".equals(strCategory)) {
			nativeSearchQueryBuilder.withIndices("class" , "entity" , "property");
			nativeSearchQueryBuilder.withTypes("class" , "entity" , "property");
		}
		else {
			nativeSearchQueryBuilder.withIndices(strCategory);
			nativeSearchQueryBuilder.withTypes(strCategory);
		}

		
		String strType = getType(type);
		if(query.contains("*") || query.contains("?") ) {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(query)).withPageable(new PageRequest(0, 1000));
		}
		else if(Type.URI.equals(type)) {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery(strType , query)).withPageable(new PageRequest(0, 1000));
		}
		else {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery(strType , query).fuzziness(1).prefixLength(0).maxExpansions(2)).withPageable(new PageRequest(0, 1000));
		}
		SearchQuery searchQuery = nativeSearchQueryBuilder.build();
		List<Entity> entityList = elasticSearchRepositoryInterface.search(searchQuery).getContent();
		return createResponse(entityList);
	}
	
	public List<Entity> findall(){
		List<Entity> entityList = new ArrayList<>();
		Iterable<Entity> entities = elasticSearchRepositoryInterface.findAll();
		entities.forEach(entityList::add);
		return entityList;
	}
	
	public Response createIndex(Request request) throws IOException {
		Response response = new Response();
		response.setBoolean(true);
		String url = request.getUrl();
		String label = request.getDefaultGraph();
		RequestType requestType = request.getRequestType();
		String userId = request.getUserId();
		Boolean useLocalDataSource = request.isUseLocalDataSource();
		log.warn("URL=" + url + " , label=" + label + " , userId = " + userId + ", useLocalDataSource =" + useLocalDataSource);
		if(useLocalDataSource) { 
			//Read from Database-ms
			log.warn("Index from Database-MS, Not implemented yet");
			return response;
		}
		ArrayList<Entity> entity_list = null;

		switch(requestType) {

			case URI : {
				SparqlHandler sparqlHandler = new SparqlHandler();
				entity_list = sparqlHandler.fetchFromSparqlEndPoint(request);
			    elasticSearchRepositoryInterface.save(entity_list);
				log.warn("Fetch and Index Properties");
				ArrayList<Property> propertyList = sparqlHandler.fetchProperties(request);
				elasticSearchRepositoryInterface.save(propertyList);
				//Index Property
				log.warn("Fetch and Index Classes");
				ArrayList<DataClass> classList = sparqlHandler.fetchClasses(request);
				elasticSearchRepositoryInterface.save(classList);
				return response;
			}
			case RDF_FILE: {
				FileHandler fileHandler = new FileHandler();
				List<String> fileList = request.getFileList();
				for (String file : fileList ) {
					entity_list = fileHandler.indexInputFile(file);
					elasticSearchRepositoryInterface.save(entity_list);
				}
				return response;
			}
			case LOCAL_DB: {
				log.warn("Index from Database-MS, Not implemented yet");
				response.setBoolean(false);
				return response;
			}
			case CUSTOM_STRING: {
				CustomStringHandler manual_input = new CustomStringHandler();
				entity_list = manual_input.indexInput(request);
				elasticSearchRepositoryInterface.save(entity_list);
				return response;
			}
			
			default :{
				log.warn("Not implemented yet");
				response.setBoolean(false);
				return response;
			}
			
		}
		 
	}
}