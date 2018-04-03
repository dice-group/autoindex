/**
 * 
 */
package org.aksw.simba.autoindex.sparql;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aksw.simba.autoindex.es.model.Entity;
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
	/*SELECT DISTINCT ?type ?label WHERE {
	?type a <http://www.w3.org/2002/07/owl#Thing> .
		?type <http://www.w3.org/2000/01/rdf-schema#label> ?label .
		} LIMIT 1000*/
	
	/* Better query
	 * SELECT DISTINCT ?URI ?label WHERE {
		?URI <http://www.w3.org/2000/01/rdf-schema#label> ?label .
		} LIMIT 100
	*/
	private static final String commandString = "SELECT DISTINCT ?URI ?label WHERE {\n" + 
			"?URI <http://www.w3.org/2000/01/rdf-schema#label> ?label .\n" + 
			"} ";
	private static final Map<String,String> prefixMap = Collections.unmodifiableMap(new HashMap<String,String>(){/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	{
		put("owl", "http://www.w3.org/2002/07/owl#");
        put("xsd", "http://www.w3.org/2001/XMLSchema#");
        put("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        put("rdfs","http://www.w3.org/2000/01/rdf-schema#");
        put("vrank","http://purl.org/voc/vrank#");
	}});
/*	
	private static int maxCount = 1000;
	private String lang = "en"; //Explore idea to not keep it open as a string since it may lead to some junk values.
	private String baseURI = "http://dbpedia.org/sparql";
	private String graphName = "";
	private String filterName = "http://dbpedia.org/ontology"; //Temporary till getAllProperties is figured out.
	private ArrayList<String> endpoints;
	public SparqlHandler() {
		this.endpoints = new ArrayList<String>();
		this.endpoints.add("http://dbpedia.org");
		this.endpoints.add("http://people.aifb.kit.edu/ath/#DBpedia_PageRank");
	}
	public  String getLang() {
		return lang;
	}
	public  void setLang(String lang) {
		this.lang = lang;
	}
    public String getBaseUri() {
        return baseURI;
    }
    public void setBaseUri(String baseURI) {
    		this.baseURI = baseURI;
    }
    public String getGraphName() {
        return graphName;
    }
    public void setGraphName(String graphName) {
    		this.graphName = graphName;
    }
    public void addEndPoint(String endPoint) {
    		this.endpoints.add(endPoint);
    }
    public void removeEndPoints() {
    		this.endpoints.clear();
    }
    public String getFilter() {
        return filterName;
    }
    public void setFilter(String filterName) {
    		this.filterName = filterName;
    }
    private  QueryExecutionFactory createQuery() {
	    	QueryExecutionFactory qef = new QueryExecutionFactoryHttp(this.baseURI , this.baseURI);
	    	qef = new QueryExecutionFactoryPaginated(qef, maxCount);
	    	return qef;
    }
   
    private ResultSet runQuery(ParameterizedSparqlString sparql_query) {
    		QueryExecutionFactory qef = createQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
        return qe.execSelect();
    }*/
    /*
     * PREFIX owl: <http://www.w3.org/2002/07/owl#> 
     * PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
     * PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
     * SELECT DISTINCT ?type ?label WHERE {?type a owl:Class .?type rdfs:label ?label . 
     * FILTER ( lang(?label) = "en")
     */
   /* public  ResultSet getallclasses() {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "SELECT DISTINCT ?type ?label  "
                + "WHERE {"
                + "?type a owl:Class ."
                + "?type rdfs:label ?label . FILTER ( lang(?label) = \"" + lang +"\") "
                + "}");
        log.debug("%s" , sparql_query);
        return runQuery(sparql_query);
    }*/
    /*
     * PREFIX owl: <http://www.w3.org/2002/07/owl#>
		PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
		PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
		PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
		PREFIX vrank:<http://purl.org/voc/vrank#>
		SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v)
		WHERE {?type a rdf:Property;
		rdfs:label ?label.
		FILTER (STRSTARTS(str(?type),"http://dbpedia.org/ontology")&&lang(?label)="en")
		}GROUP BY ?type ?label ORDER BY DESC(?v)
     */
    /*public  ResultSet getallproperties() {
    		//TODO: Hardcoding of DBPedia is available below. Need to find if this function is useful Else Add a filter parameter.
        ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX vrank:<http://purl.org/voc/vrank#>"
                + "SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v)"
                + "WHERE {?type a rdf:Property;"
                + "rdfs:label ?label."
                + "FILTER (STRSTARTS(str(?type),\"" + this.filterName + "\") &&lang(?label)=\"en\")"
                + "} GROUP BY ?type ?label ORDER BY DESC(?v)");

        log.debug("%s" , sparql_query.toString());
        return runQuery(sparql_query);
    }
    public  ResultSet getallinstances(int instances_limit) {
	    	String instance_limit = "";
	    	if(instances_limit == 0) {
	        	 instance_limit = "";
	    	}
	    	else {
	    		 instance_limit = "limit "+instances_limit;
	    	}
	    	String sparqlString = "";
	    sparqlString +=
        "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
        + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
        + "PREFIX vrank:<http://purl.org/voc/vrank#>"
        + "SELECT ?type ?label ?v  ";
        for(int i=0 ; i < endpoints.size() ; ++i) {
        		sparqlString += "FROM <" + endpoints.get(i) + "> ";
        }
        	sparqlString += "WHERE {"
        + "?type a <http://www.w3.org/2002/07/owl#Thing> . "
        + " ?type <http://www.w3.org/2000/01/rdf-schema#label> ?label . "
        + " ?type vrank:hasRank/vrank:rankValue ?v ." + "} "+ instance_limit;
        	log.debug(sparqlString);

        ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(sparqlString);
	    	return runQuery(sparql_query);
    }
    
    public ArrayList<Entity> getResults(int instances_limit) {
        ResultSet results = this.getallinstances(instances_limit);
        ArrayList<Entity> entity_list = new ArrayList<Entity>();
        while (results.hasNext()) {
            QuerySolution qs = results.next();
            entity_list.add(new Entity(qs.getResource("type").getURI(), qs
                    .getLiteral("label").getString(), Double.parseDouble(qs
                    .getLiteral("v").getString())));
        }
      return entity_list;
    }*/
    /*
     * Below Functions will be in use.
     * All Functions above will be removed once the Unit tests are fixed.
     * 
     */
    	public Query constructSparqlQuery(String baseURI , String defaultGraph , int limit) {
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
    			Entity entity = new Entity(qs.getResource("URI").getURI().toString() , qs.getLiteral("label").getString());
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
    		Query query = constructSparqlQuery(baseURI , defaultGraph , limit);
    		ResultSet output = executeQuery(baseURI , query);
    		ArrayList<Entity> entity_list = generateOutputEntities(output); 		
		return entity_list;
    	
    }
   
}
