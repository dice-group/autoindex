package org.aksw.simba.dbpedia.rdfdumpreader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.ReaderRIOT;
import org.apache.jena.riot.lang.CollectorStreamTriples;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFLib;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class RdfDumpReader {
	static Set<Resource> listofResources;

	public static CollectorStreamTriples getResource() {
		FileManager.get().addLocatorClassLoader(RdfDumpReader.class.getClassLoader());
		CollectorStreamTriples inputStream = new CollectorStreamTriples();
		RDFDataMgr.parse(inputStream, "instance_types_en.ttl");
		// Model model = FileManager.get().loadModel(, null, "TURTLE");
		// model.write(System.out, "TURTLE");
		// listofResources = new HashSet<Resource>();
		//
		// ResIterator iter = model.listSubjects();
		//
		// while (iter.hasNext()) {
		// if (iter.next().isURIResource()) {
		// listofResources.add(iter.next());
		// }
		// System.out.println(stmt.getResource().getURI());
		// System.out.println(stmt.getResource().getLocalName());
		////
		return inputStream;

	}

	public static void main(String[] args) {

		CollectorStreamTriples inputStream = getResource();
		for (Triple triple : inputStream.getCollected()) {

			System.out.println(triple);
		}

	}

}