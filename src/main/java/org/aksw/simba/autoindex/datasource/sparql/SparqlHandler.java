/**
 * 
 */
package org.aksw.simba.autoindex.datasource.sparql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

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
import org.springframework.stereotype.Component;

@Component
public class SparqlHandler {
	
	private static final Logger log = LoggerFactory
	            .getLogger(SparqlHandler.class);
	private static  String commandString = "" ; //Read from properties file.
	private static  String propertiesString = "" ;
	private static  String classesString = "";
	public static  Map<String, String> prefixMap;
	private static final String TEMPLATE_FILE = "src/main/resources/application.properties";
	private Properties properties = new Properties();
	
	@PostConstruct
	private void resourceLoader() throws FileNotFoundException {
		InputStream input = null;
		try {
			 input = new FileInputStream(TEMPLATE_FILE);
			 properties.load(input);
			 commandString = properties.getProperty("entity.whereclause");
			 propertiesString = properties.getProperty("property.whereclause");
			 classesString = properties.getProperty("class.whereclause");
			 int i=1;
			 Map<String, String> prefix = new HashMap<String, String>();
			 while(properties.containsKey("prefix" + i + ".name")) {
				 prefix.put(properties.getProperty("prefix" + i + ".name") , properties.getProperty("prefix" + i + ".url"));
				 ++i;
			 }
			 prefixMap = Collections.unmodifiableMap(prefix);
			 System.out.println(prefixMap.toString());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}	
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
