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

		if (!extractNumber(ngram).equals("")) {
			DatatypeNormalize(node, ngram);
		}
	}

	private void SearchInLemonCluster(IndexerInterface node, String label,
			String index) {
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
			this.Labels.add(result.values().toString().split(", ")[0]
					.replaceAll("\\[", ""));
			this.URIs.add(result.values().toString().split(", ")[1].replaceAll(
					"\\]", ""));
		}
	}

	public void DatatypeNormalize(IndexerInterface node, String label) {
		String NumberString = extractNumber(label);
		if (label.contains(NumberString + " ")) {
			label = label.replace(NumberString + " ", "");
		} else if (label.contains(NumberString)) {
			label = label.replace(NumberString, "");
		}
		Double Number = Double.parseDouble(NumberString);
		// Datatype edit here
		SearchHit[] results = node.transportclient(label, "datatypes");
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
			Number = Number
					* Double.parseDouble(result.values().toString().split(", ")[0]
							.replaceAll("\\[", ""));
			String LabelFactUnit = result.values().toString().split(", ")[2];
			String StdUnit = result.values().toString().split(", ")[3]
					.split("-")[0];
			if ((Number == Math.floor(Number)) && !Double.isInfinite(Number)) {
				this.Labels.add(Number.toString());
				this.URIs.add("\"" + String.valueOf(Number.intValue()) + "\""
						+ "xsd:Integer");
			} else
				this.URIs.add("\"" + Number.toString() + "\"" + "xsd:Double");
		}
	}

	public static String extractNumber(final String str) {

		if (str == null || str.isEmpty())
			return "";

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (Character c : str.toCharArray()) {
			if (Character.isDigit(c) || c.equals('.')) {
				sb.append(c);
				found = true;
			} else if (found) {
				// If we already found a digit before and this char is not a
				// digit, stop looping
				break;
			}
		}

		return sb.toString();
	}

	public List<String> getURIList() {
		return this.URIs;
	}

	public List<String> getLabelList() {
		return this.Labels;
	}


}
