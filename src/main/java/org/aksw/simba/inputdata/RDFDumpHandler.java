package org.aksw.simba.inputdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.simba.dataformat.Result;
import org.aksw.simba.output.JsonSearalizer;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFLib;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.util.FileManager;

public class RDFDumpHandler {
	// TODO: CHANGE THE PAGERANK AND HANDLING OF TRIPLES
	static Set<Resource> listofResources;

	public static Set<Node> getResource(String dumpLocation) {
		FileManager.get().addLocatorClassLoader(
				RDFDumpHandler.class.getClassLoader());
		StreamRDFLib lib = new StreamRDFLib();

		final Set<Node> list = new HashSet<Node>();
		StreamRDF destination = new StreamRDF() {

			@Override
			public void triple(Triple triple) {
				// TODO Auto-generated method stub
				if (!triple.getSubject().isBlank() && list.size() <= 10)
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
		RDFDataMgr.parse(destination, dumpLocation);
		return list;
	}

	public String getInputData(String dumpLocation) {
		Set<Node> inputStream = getResource(dumpLocation);
		List<Result> resultset = new ArrayList<Result>();
		for (Node triple : inputStream) {
			if (triple.isURI())
				resultset.add(new Result(triple.getURI(), triple.getLocalName()
						.toString(), 0.0));

		}

		JsonSearalizer ser = new JsonSearalizer();
		return ser.getJsonOutput(resultset);
	}

	public static void main(String[] args) {
		RDFDumpHandler rdf = new RDFDumpHandler();
		System.out.println(rdf.getInputData("ekaw-2012-complete.rdf"));
	}

}
