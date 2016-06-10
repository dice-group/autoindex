package org.aksw.simba.dbpedia.output;

import java.util.List;

import org.aksw.simba.dbpedia.search.Result;

public class JsonLdOutput {

	public static String getJsonLDoutput(List<Result> query_result, String Index, int limit) {
		String output = "";
		output += " { \n" + "\"@context\": {\n" + "\"@vocab\": \" \"," + "\"url\": \"goog:resultScore\",\n"
				+ "\"pagerank\": \"goog:detailedDescription\",\n" + "\"Label\": \"goog:EntitySearchResult\",\n" + "},"
				+ "\"@type\": \"ItemList\",;\n" + "\"itemListElement\": [\n{\n" + "\"@Index\": \"" + Index + "\",\n"+ "\"@Number of Results\": \"" + limit + "\",\n";

		for (Result res : query_result) {
			output += "\"result\": {\n\"" +"url\" : \"" + res.getUrl() + "\",\n" + "\"label\" : \"" + res.getLabel() + "\"\n}\n"
					+ "\"pageRank\" : " + res.getPagerank() + "\n }\n";

		}
		output+="}";

		return output;
	}
}
