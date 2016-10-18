package org.aksw.simba.searcher.indexcreation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.aksw.simba.searcher.indexcreation.IndexCreator;
import org.aksw.simba.searcher.sparql.GetRankedIndexData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.ResultSet;

public class Handler_SparqlEndpoint {
	private static Logger log = LoggerFactory
			.getLogger(Handler_SparqlEndpoint.class);
	GetRankedIndexData datahandler = new GetRankedIndexData();

	public void generateIndex(String endpoint, String endpointUri,
			String typeofIndex) {
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream(
					"src/main/java/properties/autoindex.properties");
			prop.load(input);
			String index = prop.getProperty("folderWithIndexFiles");
			switch (typeofIndex.toLowerCase()) {
			case "class": {
				index = index + File.separator + endpoint + File.separator
						+ "index_class";
				File dir = new File(index);
				try {
					if (!dir.exists())
						dir.mkdirs();
				} catch (Exception e) {
					log.info("Error making Directory");
				}
				String baseURI = prop.getProperty("baseURI");
				log.info("Setting Base URI to: " + baseURI);
				ResultSet results = this.datahandler.getallclasses(endpointUri);
				IndexCreator ic = new IndexCreator();
				ic.createIndex(results, index, baseURI);
				ic.close();
				break;
			}
			case "property": {
				index = index + File.separator + endpoint + File.separator
						+ "index_property";
				File dir = new File(index);
				try {
					if (!dir.exists())
						dir.mkdirs();
				} catch (Exception e) {
					log.info("Error making Directory");
				}
				String baseURI = prop.getProperty("baseURI");
				log.info("Setting Base URI to: " + baseURI);
				ResultSet results = this.datahandler
						.getallproperties(endpointUri);
				IndexCreator ic = new IndexCreator();
				ic.createIndex(results, index, baseURI);
				ic.close();
				break;
			}
			case "instance": {
				index = index + File.separator + endpoint + File.separator
						+ "index_instance";
				File dir = new File(index);
				try {
					if (!dir.exists())
						dir.mkdirs();
				} catch (Exception e) {
					log.info("Error making Directory");
				}
				String baseURI = prop.getProperty("baseURI");
				log.info("Setting Base URI to: " + baseURI);
				ResultSet results = this.datahandler
						.getallinstances(endpointUri);
				IndexCreator ic = new IndexCreator();
				ic.createIndex(results, index, baseURI);
				ic.close();
				break;
			}

			default: {
				index = index + File.separator + endpoint + File.separator
						+ "index_instance";
				File dir = new File(index);
				try {
					if (!dir.exists())
						dir.mkdirs();
				} catch (Exception e) {
					log.info("Error making Directory");
				}
				String baseURI = prop.getProperty("baseURI");
				log.info("Setting Base URI to: " + baseURI);
				ResultSet results = this.datahandler
						.getallinstances(endpointUri);
				IndexCreator ic = new IndexCreator();
				ic.createIndex(results, index, baseURI);
				ic.close();
				break;
			}
			}

		} catch (IOException e) {
			log.error(
					"Error while creating index. Maybe the index is corrupt now.",
					e);
		}

	}

}
