package org.aksw.simba.urimapper;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFBase;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESNode implements IndexerInterface {
	private static Logger log = LoggerFactory.getLogger(ESNode.class);
	private Node node;
	private Client client;
	// this may lead to corruption in multi thread environment
	private String indexname;
	private BulkProcessor bulkProcessor;

	public void startCluster(String clustername) {
		// /* Initialization of cluster */
		Settings settings = Settings.builder()
		                            .put("index.analysis.analyzer.my_ngram_analyzer.type", "custom")
		                            .put("index.analysis.analyzer.my_ngram_analyzer.tokenizer", "my_ngram_tokenizer")
		                            .put("index.analysis.tokenizer.my_ngram_tokenizer.type", "nGram")
		                            .put("index.analysis.tokenizer.my_ngram_tokenizer.min_gram", "1")
		                            .put("index.analysis.tokenizer.my_ngram_tokenizer.max_gram", "10")
		                            .putArray("index.analysis.tokenizer.my_ngram_tokenizer.token_chars", new String[0])
		                            .build();

		try {
			this.node = new Node(settings).start();
		} catch (NodeValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Starting the central server */
		this.client = this.node.client();

		// ONLY WORKS in LOCAL indizes
		// if (!(new File(clustername).exists())) {
		// TODO proper checking if local index exists

		/* Prepare Bulk Load */
		this.bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				log.debug("Before Bulk");
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				log.debug("After Bulk. Bulk took: " + response.getTookInMillis());
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				log.error("After Bulk", failure);
			}
		})
		                                  .setBulkActions(100000)
		                                  .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
		                                  .setFlushInterval(TimeValue.timeValueSeconds(60))
		                                  .setConcurrentRequests(0)
		                                  .build();
		// }

	}

	public void rdfcluster(String labelspath, String indexname) throws FileNotFoundException, IOException {
		// TODO proper checking if local index exists

		// if (bulkProcessor!=null) {
		/* Index name */
		this.indexname = indexname;

		/* Parsing turtlefile */
		log.info("Start parsing: " + labelspath);

		StreamRDF destination = new TripleToIndex();
		RDFDataMgr.parse(destination, labelspath);

		// }
	}

	class TripleToIndex extends StreamRDFBase {
		@Override
		public void triple(Triple triple) {
			addToIndex(triple.getSubject()
			                 .getURI(), triple.getObject()
			                                  .getURI());
		}
	}

	private void addToIndex(String subject, String objectString) {
		/* Indexing the data in the central server */
		try {
			XContentBuilder endObject = jsonBuilder().startObject()
			                         .field("uri", subject)
			                         .field("label", objectString)
			                         .endObject();
			bulkProcessor.add(new IndexRequest(indexname, "mappings").source(endObject));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void datatypeindex(String filepath, String indexname) throws FileNotFoundException, IOException {
		int i = 0;
		System.out.println("Building the " + indexname + " data...........");
		BufferedReader in = new BufferedReader(new FileReader(filepath));
		String line;
		String value = "";
		String standardunitdatatype;
		String factorunitdatatype;
		while (!((line = in.readLine()) == null)) {
			if (line.contains("$")) {

				line = in.readLine();
				if (line.contains("#")) {
					StringTokenizer standardst = new StringTokenizer(line, "#");
					if (!standardst.hasMoreTokens()) {
						System.err.print("Invalid standarddataunit format: err");
					}
					standardunitdatatype = standardst.nextToken();
					while (!("".equals((line = in.readLine())))) {
						if (line.contains("//") || line.contains("missing conversion factor"))
							continue;
						if (line.equals(""))
							break;
						StringTokenizer factorst = new StringTokenizer(line, "-");
						factorunitdatatype = factorst.nextToken();
						String labels = factorst.nextToken();
						String[] labelarr = labels.split(", ");
						value = "";
						while (factorst.hasMoreTokens())
							value = value + factorst.nextToken();
						for (String label : labelarr) {
							i = i + 1;
							client.prepareIndex(indexname, "mappings", String.valueOf(i))
							      .setSource(jsonBuilder().startObject()
							                              .field("standardunit", standardunitdatatype)
							                              .field("factorunit", factorunitdatatype)
							                              .field("label", label)
							                              .field("value", value)
							                              .endObject())
							      .execute()
							      .actionGet();
						}
					}
				} else {
					System.err.print("Invalid standardunit format format in datatype file: Standardunit not present");
				}
			} else {
				System.err.print("Invalid dimension format in datatype file");
			}
		}
		in.close();
		System.out.println("Data Entry complete");
	}

	// TODO allow partial hits
	@Override
	public SearchHit[] transportclient(String query, String path) {
		/* Connecting the remote client with the central cluster */
		Client clientremote = this.node.client();

		/* Building the Query */
		MatchQueryBuilder qb = QueryBuilders.matchQuery("label", query)
		                                    .analyzer("my_ngram_analyzer");
		SearchRequestBuilder srb = clientremote.prepareSearch(path)
		                                       .setTypes("mappings");
		SearchResponse retrieved = srb.setQuery(qb)
		                              .execute()
		                              .actionGet();

		/* Retrieving the results from the query */
		SearchHit[] results = retrieved.getHits()
		                               .getHits();
		return results;
	}

	@Override
	public void closeBulkLoader() {
		if (bulkProcessor != null)
			bulkProcessor.close();
	}

	public void closeClient() {
		client.close();
	}

	public List<Map<String, Object>> getAllDocs() {
		int scrollSize = 1000;
		List<Map<String, Object>> esData = new ArrayList<Map<String, Object>>();
		SearchResponse response = null;
		int i = 0;
		while (response == null || response.getHits()
		                                   .hits().length != 0) {
			response = client.prepareSearch("dbpedialabels")
			                 .setTypes("mappings")
			                 .setQuery(QueryBuilders.matchAllQuery())
			                 .setSize(scrollSize)
			                 .setFrom(i * scrollSize)
			                 .execute()
			                 .actionGet();
			for (SearchHit hit : response.getHits()) {
				esData.add(hit.getSource());
			}
			i++;
		}
		return esData;
	}
}
