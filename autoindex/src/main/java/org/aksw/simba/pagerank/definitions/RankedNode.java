package org.aksw.simba.pagerank.definitions;

import com.hp.hpl.jena.graph.Node;

public class RankedNode {

	public RankedNode(Node resource) {
		this.resource = resource;
		this.rank = 0.0;
	}

	public RankedNode(Node resource, double rank) {
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
	double numberOfTriples;

	public double getNumberOfTriples() {
		return numberOfTriples;
	}

	public void setNumberOfTriples(double numberOfTriples) {
		this.numberOfTriples = numberOfTriples;
	}

}
