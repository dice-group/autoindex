package org.aksw.simba.pagerank.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	DoubleMatrix triples2triples;
	DoubleMatrix pMatrixTriples;
	double dampingFactor;
	double pDistributionInitialVal;
	DoubleMatrix pDistributionMatrix;
	Map<String, Integer> nodeToTripleCount;

	public static void main(String[] args) {
		PageRankComputation p = new PageRankComputation();
		p.computePR();
	}

	public PageRankComputation() {

		this.triples2triples = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pMatrixTriples = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionMatrix = DoubleMatrix.zeros(input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionInitialVal = ((double) input.getNumberofResources()) / (input.getNumberofTriples() * (input.getNumberofResources() + input.getNumberofTriples()));
		this.dampingFactor = 1;
		this.nodeToTripleCount = new HashMap<String, Integer>();

	}

	public void computePR() {
		this.createTriples2Triples(input.getListOfTriples(), input.getListOfResources(), input.getNodesToIndex(), input.getTriplesToIndex());
		System.out.println("DONE");
		log.debug("pMatrixTriples:");
		log.debug(this.pMatrixTriples.toString("%.2f", "\n{", "}", " ", ";\n "));

		this.initializeProbabilityDistributionMatrix();
		DoubleMatrix next = DoubleMatrix.ones(input.getNumberofTriples() + 1).mul(1.0 / input.getNumberofTriples());
		DoubleMatrix identityMatrix = DoubleMatrix.ones(input.getNumberofTriples() + 1);
		DoubleMatrix pMatrixTriplesT = triples2triples.transpose();
		double epsilon = 0.001;
		double distance = 1;
		DoubleMatrix previous = DoubleMatrix.zeros(input.getNumberofTriples() + 1);
		do {
			previous = next;
			next = ((pMatrixTriplesT.mul(dampingFactor)).mmul(previous)).add(identityMatrix.mul(1 - dampingFactor));
			distance = next.distance2(previous);
			// log.debug("Distance: " + distance);
		} while (distance > epsilon);

<<<<<<< HEAD
		// TODO output the ranking vector instead of pDistributionMatrix
		log.debug(next.toString("%.2f", "\n{", "}", " ", ";\n "));
=======
>>>>>>> f79ac044a0118f2e5c11e0482a18a8fb0825593b
		// This is the ranking vector up to a scaling factor
		log.debug(next.toString("%.1f", "\n{", "}", " ", ";\n "));
	}

<<<<<<< HEAD
	public void createTriples2NodesMatrix(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes, Map<String, Integer> nodesToIndex,
			Map<Integer, Integer> triplesToIndex) {

		for (RankedTriple r : listofTriples) {
			int index = -1;
			Node subject = r.getSubject();
			index = nodesToIndex.get(subject.getURI());
			triples2Nodes.put(triplesToIndex.get(r.hashCode()), index,
					1.0 / 3.0);

			Node predicate = r.getPredicate();
			index = nodesToIndex.get(predicate.getURI());
			triples2Nodes.put(triplesToIndex.get(r.hashCode()), index,
					1.0 / 3.0);

			Node object = r.getObject();
			index = nodesToIndex.get(object.getURI());
			triples2Nodes.put(triplesToIndex.get(r.hashCode()), index,
					1.0 / 3.0);
		}
		log.debug("Triples2Nodes:");
		log.debug(triples2Nodes.toString("%.2f", "\n{", "}", " ", ";\n "));
	}

	public void createTriples2Triples(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes, Map<String, Integer> nodesToIndex,
			Map<Integer, Integer> triplesToIndex) {
=======
	public void createTriples2Triples(List<RankedTriple> listofTriples, List<RankedNode> listofNodes, Map<String, Integer> nodesToIndex, Map<Integer, Integer> triplesToIndex) {
>>>>>>> f79ac044a0118f2e5c11e0482a18a8fb0825593b

		this.calculateTriplesofNodes(listofTriples, listofNodes);

		for (RankedTriple r1 : listofTriples) {
			for (RankedTriple r2 : listofTriples) {
				Node subject1 = r1.getSubject();
				Node predicate1 = r1.getPredicate();
				Node object1 = r1.getObject();
				Node subject2 = r2.getSubject();
				Node predicate2 = r2.getPredicate();
				Node object2 = r2.getObject();
				double twohop_propability = 0.0;

				if (subject1.equals(subject2)) {
					double counter = this.nodeToTripleCount.get(subject1.getURI());
					twohop_propability += 1.0 / (3.0 * counter);
				}
				if (object1.equals(object2)) {
					double counter = this.nodeToTripleCount.get(object1.getURI());
					twohop_propability += 1.0 / (3.0 * counter);

				}
				if (subject1.equals(object2)) {
					double counter = this.nodeToTripleCount.get(subject1.getURI());
					twohop_propability += 1.0 / (3.0 * counter);
				}
				if (subject2.equals(object1)) {
					double counter = this.nodeToTripleCount.get(subject2.getURI());
					twohop_propability += 1.0 / (3.0 * counter);
				}
				if (predicate1.equals(predicate2)) {

					double counter = this.nodeToTripleCount.get(predicate1.getURI());
					twohop_propability += 1.0 / (3.0 * counter);
				}
				triples2triples.put(triplesToIndex.get(r1.hashCode()), triplesToIndex.get(r2.hashCode()), twohop_propability);
			}
		}
		log.debug("Triples2Triples:");
		log.debug(triples2triples.toString("%.2f", "\n{", "}", " ", ";\n "));
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
			if (!c.getResource().isLiteral())
				this.nodeToTripleCount.put(c.getResource().getURI(), triplecount);
		}
<<<<<<< HEAD
		log.debug("nodes2Triples:");
		log.debug(nodes2Triples.toString("%.2f", "\n{", "}", " ", ";\n "));
=======
>>>>>>> f79ac044a0118f2e5c11e0482a18a8fb0825593b
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
