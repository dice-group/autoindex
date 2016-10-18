package org.aksw.simba.searcher.search;

public class Result {
	private String url;
	private String label;
	private Double pagerank;

	public Result(String url, String label, Double pagerank) {
		super();
		this.setUrl(url);
		this.setLabel(label);
		this.setPagerank(pagerank);
	}

	public Result(String string, String string2) {
		this.setUrl(url);
		this.setLabel(label);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getPagerank() {
		return pagerank;
	}

	public void setPagerank(Double pagerank) {
		this.pagerank = pagerank;
	}

}
