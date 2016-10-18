package org.aksw.simba.searcher.rdfdumpreader;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFLib;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.util.FileManager;

public class RdfDumpReader {
	static Set<Resource> listofResources;

	public static Set<Node> getResource() {
		FileManager.get().addLocatorClassLoader(RdfDumpReader.class.getClassLoader());
		StreamRDFLib lib = new StreamRDFLib();

		final Set<Node> list = new HashSet<Node>();
		StreamRDF destination = new StreamRDF() {

			@Override
			public void triple(Triple triple) {
				// TODO Auto-generated method stub
				if (!triple.getSubject().isBlank())
					list.add(triple.getSubject());
			}

			@Override
			public void start() {
				// TODO Auto-generated method stub
			}

			@Override
			public void quad(Quad quad) {
				// TODO Auto-generated method stub
			}

			@Override
			public void prefix(String prefix, String iri) {
				// TODO Auto-generated method stub
			}

			@Override
			public void finish() {
				// TODO Auto-generated method stub
			}

			@Override
			public void base(String base) {
				// TODO Auto-generated method stub
			}
		};
		RDFDataMgr.parse(destination, "C:\\Users\\Kunal\\workspace\\New folder\\instance_types_en.ttl");
		return list;
	}

	public static void main(String[] args) {
		Set<Node> inputStream = getResource();
		for (Node triple : inputStream) {
			System.out.println(triple.getURI());
		}

	}

}