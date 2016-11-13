package org.aksw.simba.pagerank.definitions;

import com.hp.hpl.jena.graph.Node;

public class RankedNode {

	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        return true;
    }

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
