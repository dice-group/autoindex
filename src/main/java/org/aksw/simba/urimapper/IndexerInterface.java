package org.aksw.simba.urimapper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.elasticsearch.search.SearchHit;

public interface IndexerInterface {

	public void startCluster(String clustername);

	/*
	 * Methods for indexing needs to be declared in the implementing class
	 * Because of their varying nature
	 */
	public SearchHit[] transportclient(String query, String path);

	public void rdfcluster(String homeKartikSinghalhomeIndexingDatadbpedia_, String indexname) throws FileNotFoundException, IOException;

	public void datatypeindex(String homeKartikSinghalhomeIndexingDatadatatype, String datatypes) throws FileNotFoundException, IOException;

	public void closeBulkLoader();

}