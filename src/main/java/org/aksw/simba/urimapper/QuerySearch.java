package org.aksw.simba.urimapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

public class QuerySearch {
	private final List<String> URIs = new ArrayList<String>();
	private final List<String> Labels = new ArrayList<String>();

	public QuerySearch(IndexerInterface node, String query) {
		buildquery(node, query);
	}

	private void buildquery(IndexerInterface node, String ngram) {
		SearchInLemonCluster(node, ngram, "instance");
		SearchInRDFCluster(node, ngram, "instance");

	}

	private void SearchInLemonCluster(IndexerInterface node, String label, String index) {
		SearchHit[] results = node.transportclient(label, index);
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
			this.Labels.add((String) result.get("label"));
			this.URIs.add((String) result.get("uri"));
		}
	}

	private void SearchInRDFCluster(IndexerInterface node, String label, String index) {
		SearchHit[] results = node.transportclient(label, index);
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
			this.Labels.add(result.values()
			                      .toString()
			                      .split(", ")[0].replaceAll("\\[", ""));
			this.URIs.add(result.values()
			                    .toString()
			                    .split(", ")[1].replaceAll("\\]", ""));
		}
	}

	public List<String> getURIList() {
		return this.URIs;
	}

	public List<String> getLabelList() {
		return this.Labels;
	}

}
