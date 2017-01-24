package org.aksw.simba.inputdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.RDFOutput;

public class SparqlEndpointHandler {
	// TODO: Optimize queries
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SparqlEndpointHandler.class);

	public SparqlEndpointHandler() {

	}

	private static final String BASE_URI = "http://dbpedia.org/sparql";

	public ResultSet getAllClasses(String endpoint) {
		LOGGER.info("Getting all Classes ");
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
						+ "}\n");
		System.out.println(sparql_query);
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		return exec.execSelect();
	}

	public ResultSet getallinstances(String endpoint) {
		LOGGER.info("Getting all Instances ");
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
						+ " ?type vrank:hasRank/vrank:rankValue ?v .\n" + "}\n");
		System.out.println(sparql_query);
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		return exec.execSelect();
	}

	public ResultSet getAllProperties(String endpoint) {
		LOGGER.info("Getting all Properties ");
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v)\n"
						+ "WHERE {\n" + "?type a rdf:Property;\n"
						+ "rdfs:label ?label.\n"
						+ "}\n GROUP BY ?type ?label \n ORDER BY DESC(?v)");
		System.out.println(sparql_query);
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());

		return exec.execSelect();
	}

	public void getInputData(String endpointUrl) {
		SparqlEndpointHandler spr = new SparqlEndpointHandler();
		LOGGER.info("Connecting to repository " + endpointUrl);
		ResultSet results = spr.getallinstances(endpointUrl);
		RDFOutput rout = new RDFOutput();
		Model result = rout.asModel(results);
		result.write(System.out, "TURTLE");
	}

	public static void main(String[] args) {
		SparqlEndpointHandler spr = new SparqlEndpointHandler();
		spr.getInputData(BASE_URI);

	}
}
