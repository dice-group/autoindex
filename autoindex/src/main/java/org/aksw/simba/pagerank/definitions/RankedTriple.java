package org.aksw.simba.pagerank.definitions;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

public class RankedTriple extends Triple {

	double rank;

	public RankedTriple(Node s, Node p, Node o, double rank) {
		super(s, p, o);
		this.rank = rank;
	}

	public RankedTriple(Node s, Node p, Node o) {
		super(s, p, o);
		this.rank = 0.0;
	}

}
