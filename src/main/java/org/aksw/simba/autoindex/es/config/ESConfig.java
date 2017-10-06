package org.aksw.simba.autoindex.es.config;

import java.io.File;
import java.io.IOException;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.askw.simba.autoindex.es.repository")
public class ESConfig {

	@Bean
	public NodeBuilder nodeBuilder() {
		return new NodeBuilder();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() throws IOException {
		File tmpDir = File.createTempFile("elastic",
				Long.toString(System.nanoTime()));
		System.out.println("Temp directory: " + tmpDir.getAbsolutePath());
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder()
				.put("http.enabled", "true")
				.put("index.number_of_shards", "1")
				.put("path.data", new File(tmpDir, "data").getAbsolutePath()) // 2
				.put("path.logs", new File(tmpDir, "logs").getAbsolutePath()) // 2
				.put("path.work", new File(tmpDir, "work").getAbsolutePath()) // 2
				.put("path.home", tmpDir); // 3

		return new ElasticsearchTemplate(nodeBuilder().local(true)
				.settings(elasticsearchSettings.build()).node().client());
	}
}
