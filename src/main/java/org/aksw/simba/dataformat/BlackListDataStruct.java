package org.aksw.simba.dataformat;

public class BlackListDataStruct {
	private String URI1;
	private String URI2;

	public BlackListDataStruct(String URI1, String URI2) {
		this.URI1 = URI1;
		this.URI2 = URI2;
	}

	public String getfirstURI() {
		return this.URI1;
	}

	public String getsecondURI() {
		return this.URI2;
	}
}