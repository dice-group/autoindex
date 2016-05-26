package org.aksw.simba.dbpedia.search;

public class Result {
	private String url;
	private String label;
	private Double pagerank;

	public Result(String url, String label, Double pagerank) {
		super();
		this.url = url;
		this.label = label;
		this.pagerank = pagerank;
	}

}
