package org.aksw.simba.pagerank.input;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class ProcessInputforPR {

	int numberofTriples;
	int numberofLiterals;
	Model model;

	public static void main(String[] args) {
		String inputFileName = "/Users/Kunal/Downloads/ekaw-2012-complete.rdf";
		ProcessInputforPR p = new ProcessInputforPR(inputFileName);
		p.getDataFromRDF(p.getModel());

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

	public ProcessInputforPR(String inputFileName) {

		this.model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName
					+ " not found");
		}
		// read the RDF/XML file
		model.read(in, "");
	}

	public void getDataFromRDF(Model model) {
		// create an empty model
		numberofLiterals = 0;
		numberofTriples = 0;
		StmtIterator iter = model.listStatements();
		try {
			while (iter.hasNext()) {
				Statement stmt = iter.next();

				Resource s = stmt.getSubject();
				Resource p = stmt.getPredicate();
				RDFNode o = stmt.getObject();

				if (s.isURIResource()) {
					System.out.print("URI");
				} else if (s.isAnon()) {
					System.out.print("blank");
				}
				if (p.isURIResource())
					System.out.print(" URI ");

				if (o.isURIResource()) {
					System.out.print("URI");
				} else if (o.isAnon()) {
					System.out.print("blank");
				} else if (o.isLiteral()) {
					System.out.print("literal");
					numberofLiterals++;
				}
				System.out.println();
				numberofTriples++;

			}
		} finally {
			if (iter != null)
				iter.close();
		}
		System.out.println("Number of literals " + numberofLiterals);
		System.out.println("Number of Triples " + numberofTriples);

		// write it to standard out
		// model.write(System.out);
	}
}
