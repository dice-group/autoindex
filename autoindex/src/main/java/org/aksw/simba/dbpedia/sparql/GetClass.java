package org.aksw.simba.dbpedia.sparql;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class GetClass {
	public static ResultSet getallclasses() {
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

						// + "" + "\n"

						+ "SELECT DISTINCT ?type ?label ?v \n" + "WHERE {\n" + "?type a owl:Class .\n"
						+ "?type rdfs:label ?label .\n?type vrank:hasRank/vrank:rankValue ?v. \n" + "}\n");

		String ontology_service = "http://dbpedia.org/sparql";
		String endpoint = "DBpedia";
		System.out.println(sparql_query);

		QueryExecution exec = QueryExecutionFactory.sparqlService(ontology_service, sparql_query.asQuery());

		ResultSet results = ResultSetFactory.copyResults(exec.execSelect());

		return results;
	}

	// System.out.println(results.next().get("resource"));
	public static void main(String[] args) {

		ResultSet results = getallclasses();
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
		// ResultSetFormatter.out(results);

	}
}