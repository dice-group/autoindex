package org.aksw.simba.autoindex.es.repository;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	public ElasticSearchRepositoryInterface elasticSearchRepositoryInterface;
	
	@Autowired
	public ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	public SparqlHandler sparqlHandler;
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
	
	public NativeSearchQueryBuilder createNativeSearchQueryBuilder(String query , String strCategory , String strType) {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		if("all".equals(strCategory)) {
			nativeSearchQueryBuilder.withIndices(categoryClass , categoryEntity , categoryProperty);
			nativeSearchQueryBuilder.withTypes(categoryClass , categoryEntity , categoryProperty);
		}
		else {
			nativeSearchQueryBuilder.withIndices(strCategory);
			nativeSearchQueryBuilder.withTypes(strCategory);
		}
		//Support Wild card and Fuzzy search. If Any Regular expression is contained, then it must be within /
		if(query.contains("*") || query.contains("?") || query.contains("~") || query.contains("^") || query.contains("/") ) {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(query)).withPageable(new PageRequest(0, 1000));
		}
		else if(query.contains(" OR ") || query.contains(" AND ") || query.contains(" NOT ") || query.contains(" + ") || query.contains(" - ")) { //Boolean Search
			nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(query)).withPageable(new PageRequest(0, 1000));
		}
		else if(query.contains(" TO ") && ((query.contains("[") && query.contains("]")) || (query.contains("{") && query.contains("}")))) {//Range Search
			nativeSearchQueryBuilder.withQuery(QueryBuilders.queryStringQuery(query)).withPageable(new PageRequest(0, 1000));
		}
		else if("url".equals(strType)) {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery(strType , query)).withPageable(new PageRequest(0, 1000));
		}
		else {
			nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery(strType , query).fuzziness(1).prefixLength(0).maxExpansions(2).fuzzyTranspositions(true)).withPageable(new PageRequest(0, 1000));
		}
		return nativeSearchQueryBuilder;
	}
	
	public Response search(SearchRequest searchRequest) {	
		String query = searchRequest.getQuery();
		Type type = searchRequest.getType();
		Category category = searchRequest.getCategory();
		
		if(Category.NONE.equals(category) || Type.NONE.equals(type) || query.isEmpty()) {
			throw new IllegalArgumentException("Invalid Category or type or empty Query");
		}
		String strCategory = getCategory(category);
		String strType = getType(type);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = createNativeSearchQueryBuilder(query, strCategory , strType );
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
	public Response createNewResponse() {
		Response response = new Response();
		response.setBoolean(true);
		return response;
	}
	
	public Response indexEntity(Request request) {
		//SparqlHandler sparqlHandler = new SparqlHandler();
		ArrayList<Entity> entity_list = null;
		Response response = createNewResponse();
		try {
			entity_list = sparqlHandler.fetchFromSparqlEndPoint(request);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.warn("Unsupported Encoding Exception");
			response.setBoolean(false);
			return response;
		}
	    elasticSearchRepositoryInterface.save(entity_list);
	    return response;
	}
	
	public Response handleEndPointURL(Request request) {
		Response response = indexEntity(request);
		if(response.getBoolean()) {
			log.warn("Fetch and Index Properties");
			ArrayList<Property> propertyList = sparqlHandler.fetchProperties(request);
			elasticSearchRepositoryInterface.save(propertyList);
			log.warn("Fetch and Index Classes");
			ArrayList<DataClass> classList = sparqlHandler.fetchClasses(request);
			elasticSearchRepositoryInterface.save(classList);
		}
		return response;
	}
	
	public Response handleLocalEndPoint(Request request) {
		return indexEntity(request);
	}
	
	public Response handleFile(Request request) {
		ArrayList<Entity> entity_list = null;
		Response response = createNewResponse();
		FileHandler fileHandler = new FileHandler();
		List<String> fileList = request.getFileList();
		for (String file : fileList ) {
			try {
				entity_list = fileHandler.indexInputFile(file);
			} catch (IOException e) {
				log.warn("IO Exception on RDF Files");
				response.setBoolean(false);
				return response;
			}
			elasticSearchRepositoryInterface.save(entity_list);
		}
		return response;
	}
	
	public Response handleCustomString(Request request) {
		ArrayList<Entity> entity_list = null;
		Response response = createNewResponse();
		CustomStringHandler manual_input = new CustomStringHandler();
		try {
			entity_list = manual_input.indexInput(request);
		} catch (UnsupportedEncodingException e) {
			log.warn("Unsupported Encoding Exception");
			response.setBoolean(false);
			return response;
		}
		elasticSearchRepositoryInterface.save(entity_list);
		return response;
	}
	
	public Response createIndex(Request request) throws IOException {
		Response response = new Response();
		response.setBoolean(true);
		String label = request.getDefaultGraph();
		RequestType requestType = request.getRequestType();
		String userId = request.getUserId();
		Boolean useLocalDataSource = request.isUseLocalDataSource();
		log.warn( "label=" + label + " , userId = " + userId + ", useLocalDataSource =" + useLocalDataSource);
		/*if(useLocalDataSource) { 
			//Read from Database-ms
			log.warn("Index from Database-MS, Not implemented yet");
			return response;
		}*/
		switch(requestType) {

			case URI : {
				return handleEndPointURL(request);
			}
			case RDF_FILE: {	
				return handleFile(request);
			}
			case LOCAL_DB: {
				return handleLocalEndPoint(request);
			}
			case CUSTOM_STRING: {
				return handleCustomString(request);
			}			
			default :{
				log.warn("Not implemented yet");
				response.setBoolean(false);
				return response;
			}
			
		}
		 
	}
}