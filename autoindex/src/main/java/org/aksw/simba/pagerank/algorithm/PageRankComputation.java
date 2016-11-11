package org.aksw.simba.pagerank.algorithm;

import java.util.List;

import org.aksw.simba.pagerank.definitions.RankedNode;
import org.aksw.simba.pagerank.definitions.RankedTriple;
import org.aksw.simba.pagerank.input.ProcessedInput;
import org.jblas.DoubleMatrix;

public class PageRankComputation {
	ProcessedInput input = new ProcessedInput(
			"/Users/Kunal/Downloads/ekaw-2012-complete.rdf");
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

		this.triples2Nodes = DoubleMatrix.zeros(input.getNumberofTriples() + 1,
				input.getNumberofResources() + 1);
		this.nodes2Triples = DoubleMatrix.zeros(
				input.getNumberofResources() + 1,
				input.getNumberofTriples() + 1);
		this.pMatrixTriples = DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pMatrixTriples = DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionMatrix=  DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionInitialVal = input.getNumberofResources()
				/ (input.getNumberofTriples() * (input.getNumberofResources() + input
						.getNumberofTriples()));
		this.dampingFactor = 0.85;

	}

	public void computePR() {
		this.createTriples2NodesMatrix(input.getListOfTriples(),
				input.getListOfResources());
		this.createTriples2NodesMatrix(input.getListOfTriples(),
				input.getListOfResources());
		this.computeProbabilityTripleMatrix();

		this.initializeProbabilityDistributionMatrix();
		DoubleMatrix initialDistributionMatrix = DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		DoubleMatrix identityMatrix = DoubleMatrix.eye(input
				.getNumberofTriples() + 1);
		DoubleMatrix pMatrixTriplesTranspose = pMatrixTriples.transpose();

		while ( initialDistributionMatrix.eq(pDistributionMatrix).min() == 1 ) {

			initialDistributionMatrix = pDistributionMatrix;
			pDistributionMatrix = (pMatrixTriplesTranspose.muli(dampingFactor))
					.mul(initialDistributionMatrix).add(
						identityMatrix.muli(dampingFactor));

		}

		//pDistributionMatrix.print();

	}

	public void createTriples2NodesMatrix(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes) {

		for (RankedNode c : listofNodes)

		{
			for (RankedTriple r : listofTriples) {
				if (c.equals(r.getSubject()) || (c.equals(r.getPredicate()))
						|| (c.equals(r.getObject())))

				{
					triples2Nodes.put(listofTriples.indexOf(r),
							listofNodes.indexOf(r), 1 / 3);
				}

			}
		}
	}

	public void calculateTriplesofNodes(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes) {
		int triplecount = 0;
		for (RankedNode c : listofNodes)

		{
			triplecount = 0;
			for (RankedTriple r : listofTriples) {
				if (c.equals(r.getSubject()) || c.equals(r.getPredicate())
						|| c.equals(r.getObject()))

				{
					triplecount++;
				}

			}
			c.setNumberOfTriples(triplecount);
		}
	}

	public void createNode2TripleMatrix(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes) {

		this.calculateTriplesofNodes(listofTriples, listofNodes);

		for (RankedNode c : listofNodes)

		{

			for (RankedTriple r : listofTriples) {
				if (c.equals(r.getSubject()) || c.equals(r.getPredicate())
						|| c.equals(r.getObject()))

				{
					nodes2Triples.put(listofTriples.indexOf(r),
							listofNodes.indexOf(r), 1 / c.getNumberOfTriples());
				}

			}
		}
	}

	public void computeProbabilityTripleMatrix() {
		pMatrixTriples = triples2Nodes.mul(nodes2Triples);
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
