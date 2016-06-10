package org.aksw.simba.dbpedia.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.aksw.simba.dbpedia.indexcreation.Handler_SparqlEndpoint;
import org.aksw.simba.dbpedia.output.JsonLdOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



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
	
}
