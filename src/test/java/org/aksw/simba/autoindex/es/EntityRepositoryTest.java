package org.aksw.simba.autoindex.es;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

public class EntityRepositoryTest {
	
	  @Inject
	  private static ElasticsearchTemplate elasticSearchTemplate;
	  
	  private static Client client_;
	  private static Node node_;
	  private static String storagePath_ = "./elasticTests";
	
	
	@BeforeClass
	public static void Setup() throws IOException {
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder()
			      .put("http.enabled", "false")
			      .put("path.home", storagePath_);

	    node_ = new NodeBuilder()
	      .local(true)
	      .settings(elasticsearchSettings.build())
	      .node();

	    client_ = node_.client();
	    elasticSearchTemplate = new ElasticsearchTemplate(client_);
	}
	@Test
	public void Test1() {
		
	}
	@AfterClass
	public static void tearDown() throws IOException {
		node_.close();
		deleteStorage();
	}
	
	public static void deleteStorage() throws IOException
	  {
	    File storage = new File(storagePath_);

	    if(storage.exists())
	    {
	      FileUtils.deleteDirectory(storage);
	    }

	  }
}
