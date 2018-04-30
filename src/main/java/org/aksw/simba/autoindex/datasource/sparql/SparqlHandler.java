/**
 * 
 */
package org.aksw.simba.autoindex.datasource.sparql;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.aksw.simba.autoindex.es.model.DataClass;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.request.Keys;
import org.aksw.simba.autoindex.request.Request;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Component
public class SparqlHandler {
	
	private static final Logger log = LoggerFactory
	            .getLogger(SparqlHandler.class);

	private static final String commandString = "SELECT DISTINCT ?key1 ?key2 WHERE{?key1 a ?s1 . ?key1 ?s2 ?key2 .}" ;
	
	private static final String propertiesString = "SELECT DISTINCT ?key1 ?key2  (COUNT(*)AS ?v) WHERE {\n" + 
			"		?key1 a rdf:Property;\n" + 
			"		?s2 ?key2.\n" + 
			"		} GROUP BY ?key1 ?key2 \n" + 
			"		 ORDER BY DESC(?v)" ;
	
	private static final String classesString = "SELECT DISTINCT ?key1 ?key2  \n" + 
			"		WHERE {\n" + 
			"		?key1 a owl:Class .\n" + 
			"		?key1 ?s2 ?key2 .\n" + 
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
	
	private static final String TEMPLATE_FILE = "src/main/resources/properties/properties.yml";
	
	private  static String key1 ;

	private static String key2; 
	//Used in addition of @PropertySource

	@PostConstruct
	private void resourceLoader() {
		try {
			
			String yamlTemplateContents = new String(Files.readAllBytes(Paths.get(TEMPLATE_FILE)));
			ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
			Keys keys = new Keys();
			keys = yamlReader.readValue(yamlTemplateContents, Keys.class);
			
			key1 = keys.getFirstKey();
			key2 = keys.getSecondKey();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("resourceLoader, IO Exception while parsing YAML template,Stack Trace=" + e.getMessage());
			e.printStackTrace();
		}
	}
	
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
			DataClass dataClass = new DataClass(qs.getResource("key1").getURI().toString() , qs.getLiteral("key2").getString());
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
			Property property = new Property(qs.getResource("key1").getURI().toString() , qs.getLiteral("key2").getString());
			propertyList.add(property);
		}
		return propertyList;
	}
	
    	public Query constructSparqlQuery(String baseURI , String defaultGraph , int limit , String commandString) {
    		ParameterizedSparqlString sparqlQueryHandler = new ParameterizedSparqlString();
    		sparqlQueryHandler.setBaseUri(baseURI);
    		sparqlQueryHandler.setNsPrefixes(prefixMap);
    		//TODO: Find a way to handle this by ParameterizedSparql String. 
    		// Doesnt work with SetLiteral or SetParam(Node) or setIRI. Try other options
    		String commandText = commandString;
    		commandText= commandText.replace("?s1", key1);
    		commandText=commandText.replace("?s2", key2);
    		sparqlQueryHandler.setCommandText(commandText);
    		
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
    			Entity entity = new Entity(qs.getResource("key1").getURI().toString() , qs.getLiteral("key2").getString());
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
