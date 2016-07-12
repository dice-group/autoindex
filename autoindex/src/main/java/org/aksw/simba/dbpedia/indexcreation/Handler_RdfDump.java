package org.aksw.simba.dbpedia.indexcreation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import com.hp.hpl.jena.graph.Node;

import org.aksw.simba.dbpedia.rdfdumpreader.RdfDumpReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handler_RdfDump {
	private static Logger log = LoggerFactory.getLogger(Handler_RdfDump.class);

	public static  void generateDumpIndex() {
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream("src/main/java/properties/autoindex.properties");
			prop.load(input);

			String index = prop.getProperty("index_dump");
			log.info("The index will be here: " + index);

			String baseURI = prop.getProperty("baseURI");
			log.info("Setting Base URI to: " + baseURI);
			Set<Node> results = RdfDumpReader.getResource();
			IndexCreator ic = new IndexCreator();
			ic.createDumpIndex(results, index, baseURI);
			ic.close();
		} catch (IOException e) {
			log.error("Error while creating index. Maybe the index is corrupt now.", e);
		}

	}
}
