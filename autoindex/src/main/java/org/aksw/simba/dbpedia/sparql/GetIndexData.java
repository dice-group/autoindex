package org.aksw.simba.dbpedia.sparql;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class GetIndexData {
	public static ResultSet getallclasses(String endpoint) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
						+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
						+ "PREFIX : <http://dbpedia.org/resource/>\n"
						+ "PREFIX dbpedia2: <http://dbpedia.org/property/>\n"
						+ "PREFIX dbpedia: <http://dbpedia.org/>\n"
						+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX type: <http://dbpedia.org/class/yago/>\n"
						+ "PREFIX prop: <http://dbpedia.org/property/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n" + "SELECT DISTINCT ?type ?label ?v \n"
						+ "WHERE {\n" + "?type a owl:Class .\n"
						+ "?type rdfs:label ?label .\n?type vrank:hasRank/vrank:rankValue ?v. \n" + "}\n");
		System.out.println(sparql_query);
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint, sparql_query.asQuery());
		return exec.execSelect();
	}

	public static ResultSet getallinstances(String endpoint) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
						+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
						+ "PREFIX : <http://dbpedia.org/resource/>\n"
						+ "PREFIX dbpedia2: <http://dbpedia.org/property/>\n"
						+ "PREFIX dbpedia: <http://dbpedia.org/>\n"
						+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX type: <http://dbpedia.org/class/yago/>\n"
						+ "PREFIX prop: <http://dbpedia.org/property/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX dbo:<http://dbpedia.org/ontology/>\n" + "SELECT ?type ?label ?v  \n"
						+ "FROM <http://dbpedia.org>  \n" + "FROM <http://people.aifb.kit.edu/ath/#DBpedia_PageRank> \n"
						+ "WHERE {\n" + "?type a <http://www.w3.org/2002/07/owl#Thing> . \n"
						+ " ?type <http://www.w3.org/2000/01/rdf-schema#label> ?label .\n "
						+ " ?type vrank:hasRank/vrank:rankValue ?v .\n" + "}\n");
		System.out.println(sparql_query);
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint, sparql_query.asQuery());
		return exec.execSelect();
	}

	public static ResultSet getallproperties(String endpoint) {
		ParameterizedSparqlString sparql_query = new ParameterizedSparqlString(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
						+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
						+ "PREFIX : <http://dbpedia.org/resource/>\n"
						+ "PREFIX dbpedia2: <http://dbpedia.org/property/>\n"
						+ "PREFIX dbpedia: <http://dbpedia.org/>\n"
						+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX type: <http://dbpedia.org/class/yago/>\n"
						+ "PREFIX prop: <http://dbpedia.org/property/>\n"
						+ "PREFIX vrank:<http://purl.org/voc/vrank#>\n"
						+ "SELECT DISTINCT ?type ?label  (COUNT(*)AS ?v)\n" + "WHERE {\n" + "?type a rdf:Property;\n"
						+ "rdfs:label ?label.\n" + "}\n GROUP BY ?type ?label \n ORDER BY DESC(?v)");

		System.out.println(sparql_query);
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint, sparql_query.asQuery());
		return exec.execSelect();
	}

	public static void main(String[] arg) {

		ResultSet results = getallclasses("http://dbpedia.org/sparql");
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			String url = qs.getResource("type").getURI();
			String label = qs.getLiteral("label").getString();
			String pagerank = qs.getLiteral("v").getString();
			System.out.println(url + "  --------");
			System.out.print(label + "  --------");
			System.out.println(pagerank);
			System.out.println("_____________________________________________");

		}

	}
}