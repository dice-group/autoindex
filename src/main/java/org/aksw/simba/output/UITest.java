package org.aksw.simba.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.aksw.simba.Initializer.initializer;
import org.aksw.simba.dataformat.ListUtility;
import org.aksw.simba.dataformat.ResultDataStruct;
import org.aksw.simba.importer.neo4j;
import org.aksw.simba.inputdata.SparqlEndpointHandler;
import org.aksw.simba.ngram.NGramModel;
import org.aksw.simba.propagator.propagator;
import org.aksw.simba.serverproperties.pathvariables;
import org.aksw.simba.urimapper.ESNode;
import org.aksw.simba.urimapper.IndexerInterface;
import org.aksw.simba.urimapper.Mapper;
import org.elasticsearch.search.SearchHit;
import org.neo4j.graphdb.GraphDatabaseService;

public class UITest {
	private pathvariables Instance;

	public UITest() throws IOException {
		this.Instance = new pathvariables();
	}

	private File[] rdffileiterator() {
		File folder = new File(this.Instance.getrdf());
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}

	private String graphpath() {

		return this.Instance.getgraph();
	}

	public static void main(String[] args) throws IOException {
		IndexerInterface esnode = new ESNode();
		SparqlEndpointHandler spr = new SparqlEndpointHandler();
		spr.generateInputData(spr.getBaseUri());
		esnode.startCluster("DBpediacluster");
		try {
			esnode.rdfcluster("Input Data" + File.separator + "Instances"
					+ File.separator + "allInstances-nif.ttl", "instance");
			esnode.rdfcluster("Input Data" + File.separator + "Class"
					+ File.separator + "allclass-nif.ttl", "class");
			esnode.rdfcluster("Input Data" + File.separator + "Properties"
					+ File.separator + "allProperties-nif.ttl", "properties");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SearchHit[] results = esnode.transportclient("8000kilometre",
				"instance");
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
			System.out.println(result.values());
		}
		String Query = "Bristol City FC gender";
		NGramModel ngrams = new NGramModel();
		ngrams.CreateNGramModel(Query);

		Mapper mappings = new Mapper();
		mappings.BuildMappings(esnode, ngrams.getNGramMod());
		initializer init = new initializer();
		init.initiate(mappings.getMappings(), ngrams.getNGramMod());

		/* extracting paths where the graphdb has to be formed */
		UITest pathsetter = new UITest();
		File[] listoffiles = pathsetter.rdffileiterator();
		String graphpath = pathsetter.graphpath();

		/* Formation of graph database at specified path */
		neo4j graphdb = new neo4j(graphpath);
		GraphDatabaseService gdb = graphdb.getgdbservice();

		for (File file : listoffiles) {
			if (file.isFile()) /*
								 * extracting all the files in the specified
								 * folder
								 */{
				graphdb.graphdbform(gdb, pathsetter.Instance.getrdf() + '/'
						+ file.getName());
			}
		}
		propagator finalresults = new propagator();
		finalresults.PropagateInit(gdb, init.getResultsList());
		List<ResultDataStruct> actual = init.getResultsList();

		/*
		 * Sorting the final nodes according to explanation score and energy
		 * score
		 */
		ListUtility.sortresults(actual);
		System.out
				.println(" Retrieving results in decreasing order of relevancy.... ");
		System.out.println();
		for (int i = actual.size() - 1; i >= 0; i--) {
			System.out.println(" URI : " + actual.get(i).getURI()
					+ ", Colors : " + actual.get(i).getImage() + "}");
		}
	}
}
