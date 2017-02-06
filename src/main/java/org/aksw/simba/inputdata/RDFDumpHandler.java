package org.aksw.simba.inputdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.simba.dataformat.Result;
import org.aksw.simba.output.JsonSearalizer;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFBase;

public class RDFDumpHandler {
	// TODO: CHANGE THE PAGERANK AND HANDLING OF TRIPLES
	static Set<Resource> listofResources;

	public Set<Node> getResource(String dumpLocation) {

		final Set<Node> list = new HashSet<Node>();
		
		class TripleToIndex extends StreamRDFBase {
			@Override
			public void triple(Triple triple) {
				Node subject = triple.getSubject();
				if (!subject.isBlank() && list.size() <= 10)
					list.add(subject);
			}
		};
		
		StreamRDF destination = new TripleToIndex();
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
}
