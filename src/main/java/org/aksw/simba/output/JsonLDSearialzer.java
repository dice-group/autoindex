package org.aksw.simba.output;

import java.util.List;

import org.aksw.simba.dataformat.Result;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonLDSearialzer {

	public String getFinalResult(List<Result> query_result, String Index, int limit) {

		String json = " { \n" + "\"context\": {\n" + "\"vocab\": \" \",\n" + "\"url\": \"goog:resultScore\",\n \"pagerank\": \"goog:detailedDescription\",\n"
		        + "\"label\": \"goog:EntitySearchResult\",\n" + "}," + "\n\"type\": \"ItemList\",;\n" + "\"itemListElement\": \n[{\n" + "\"IndexType\": \"" + Index.toUpperCase() + "\",\n"
		        + "\"Number of Results\": \"" + limit + "\",\n";
		Gson gson = new GsonBuilder().setPrettyPrinting()
		                             .create();
		json += gson.toJson(query_result);
		return json;
	}

	public String getInput(List<Result> query_result, int limit) {
		String json = " { \n" + "\"context\": {\n" + "\"vocab\": \" \",\n" + "\"url\": \"goog:resultScore\",\n \"pagerank\": \"goog:detailedDescription\",\n"
		        + "\"label\": \"goog:EntitySearchResult\",\n" + "}," + "\n\"type\": \"ItemList\",;\n" + "\"itemListElement\": \n[{\n" + "\"IndexType\": \"" + null + "\",\n"
		        + "\"Number of Results\": \"" + limit + "\",\n";
		Gson gson = new GsonBuilder().setPrettyPrinting()
		                             .create();
		json += gson.toJson(query_result);
		return json;
	}
}
