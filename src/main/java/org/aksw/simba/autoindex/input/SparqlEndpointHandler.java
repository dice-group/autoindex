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

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SparqlEndpointHandler.class);

    private static final String BASE_URI = "http://dbpedia.org/sparql";
  
    public static QueryExecutionFactory makeQuery() {
    	QueryExecutionFactory qef = new QueryExecutionFactoryHttp("http://dbpedia.org/sparql", "http://dbpedia.org");
    	qef = new QueryExecutionFactoryPaginated(qef, 100);
    	return qef;
  }

    public static ResultSet getallclasses(String endpoint) {
        ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
                + "SELECT DISTINCT ?type ?label ?v \n"
                + "WHERE {\n"
                + "?type a owl:Class .\n"
                + "?type rdfs:label ?label .\n?type vrank:hasRank/vrank:rankValue ?v. \n"
                + "} Limit 500\n");
        QueryExecutionFactory qef = makeQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
        return qe.execSelect();
    }

    @SuppressWarnings("resource")
	public static ResultSet getallinstances(String endpoint) {
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
                + " ?type vrank:hasRank/vrank:rankValue ?v .\n" + "} Limit 50000 \n");
		
		// Create a QueryExecution object from a query string ...
    	QueryExecutionFactory qef = makeQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
        return qe.execSelect();
    }

    public static ResultSet getallproperties(String endpoint) {
        ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
                + "SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v)\n"
                + "WHERE {\n" + "?type a rdf:Property;\n"
                + "rdfs:label ?label.\n"
                + "}\n GROUP BY ?type ?label \n ORDER BY DESC(?v) Limit 500");

        QueryExecutionFactory qef = makeQuery();
		QueryExecution qe = qef.createQueryExecution(sparql_query.asQuery());
        return qe.execSelect();
    }

//    public ArrayList<Entity> getResults() {
//        ResultSet results = this.getallinstances("dbpedia.org/sparql");
//        ArrayList<Entity> entity_list = new ArrayList<Entity>();
//        while (results.hasNext()) {
//        	System.out.println("*****************************************");
//            QuerySolution qs = results.next();
//            entity_list.add(new Entity(qs.getResource("type").getURI(), qs
//                    .getLiteral("label").getString(), Double.parseDouble(qs
//                    .getLiteral("v").getString())));
//        }
//        System.out.println("____________________________________________");
//        return entity_list;
//    }

//    public static void main(String[] args) {
//        SparqlEndpointHandler test = new SparqlEndpointHandler();
//        test.getallclasses("http://dbpedia.org/sparql");
//    }

    public String getBaseUri() {
        return BASE_URI;
    }
}
