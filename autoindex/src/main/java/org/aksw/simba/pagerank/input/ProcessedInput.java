package org.aksw.simba.pagerank.input;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.pagerank.definitions.RankedNodes;
import org.aksw.simba.pagerank.definitions.RankedTriples;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class ProcessedInput {

	int numberofTriples;
	int numberofLiterals;
	int numberofResources;
	List<RankedNodes> listOfResources;
	List<RankedTriples> listOfTriples;

	Model model;

	/*
	 * public static void main(String[] args) { String inputFileName =
	 * "/Users/Kunal/Downloads/ekaw-2012-complete.rdf"; new
	 * ProcessedInput(inputFileName);
	 *
	 * }
	 */
	public int getNumberofTriples() {
		return numberofTriples;
	}

	public void setNumberofTriples(int numberofTriples) {
		this.numberofTriples = numberofTriples;
	}

	public int getNumberofLiterals() {
		return numberofLiterals;
	}

	public void setNumberofLiterals(int numberofLiterals) {
		this.numberofLiterals = numberofLiterals;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ProcessedInput(String inputFileName) {

		this.model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName
					+ " not found");
		}
		// read the RDF/XML file
		model.read(in, "");
		this.listOfResources = new ArrayList<RankedNodes>();
		this.listOfTriples = new ArrayList<RankedTriples>();
		calculateDimensions(model);
	}

	public void calculateDimensions(Model model) {
		// create an empty model
		numberofLiterals = 0;
		numberofTriples = 0;
		StmtIterator iter = model.listStatements();
		try {
			while (iter.hasNext()) {

				Triple a = iter.next().asTriple();

				/*
				 * System.out.println(a.getSubject().toString());
				 * System.out.println("__________________________________");
				 *
				 * System.out.println(a.getPredicate().toString());
				 * System.out.println("__________________________________");
				 *
				 * System.out.println(a.getObject().toString());
				 * System.out.println("__________________________________");
				 * System.out.println("__________________________________");
				 * System.out.println("__________________________________");
				 */
				if (a.getObject().isLiteral()) {

					numberofLiterals++;

				}
				numberofTriples++;

			}
		} finally {
			if (iter != null)
				iter.close();
		}
		numberofResources = numberofTriples * 3;
		System.out.println("Number of literals " + numberofLiterals);
		System.out.println("Number of Triples " + numberofTriples);

		System.out.println("Number of resources " + numberofResources);

	}

	public int getNumberofResources() {
		return numberofResources;
	}

	public void setNumberofResources(int numberofResources) {
		this.numberofResources = numberofResources;
	}
}
