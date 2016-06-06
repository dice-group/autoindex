package org.aksw.simba.dbpedia.rdfdumpreader;

import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.CollectorStreamTriples;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class RdfDumpReader {
	static Set<Resource> listofResources;

	public static CollectorStreamTriples getResource() {
		FileManager.get().addLocatorClassLoader(RdfDumpReader.class.getClassLoader());
		CollectorStreamTriples inputStream = new CollectorStreamTriples();
		RDFDataMgr.parse(inputStream, "pagerank_en_2015-10.ttl");
		return inputStream;

	}

	public static void main(String[] args) {

		CollectorStreamTriples inputStream = getResource();
		for (Triple triple : inputStream.getCollected()) {

			System.out.println(triple);
		}

	}

}