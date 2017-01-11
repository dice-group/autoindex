package org.aksw.simba.inputdata;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.dataformat.Result;
import org.aksw.simba.output.JsonLDSearialzer;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SparqlEndpointHandler {
	// TODO: Optimize queries
	public SparqlEndpointHandler() {

	}

	public ResultSet getallclasses(String endpoint) {
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

	public ResultSet getallproperties(String endpoint) {
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

	public static void main(String[] arg) {
		SparqlEndpointHandler spr = new SparqlEndpointHandler();
		ResultSet results = spr.getallinstances("http://dbpedia.org/sparql");
		List<Result> resultset = new ArrayList<Result>();
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			resultset.add(new Result(qs.getResource("type").getURI(), qs
					.getLiteral("label").getString(), Double.parseDouble(qs
					.getLiteral("v").getString())));

		}

		JsonLDSearialzer ser = new JsonLDSearialzer();
		System.out.println(ser.getInput(resultset, 100));

	}
}
