package org.aksw.simba.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.aksw.simba.inputdata.SparqlEndpointHandler;
import org.aksw.simba.urimapper.ESNode;
import org.aksw.simba.urimapper.IndexerInterface;
import org.elasticsearch.search.SearchHit;

public class UITest {


	public static void main(String[] args) throws IOException {
		IndexerInterface esnode = new ESNode();
		SparqlEndpointHandler spr = new SparqlEndpointHandler();
		spr.generateInputData(spr.getBaseUri());
		esnode.startCluster("DBpediacluster");
		try {
			esnode.rdfcluster("Input Data" + File.separator + "Instances"
					+ File.separator + "allInstances-nif.ttl", "instance");
			esnode.rdfcluster("Input Data" + File.separator + "Class"
					+ File.separator + "allclass-nif.ttl", "class");
			esnode.rdfcluster("Input Data" + File.separator + "Properties"
					+ File.separator + "allProperties-nif.ttl", "properties");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SearchHit[] results = esnode.transportclient("8000kilometre",
				"instance");
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
			System.out.println(result.values());
		}

	}
}
