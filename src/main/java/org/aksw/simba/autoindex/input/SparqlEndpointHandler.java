package org.aksw.simba.autoindex.input;

import java.util.ArrayList;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.jena_sparql_api.pagination.core.QueryExecutionFactoryPaginated;
import org.aksw.simba.autoindex.es.model.Entity;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparqlEndpointHandler {
    // TODO: Optimize queries

    private static final Logger log = LoggerFactory
            .getLogger(SparqlEndpointHandler.class);

    private  final String BASE_URI = "http://dbpedia.org/sparql";

	private String lang ="en";
  
    public  String getLang() {
		return lang;
	}

	public  void setLang(String lang) {
		this.lang = lang;
	}

	public  QueryExecutionFactory makeQuery() {
    	QueryExecutionFactory qef = new QueryExecutionFactoryHttp("http://dbpedia.org/sparql", "http://dbpedia.org");
    	qef = new QueryExecutionFactoryPaginated(qef, 1);
    	return qef;
  }

    public  ResultSet getallclasses(String endpoint) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "SELECT DISTINCT ?type ?label  \n"
                + "WHERE {\n"
                + "?type a owl:Class .\n"
                + "?type rdfs:label ?label . FILTER ( lang(?label) = \""+lang +"\") \n"
                + "}\n");
        log.debug("%s" , sparql_query);
        QueryExecutionFactory qef = makeQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
		return qe.execSelect();
    }

    @SuppressWarnings("resource")
	public  ResultSet getallinstances(String endpoint, int instances_limit) {
    	String instance_limit = "";
    	if(instances_limit == 0) {
        	 instance_limit = "";
    	}
    	else {
    		 instance_limit = "limit "+instances_limit;
    	}
    	
        ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
                + "SELECT ?type ?label ?v  \n"
                + "FROM <http://dbpedia.org>  \n"
                + "FROM <http://people.aifb.kit.edu/ath/#DBpedia_PageRank> \n"
                + "WHERE {\n"
                + "?type a <http://www.w3.org/2002/07/owl#Thing> . \n"
                + " ?type <http://www.w3.org/2000/01/rdf-schema#label> ?label .\n "
                + " ?type vrank:hasRank/vrank:rankValue ?v .\n" + "} \n"+ instance_limit);
		
		// Create a QueryExecution object from a query string ...
    	QueryExecutionFactory qef = makeQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
        return qe.execSelect();
    }

    public  ResultSet getallproperties(String endpoint) {
        ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
                + "SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v)\n"
                + "WHERE {\n" + "?type a rdf:Property;\n"
                + "rdfs:label ?label.\n"
                + "FILTER (STRSTARTS(str(?type),\"http://dbpedia.org/ontology\")&&lang(?label)=\"en\")"
                + "}\n GROUP BY ?type ?label \n ORDER BY DESC(?v)");

        log.debug("%s" , sparql_query.toString());
        QueryExecutionFactory qef = makeQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
        return qe.execSelect();
    }

    public ArrayList<Entity> getResults(int instances_limit) {
    	
        ResultSet results = this.getallinstances("dbpedia.org/sparql",instances_limit);
        ArrayList<Entity> entity_list = new ArrayList<Entity>();
        while (results.hasNext()) {
            QuerySolution qs = results.next();
            entity_list.add(new Entity(qs.getResource("type").getURI(), qs
                    .getLiteral("label").getString(), Double.parseDouble(qs
                    .getLiteral("v").getString())));
        }

        return entity_list;
    }

    public String getBaseUri() {
        return BASE_URI;
    }
}
