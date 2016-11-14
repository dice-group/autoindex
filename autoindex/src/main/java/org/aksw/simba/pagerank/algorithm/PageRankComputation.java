package org.aksw.simba.pagerank.algorithm;

import java.util.List;

import org.aksw.simba.pagerank.definitions.RankedNode;
import org.aksw.simba.pagerank.definitions.RankedTriple;
import org.aksw.simba.pagerank.input.ProcessedInput;
import org.jblas.DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;

public class PageRankComputation {
	Logger log = LoggerFactory.getLogger(PageRankComputation.class);
	ProcessedInput input = new ProcessedInput("example.ttl");
	DoubleMatrix triples2Nodes;
	DoubleMatrix nodes2Triples;
	DoubleMatrix pMatrixTriples;
	double dampingFactor;
	double pDistributionInitialVal;
	DoubleMatrix pDistributionMatrix;

	public static void main(String[] args) {
		PageRankComputation p = new PageRankComputation();
		p.computePR();
	}

	public PageRankComputation() {

		this.triples2Nodes = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofResources() + 1);
		this.nodes2Triples = DoubleMatrix.zeros(input.getNumberofResources() + 1, input.getNumberofTriples() + 1);
		this.pMatrixTriples = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionMatrix = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionInitialVal = ((double) input.getNumberofResources()) / (input.getNumberofTriples() * (input.getNumberofResources() + input.getNumberofTriples()));
		this.dampingFactor = 0.85;

	}

	public void computePR() {
		this.createTriples2NodesMatrix(input.getListOfTriples(), input.getListOfResources());
		this.createNode2TripleMatrix(input.getListOfTriples(), input.getListOfResources());
		this.computeProbabilityTripleMatrix();

		this.initializeProbabilityDistributionMatrix();
		// this.triples2Nodes.print();
		DoubleMatrix initialDistributionMatrix = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		DoubleMatrix identityMatrix = DoubleMatrix.eye(input.getNumberofTriples() + 1);
		DoubleMatrix pMatrixTriplesTranspose = pMatrixTriples.transpose();
		double epsilon = 0.0000001;
		double distance = 1;
		//TODO it runs and terminates but please check math 
		do {
			initialDistributionMatrix = pDistributionMatrix;
			pDistributionMatrix = (pMatrixTriplesTranspose.muli(dampingFactor)).mmul(initialDistributionMatrix.add(identityMatrix.muli(dampingFactor)));
			distance = initialDistributionMatrix.distance2(pDistributionMatrix);
			log.debug("Distance: " + distance);
		} while (distance > epsilon);

		//TODO output the ranking vector instead of pDistributionMatrix
		log.debug(pDistributionMatrix.toString("%.1f", "\n{", "}", " ", ";\n "));

	}

	public void createTriples2NodesMatrix(List<RankedTriple> listofTriples, List<RankedNode> listofNodes) {

		for (RankedTriple r : listofTriples) {
			int index = -1;
			Node subject = r.getSubject();
			index = listofNodes.indexOf(new RankedNode(subject));
			triples2Nodes.put(listofTriples.indexOf(r), index, 1.0 / 3.0);

			Node predicate = r.getPredicate();
			index = listofNodes.indexOf(new RankedNode(predicate));
			triples2Nodes.put(listofTriples.indexOf(r), index, 1.0 / 3.0);

			Node object = r.getObject();
			index = listofNodes.indexOf(new RankedNode(object));
			triples2Nodes.put(listofTriples.indexOf(r), index, 1.0 / 3.0);
		}
		log.debug("Triples2Nodes:");
		log.debug(triples2Nodes.toString("%.1f", "\n{", "}", " ", ";\n "));
	}

	public void calculateTriplesofNodes(List<RankedTriple> listofTriples, List<RankedNode> listofNodes) {
		int triplecount = 0;
		for (RankedNode c : listofNodes) {
			triplecount = 0;
			for (RankedTriple r : listofTriples) {
				RankedNode subject = new RankedNode(r.getSubject());
				RankedNode predicate = new RankedNode(r.getPredicate());
				RankedNode object = new RankedNode(r.getObject());

				if (c.equals(subject) || c.equals(predicate) || c.equals(object)) {
					triplecount++;
				}
			}
			log.debug("c: " + c.getResource() + " => triplecount: " + triplecount);
			c.setNumberOfTriples(triplecount);
		}
	}

	public void createNode2TripleMatrix(List<RankedTriple> listofTriples, List<RankedNode> listofNodes) {

		this.calculateTriplesofNodes(listofTriples, listofNodes);

		for (RankedTriple r : listofTriples) {
			RankedNode subject = new RankedNode(r.getSubject());
			RankedNode predicate = new RankedNode(r.getPredicate());
			RankedNode object = new RankedNode(r.getObject());

			int indexOfsub = listofNodes.indexOf(subject);
			int indexOfpred = listofNodes.indexOf(predicate);
			int indexOfobj = listofNodes.indexOf(object);
			int indexOfTriple = listofTriples.indexOf(r);
			nodes2Triples.put(indexOfsub, indexOfTriple, 1.0d / listofNodes.get(indexOfsub).getNumberOfTriples());
			nodes2Triples.put(indexOfpred, indexOfTriple, 1.0d / listofNodes.get(indexOfpred).getNumberOfTriples());
			nodes2Triples.put(indexOfobj, indexOfTriple, 1.0d / listofNodes.get(indexOfobj).getNumberOfTriples());
		}
		log.debug("nodes2Triples:");
		log.debug(nodes2Triples.toString("%.1f", "\n{", "}", " ", ";\n "));
	}

	public void computeProbabilityTripleMatrix() {
		pMatrixTriples = triples2Nodes.mmul(nodes2Triples);
	}

	public void initializeProbabilityDistributionMatrix() {
		int row = input.getNumberofTriples();
		for (int y = 0; y < row; y++) {
			for (int x = 0; x < row; x++) {
				pDistributionMatrix.put(y, x, pDistributionInitialVal);
			}
		}

	}
}
