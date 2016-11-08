package org.aksw.simba.pagerank.input;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class RDFFileReader {

	static final String inputFileName = "/Users/Kunal/Downloads/ekaw-2012-complete.rdf";

	public static void main(String args[]) {
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName
					+ " not found");
		}

		// read the RDF/XML file
		model.read(in, "");

		// write it to standard out
		model.write(System.out);
	}
}
