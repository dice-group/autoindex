package org.aksw.simba.pagerank.input;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.pagerank.definitions.RankedNode;
import org.aksw.simba.pagerank.definitions.RankedTriple;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class ProcessedInput {

	int numberofTriples;
	int numberofLiterals;
	int numberofResources;
	List<RankedNode> listOfResources;
	List<RankedTriple> listOfTriples;

	Model model;

	public static void main(String[] args) {
		String inputFileName = "/Users/Kunal/Downloads/ekaw-2012-complete.rdf";
		new ProcessedInput(inputFileName);

	}

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
		this.listOfResources = new ArrayList<RankedNode>();
		this.listOfTriples = new ArrayList<RankedTriple>();
		calculateDimensions(model);
	}

	public List<RankedNode> getListOfResources() {
		return listOfResources;
	}

	public void setListOfResources(List<RankedNode> listOfResources) {
		this.listOfResources = listOfResources;
	}

	public List<RankedTriple> getListOfTriples() {
		return listOfTriples;
	}

	public void setListOfTriples(List<RankedTriple> listOfTriples) {
		this.listOfTriples = listOfTriples;
	}

	public void calculateDimensions(Model model) {
		// create an empty model
		numberofLiterals = 0;
		numberofTriples = 0;
		StmtIterator iter = model.listStatements();
		try {
			while (iter.hasNext()) {

				Triple a = iter.next().asTriple();
				listOfResources.add(new RankedNode(a.getSubject()));
				listOfResources.add(new RankedNode(a.getPredicate()));
				listOfResources.add(new RankedNode(a.getObject()));

				if (a.getObject().isLiteral()) {

					numberofLiterals++;

				}
				listOfTriples.add(new RankedTriple(a.getSubject(), a
						.getPredicate(), a.getObject()));
			}
		} finally {
			if (iter != null)
				iter.close();
		}
		numberofTriples = listOfTriples.size();
		numberofResources = listOfResources.size();
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
