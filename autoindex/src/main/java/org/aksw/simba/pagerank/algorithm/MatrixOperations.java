package org.aksw.simba.pagerank.algorithm;

import org.aksw.simba.pagerank.input.ProcessedInput;
import org.jblas.DoubleMatrix;

public class MatrixOperations {

	DoubleMatrix triples2Nodes;

	public MatrixOperations() {
		ProcessedInput input = new ProcessedInput(
				"/Users/Kunal/Downloads/ekaw-2012-complete.rdf");

		this.triples2Nodes = DoubleMatrix.zeros(input.getNumberofTriples(),
				input.getNumberofResources());

	}

}
