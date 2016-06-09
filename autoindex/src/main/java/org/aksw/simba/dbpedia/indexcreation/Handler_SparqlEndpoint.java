package org.aksw.simba.dbpedia.indexcreation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.aksw.simba.dbpedia.indexcreation.IndexCreator;
import org.aksw.simba.dbpedia.sparql.GetClass;
import org.aksw.simba.dbpedia.sparql.GetInstance;
import org.aksw.simba.dbpedia.sparql.GetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.ResultSet;

public class Handler_SparqlEndpoint {
	private static Logger log = LoggerFactory.getLogger(Handler_SparqlEndpoint.class);


	public static  void generateIndexforClass() {
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream("src/main/java/properties/autoindex.properties");
			prop.load(input);

			String index = prop.getProperty("index_class");
			log.info("The index will be here: " + index);

			String baseURI = prop.getProperty("baseURI");
			log.info("Setting Base URI to: " + baseURI);
			ResultSet results = GetClass.getallclasses();
			IndexCreator ic = new IndexCreator();
			ic.createIndex(results, index, baseURI);
			ic.close();
		} catch (IOException e) {
			log.error("Error while creating index. Maybe the index is corrupt now.", e);
		}

	}

	public static void generateIndexforInstances() {
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream("src/main/java/properties/autoindex.properties");
			prop.load(input);

			String index = prop.getProperty("index_instance");
			log.info("The index will be here: " + index);

			String baseURI = prop.getProperty("baseURI");
			log.info("Setting Base URI to: " + baseURI);
			ResultSet results = GetInstance.getallinstances();
			IndexCreator ic = new IndexCreator();
			ic.createIndex(results, index, baseURI);
			ic.close();
		} catch (IOException e) {
			log.error("Error while creating index. Maybe the index is corrupt now.", e);
		}

	}

	public static void generateIndexforProperties() {
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream("src/main/java/properties/autoindex.properties");
			prop.load(input);

			String index = prop.getProperty("index_property");
			log.info("The index will be here: " + index);

			String baseURI = prop.getProperty("baseURI");
			log.info("Setting Base URI to: " + baseURI);
			ResultSet results = GetProperties.getallproperties();
			IndexCreator ic = new IndexCreator();
			ic.createIndex(results, index, baseURI);
			ic.close();
		} catch (IOException e) {
			log.error("Error while creating index. Maybe the index is corrupt now.", e);
		}

	}
}
