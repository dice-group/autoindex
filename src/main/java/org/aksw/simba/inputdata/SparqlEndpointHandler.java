package org.aksw.simba.inputdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.RDFOutput;

public class SparqlEndpointHandler {
	// TODO: Optimize queries
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SparqlEndpointHandler.class);

	private static final String BASE_URI = "http://dbpedia.org/sparql";

	public void getAllClasses(String endpoint) {
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

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		RDFOutput rout = new RDFOutput();

		try {
			this.generateInputFile("Input Data" + File.separator + "Class"
					+ File.separator + "allclass-nif.ttl",
					rout.asModel(exec.execSelect()));
		} catch (FileNotFoundException e) {
			LOGGER.error("Cannot find Class file!!!");
			e.printStackTrace();
		}
	}

	public void getallInstances(String endpoint) {
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

		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		RDFOutput rout = new RDFOutput();

		try {
			this.generateInputFile("Input Data" + File.separator + "Instances"
					+ File.separator + "allInstances-nif.ttl",
					rout.asModel(exec.execSelect()));
		} catch (FileNotFoundException e) {
			LOGGER.error("Cannot find Instance file!!!");
			e.printStackTrace();
		}

	}

	public void getAllProperties(String endpoint) {
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
		QueryExecution exec = QueryExecutionFactory.sparqlService(endpoint,
				sparql_query.asQuery());
		RDFOutput rout = new RDFOutput();
		try {
			this.generateInputFile("Input Data" + File.separator + "Properties"
					+ File.separator + "allProperties-nif.ttl",
					rout.asModel(exec.execSelect()));
		} catch (FileNotFoundException e) {
			LOGGER.error("Cannot find Properties file!!!");
			e.printStackTrace();
		}

	}

	public void generateInputFile(String path, Model nifModel)
			throws FileNotFoundException {
		File resultfile = new File(path);
		if (!resultfile.exists()) {
			resultfile.getParentFile().mkdirs();
			try {
				resultfile.createNewFile();
			} catch (IOException e) {
				LOGGER.error("Cannot create new file");
				e.printStackTrace();
			}
		}
		FileOutputStream fout = new FileOutputStream(resultfile);
		try {
			fout.flush();
		} catch (IOException e) {
			LOGGER.error("Cannot Flush the stream");
			e.printStackTrace();
		}
		nifModel.write(fout, "TTL");
		try {
			fout.close();
		} catch (IOException e) {
			LOGGER.error("Cannot Close the stream");
			e.printStackTrace();
		}
		LOGGER.info("Final Input files generated");
	}

	public void generateInputData(String endpointUrl) {

		LOGGER.info("Connecting to endpoint " + endpointUrl);
		this.getAllClasses(endpointUrl);
		this.getallInstances(endpointUrl);
		this.getAllProperties(endpointUrl);
	}

	public static void main(String[] args) {
		SparqlEndpointHandler spr = new SparqlEndpointHandler();
		spr.generateInputData(BASE_URI);

	}
}
