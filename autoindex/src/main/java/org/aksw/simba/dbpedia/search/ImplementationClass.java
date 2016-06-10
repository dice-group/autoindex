package org.aksw.simba.dbpedia.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.aksw.simba.dbpedia.indexcreation.Handler_SparqlEndpoint;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@SwaggerDefinition(host = "localhost:8080", //
		info = @Info(description = "Generates Indexes and Allows to query the ", //
				version = "V1.0", //
				title = "AutoIndex", //

				contact = @Contact(name = "Aksw", url = "http://aksw.org/Team.html")), //
		schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }, //
		consumes = { "application/json" }, //
		produces = { "application/json" }, //
		tags = { @Tag(name = "swagger") })

public class ImplementationClass {
	public static void main(String[] args) throws IOException, JsonLdError {
		Handler_SparqlEndpoint.generateIndexforClass();
		 Handler_SparqlEndpoint.generateIndexforProperties();
		 Handler_SparqlEndpoint.generateIndexforInstances();
//		 final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);

		// port(8082);

		Gson gson = new GsonBuilder().create();

		List<Result> query_result = SearchLuceneLabel.getEndpointResult("instance", "berlin");

		InputStream input = new FileInputStream(
				"src" + File.separator + "main" + File.separator + "java" + File.separator + "context.json");

		final Object context = JsonUtils.fromInputStream(input);
		JsonLdOptions options = new JsonLdOptions();
		options.format = "application/ld+json";
		Object compact = JsonLdProcessor.compact(JsonUtils.fromString(gson.toJson(query_result)), context, options);

		System.out.println(JsonUtils.toPrettyString(compact));

		/*
		 * get("/search", (req, res) -> { String index =
		 * req.queryParams("Index"); String searchlabel =
		 * req.queryParams("term"); List<Result> query_result =
		 * getEndpointResult(index, searchlabel);
		 * 
		 * log.info("Responding to Query"); if (flag == true) { log.info(
		 * "Choosing default index"); flag = false; //
		 * 
		 * 
		 * return JsonUtils.toPrettyString(compact);
		 * 
		 * } else { } });
		 */
	}
}
