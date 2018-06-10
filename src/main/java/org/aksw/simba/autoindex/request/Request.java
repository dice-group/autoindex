package org.aksw.simba.autoindex.request;

import java.util.ArrayList;
import java.util.List;

/*Class for Dependancy Injection of Spring for Handling Index Create Requests*/

public class Request{
	
	private String url;
	private String default_graph;
	private String userId;
	private Boolean useLocalDataSource;
	private RequestType requestType;
	private Keys keys;
	public enum RequestType {
		URI ,
		RDF_FILE  , 
		LOCAL_DB,
		CUSTOM_STRING,
		NONE
	}
	private List<String> fileList;
	private int limit;
	public Request(){
		this.url = "";
		this.default_graph = "";
		this.userId = "00000000001";
		this.useLocalDataSource = false;
		this.requestType = RequestType.NONE;
		this.fileList = new ArrayList<String>();
		this.limit = 0;
		this.keys = new Keys();
	}
	public Request(String url) {
		this.url = url;
		this.default_graph = "";
		this.userId = "00000000001";
		this.useLocalDataSource = false;
		this.requestType = RequestType.URI;
		this.fileList = new ArrayList<String>();
		this.limit = 0;
		this.keys = new Keys();
	}
	public Request(String url , String label , String userId){
		this.url = url;
		this.default_graph = label;
		this.userId = userId;
		this.useLocalDataSource = false;
		this.requestType = RequestType.URI;
		this.fileList = new ArrayList<String>();
		this.limit = 0;
		this.keys = new Keys();
	}
	public Request(List<String> fileList , String userId) {
		this.fileList = fileList;
		this.userId = userId;
		this.useLocalDataSource = false;
		this.requestType = RequestType.RDF_FILE;
		this.limit = 0;
		this.url = "";
		this.default_graph = "";
		this.keys = new Keys();
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setDefaultGraph(String default_graph) {
		this.default_graph = default_graph;
	}
	
	public String getDefaultGraph() {
		return this.default_graph;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUseLocalDataSource(Boolean useLocalDataSource) {
		this.useLocalDataSource = useLocalDataSource;
		if(this.useLocalDataSource.equals(true)) {
			this.requestType = RequestType.LOCAL_DB;
		}
	}
	
	public Boolean isUseLocalDataSource() {
		return this.useLocalDataSource;
	}
	
	public void setRequestType (String requestType) {
		String type = requestType.toLowerCase();
		if ("uri".equals(type)) 
			this.requestType = RequestType.URI;
		else if ("filepath".equals(type)) 
			this.requestType = RequestType.RDF_FILE;
		else if ("custom".equals(type)) 
			this.requestType = RequestType.CUSTOM_STRING;
		else 
			this.requestType = RequestType.NONE;

	}
	
	public RequestType getRequestType() {
		return this.requestType;
	}
	
	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
	
	public List<String> getFileList() {
		return this.fileList;
	}
	
	public void setLimit (int limit) {
		this.limit = limit;
	}
	
	public int getlimit() {
		return this.limit;
	}

	public Keys getKeys() {
		if(this.keys == null) {
			this.keys = new Keys();
		}
		return this.keys;
	}	
	public void setKeys(Keys keys) {
		this.keys = keys;
  }	
}