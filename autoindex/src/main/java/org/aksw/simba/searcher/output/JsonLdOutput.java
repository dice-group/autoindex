package org.aksw.simba.searcher.output;

import java.util.List;
import org.aksw.simba.searcher.search.Result;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonLdOutput {

	public static String getJsonLDoutput(List<Result> query_result,
			String Index, int limit) {

		String json = " { \n"
				+ "\"context\": {\n"
				+ "\"vocab\": \" \",\n"
				+ "\"url\": \"goog:resultScore\",\n \"pagerank\": \"goog:detailedDescription\",\n"
				+ "\"label\": \"goog:EntitySearchResult\",\n" + "},"
				+ "\n\"type\": \"ItemList\",;\n"
				+ "\"itemListElement\": \n[{\n" + "\"IndexType\": \""
				+ Index.toUpperCase() + "\",\n" + "\"Number of Results\": \""
				+ limit + "\",\n";
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		json += gson.toJson(query_result);
		return json;
	}
}
