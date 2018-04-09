/**
 * 
 */
package org.aksw.simba.autoindex.datasource.sparql;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aksw.simba.autoindex.es.model.DataClass;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.request.Request;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SparqlHandler {
	
	private static final Logger log = LoggerFactory
	            .getLogger(SparqlHandler.class);

	private static final String commandString = "SELECT DISTINCT ?type ?label WHERE{?type a owl:Thing . ?type rdfs:label ?label .}" ;
	
	private static final String propertiesString = "SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v) WHERE {\n" + 
			"		?type a rdf:Property;\n" + 
			"		rdfs:label ?label.\n" + 
			"		} GROUP BY ?type ?label \n" + 
			"		 ORDER BY DESC(?v)" ;
	
	private static final String classesString = "SELECT DISTINCT ?type ?label  \n" + 
			"		WHERE {\n" + 
			"		?type a owl:Class .\n" + 
			"		?type rdfs:label ?label .\n" + 
			"		}";
	public static final Map<String, String> prefixMap;
	
	static {
		 	Map<String, String> prefix = new HashMap<String, String>();
		    	prefix.put("owl", "http://www.w3.org/2002/07/owl#");
		    	prefix.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		    	prefix.put("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		    	prefix.put("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		    	prefix.put("vrank","http://purl.org/voc/vrank#");
		    	prefixMap = Collections.unmodifiableMap(prefix);
		    };
	

	public ArrayList<DataClass> fetchClasses(Request request){
		ArrayList<DataClass> classList = null;
		String baseURI = request.getUrl(); 
		if (baseURI.isEmpty()) {
			log.warn("base URI is empty, Cannot proceed further");
			throw new IllegalArgumentException("base URI is empty");
		}
		if (!baseURI.startsWith("http://")) {
			baseURI = "http://" + baseURI; //Appending because QueryHandler appends local path otherwise
		}
		String defaultGraph = request.getDefaultGraph();
		Query query = constructSparqlQuery(baseURI , defaultGraph , 0 , classesString);
		ResultSet output = executeQuery(baseURI , query);
		classList = generateClasses(output);
		return classList;
	}
	
	public ArrayList<DataClass> generateClasses(ResultSet result){
		ArrayList<DataClass> classList = new ArrayList<DataClass>();
		while (result.hasNext()) {
			QuerySolution qs = result.next();
			DataClass dataClass = new DataClass(qs.getResource("type").getURI().toString() , qs.getLiteral("label").getString());
			classList.add(dataClass);
		}
		return classList;
	}
	
	public ArrayList<Property> fetchProperties(Request request){
		ArrayList<Property> propertyList = null;
		String baseURI = request.getUrl(); 
		if (baseURI.isEmpty()) {
			log.warn("base URI is empty, Cannot proceed further");
			throw new IllegalArgumentException("base URI is empty");
		}
		if (!baseURI.startsWith("http://")) {
			baseURI = "http://" + baseURI; //Appending because QueryHandler appends local path otherwise
		}
		String defaultGraph = request.getDefaultGraph();
		Query query = constructSparqlQuery(baseURI , defaultGraph , 0 , propertiesString);
		ResultSet output = executeQuery(baseURI , query);
		propertyList = generatePropertiesList(output);
		return propertyList;
	}
	
	public ArrayList<Property> generatePropertiesList(ResultSet result){
		ArrayList<Property> propertyList = new ArrayList<Property>();
		while (result.hasNext()) {
			QuerySolution qs = result.next();
			Property property = new Property(qs.getResource("type").getURI().toString() , qs.getLiteral("label").getString());
			propertyList.add(property);
		}
		return propertyList;
	}
	
    	public Query constructSparqlQuery(String baseURI , String defaultGraph , int limit , String commandString) {
    		ParameterizedSparqlString sparqlQueryHandler = new ParameterizedSparqlString();
    		sparqlQueryHandler.setBaseUri(baseURI);
    		sparqlQueryHandler.setNsPrefixes(prefixMap);
    		sparqlQueryHandler.setCommandText(commandString);
    		if(!defaultGraph.isEmpty()) {
    			log.warn("Overrding a new default Graph , name= " + defaultGraph);
    			sparqlQueryHandler.setIri("default-graph-uri", defaultGraph);
    		}
    		if (limit > 0) {
    			sparqlQueryHandler.append("LIMIT ");
    			sparqlQueryHandler.appendLiteral(limit);
    		}
    		Query query = QueryFactory.create(sparqlQueryHandler.asQuery());
    		log.debug("Query=" + query.toString());
    		return query;
    	}
    	
    	public ResultSet executeQuery(String baseURI , Query query) {
    		QueryExecution queryExecutionFactory = org.apache.jena.query.QueryExecutionFactory.sparqlService(baseURI , query);
    		ResultSet output = queryExecutionFactory.execSelect();
    		return output;
    	}
    	
    	public ArrayList<Entity> generateOutputEntities(ResultSet result){
    		ArrayList<Entity> entity_list = new ArrayList<Entity>();
    		while (result.hasNext()) {
    			QuerySolution qs = result.next();
    			Entity entity = new Entity(qs.getResource("type").getURI().toString() , qs.getLiteral("label").getString());
    			entity_list.add(entity);
    		}
    		return entity_list;
    	}
    	
    public ArrayList<Entity> fetchFromSparqlEndPoint(Request request) throws UnsupportedEncodingException{
    		String baseURI = request.getUrl(); 
    		if (baseURI.isEmpty()) {
    			log.warn("base URI is empty, Cannot proceed further");
    			throw new IllegalArgumentException("base URI is empty");
    		}
    		if (!baseURI.startsWith("http://")) {
    			baseURI = "http://" + baseURI; //Appending because QueryHandler appends local path otherwise
    		}
    		String defaultGraph = request.getDefaultGraph();
    		int limit = request.getlimit();
    		Query query = constructSparqlQuery(baseURI , defaultGraph , limit , commandString);
    		ResultSet output = executeQuery(baseURI , query);
    		ArrayList<Entity> entity_list = generateOutputEntities(output); 		
		return entity_list;
    } 
}
