package org.aksw.simba.pagerank.input;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	Map<String, Integer> nodesToIndex;
	Map<Integer, Integer> triplesToIndex;

	Model model;

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
		model.read(in, null, "TTL");
		this.listOfResources = new ArrayList<RankedNode>();
		this.listOfTriples = new ArrayList<RankedTriple>();
		this.nodesToIndex = new HashMap<String, Integer>();
		this.triplesToIndex = new HashMap<Integer, Integer>();

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
		numberofResources = 0;
		StmtIterator iter = model.listStatements();
		try {
			while (iter.hasNext()) {

				Triple a = iter.next().asTriple();
				RankedNode subj = new RankedNode(a.getSubject());
				RankedNode pred = new RankedNode(a.getPredicate());
				RankedNode obj = new RankedNode(a.getObject());

				if (!listOfResources.contains(subj)) {
					listOfResources.add(subj);
					nodesToIndex.put(subj.getResource().getURI(),
							numberofResources);
					numberofResources++;
				}

				if (!listOfResources.contains(pred)) {
					listOfResources.add(pred);
					nodesToIndex.put(pred.getResource().getURI(),
							numberofResources);
					numberofResources++;
				}

				if (!listOfResources.contains(obj)) {
					if (obj.getResource().isURI()) {
						listOfResources.add(obj);
						nodesToIndex.put(obj.getResource().getURI(),
								numberofResources);
						numberofResources++;
					}
				}
				if (a.getObject().isLiteral()) {
					numberofLiterals++;
				}

				RankedTriple x = new RankedTriple(a.getSubject(),
						a.getPredicate(), a.getObject());
				listOfTriples.add(x);
				triplesToIndex.put(x.hashCode(), numberofTriples);
				numberofTriples++;
			}
		} finally {
			if (iter != null)
				iter.close();
		}
		System.out.println("Number of literals " + numberofLiterals);
		System.out.println("Number of Triples " + numberofTriples);

		System.out.println("Number of resources " + numberofResources);

	}

	public Map<String, Integer> getNodesToIndex() {
		return nodesToIndex;
	}

	public void setNodesToIndex(Map<String, Integer> nodesToIndex) {
		this.nodesToIndex = nodesToIndex;
	}

	public Map<Integer, Integer> getTriplesToIndex() {
		return triplesToIndex;
	}

	public void setTriplesToIndex(Map<Integer, Integer> triplesToIndex) {
		this.triplesToIndex = triplesToIndex;
	}

	public int getNumberofResources() {
		return numberofResources;
	}

	public void setNumberofResources(int numberofResources) {
		this.numberofResources = numberofResources;
	}
}
