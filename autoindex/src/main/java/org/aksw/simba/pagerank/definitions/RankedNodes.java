package org.aksw.simba.pagerank.definitions;

import com.hp.hpl.jena.graph.Node;

public class RankedNodes {

	public RankedNodes(Node resource) {
		super();
		this.resource = resource;
		this.rank = 0.0;
	}

	public RankedNodes(Node resource, double rank) {
		super();
		this.resource = resource;
		this.rank = rank;
	}

	public Node getResource() {
		return resource;
	}

	public void setResource(Node resource) {
		this.resource = resource;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	Node resource;

	double rank;

}
