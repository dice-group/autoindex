package org.aksw.simba.dbpedia.indexcreation;

public class Triple {
	public Triple(String uri, String label, String object) {

		this.uri = uri;
		this.label = label;
		this.pagerank = object;
	}

	String uri;
	String label;
	String pagerank;

	public String getSubject() {
		return uri;
	}

	public void setSubject(String subject) {
		this.uri = subject;
	}

	public String getPredicate() {
		return label;
	}

	public void setPredicate(String predicate) {
		this.label = predicate;
	}

	public String getObject() {
		return pagerank;
	}

	public void setObject(String object) {
		this.pagerank = object;
	}

	@Override
	public String toString() {
		return uri + " " + label + " " + pagerank;
	}

}