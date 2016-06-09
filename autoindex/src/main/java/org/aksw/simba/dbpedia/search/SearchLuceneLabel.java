package org.aksw.simba.dbpedia.search;

import java.io.File;
import static spark.Spark.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.net.URLDecoder;
import org.apache.lucene.store.NIOFSDirectory;

import org.slf4j.LoggerFactory;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hp.hpl.jena.rdf.model.Model;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.aksw.simba.dbpedia.indexcreation.Handler_SparqlEndpoint;
import org.aksw.simba.dbpedia.swagger.SwaggerParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;

/*@SwaggerDefinition(host = "localhost:4567", //
		info = @Info(description = "AutoIndex", //
				version = "V1.0", //
				title = "Some random api for testing", //

				contact = @Contact(name = "Aksw", url = "http://aksw.org/Team.html")), //
		schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }, //
		consumes = { "application/json" }, //
		produces = { "application/json" }, //
		tags = { @Tag(name = "swagger") })
*/public class SearchLuceneLabel {
	public static final String APP_PACKAGE = "org.aksw.simba.dbpedia";
	final static int TIMES_MORE_RESULTS = 10;
	private static org.slf4j.Logger log = LoggerFactory.getLogger(SearchLuceneLabel.class);

	public DirectoryReader readerFromIndex(NIOFSDirectory dir) throws IOException {
		return DirectoryReader.open(dir);
	}

	static boolean flag = false;

	public BooleanQuery queryFromString(String queryString) throws UnsupportedEncodingException {
		BooleanQuery query = new BooleanQuery();
		PhraseQuery phraseQuery = new PhraseQuery();
		String[] words = URLDecoder.decode(queryString, "UTF-8").toLowerCase().split(" ");
		for (String it : words) {
			it.trim();
			if (!(it.isEmpty()) && (it != "*")) {
				Term term = new Term("label", it);
				phraseQuery.add(term);
				TermQuery parse = new TermQuery(term);
				parse.setBoost(0.9f);
				BooleanClause clause = new BooleanClause(parse, Occur.SHOULD);
				query.add(clause);
			}
		}

		query.add(new BooleanClause(phraseQuery, Occur.SHOULD));
		return query;
	}

	public List<Result> search(IndexSearcher searcher, String queryString, Integer limit) throws IOException {
		if (limit == 0)
			limit = 10;
		BooleanQuery query = queryFromString(queryString);

		int hitsPerPage = limit * TIMES_MORE_RESULTS;
		Sort sort = new Sort(SortField.FIELD_SCORE,
				new SortedNumericSortField("pagerank_sort", SortField.Type.FLOAT, true));
		TopFieldDocs hits = searcher.search(query, hitsPerPage, sort);

		List<Result> res = new ArrayList<Result>();

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Result result = new Result(doc.get("url"), doc.get("label"), Double.parseDouble(doc.get("pagerank")));
			res.add(result);
		}
		return res;
	}

	/*
	 * public List<Result> search_dump(IndexSearcher searcher, String
	 * queryString, Integer limit) throws IOException { if (limit == 0) limit =
	 * 10; BooleanQuery query = queryFromString(queryString);
	 * 
	 * int hitsPerPage = limit * TIMES_MORE_RESULTS; Sort sort = new Sort(new
	 * SortField("label", SortField.Type.STRING, true)); TopFieldDocs hits =
	 * searcher.search(query, hitsPerPage, sort);
	 * 
	 * List<Result> res = new ArrayList<Result>();
	 * 
	 * for (ScoreDoc scoreDoc : hits.scoreDocs) { Document doc =
	 * searcher.doc(scoreDoc.doc); Result result = new Result(doc.get("url"),
	 * doc.get("label")); res.add(result); } return res; }
	 */
	/*
	 * public static List<Result> getRDFDumpResult(String term) throws
	 * IOException { Properties prop = new Properties(); InputStream input = new
	 * FileInputStream("src/main/java/properties/autoindex.properties");
	 * prop.load(input); IndexSearcher searcher = null; List<Result> resultlist
	 * = null;
	 * 
	 * String indexDir = prop.getProperty("index_dump");
	 * 
	 * @SuppressWarnings("deprecation") IndexReader reader =
	 * IndexReader.open(NIOFSDirectory.open(new File(indexDir))); searcher = new
	 * IndexSearcher(reader);
	 * 
	 * SearchLuceneLabel tester;
	 * 
	 * try { tester = new SearchLuceneLabel();
	 * 
	 * resultlist = tester.search_dump(searcher, term, 0);
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } return resultlist; }
	 */
	public static List<Result> getEndpointResult(String index, String term) throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/java/properties/autoindex.properties");
		prop.load(input);
		IndexSearcher searcher = null;
		List<Result> resultlist = null;
		switch (index.toUpperCase()) {
		case "CLASS": {

			String indexDir = prop.getProperty("index_class");

			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);

			break;
		}
		case "INSTANCE":

		{

			String indexDir = prop.getProperty("index_instance");

			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);
			break;
		}
		case "PROPERTY": {
			String indexDir = prop.getProperty("index_property");

			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);
			break;
		}
		default:
			String indexDir = prop.getProperty("index_instance");

			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);
			flag = true;
			break;

		}

		SearchLuceneLabel tester;

		try {
			tester = new SearchLuceneLabel();

			resultlist = tester.search(searcher, term, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultlist;
	}

	public static void main(String[] args) throws IOException, JsonLdError {
//		Handler_SparqlEndpoint.generateIndexforClass();
//		Handler_SparqlEndpoint.generateIndexforProperties();
//		Handler_SparqlEndpoint.generateIndexforInstances();
//		final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);

//		port(8082);
		 
		 
		Gson gson = new GsonBuilder().create();
		List<Result> query_result = getEndpointResult("instance", "berlin");
//		Model model = RDFDataMgr.loadModel() ;
//		 RDFDataMgr.write(System.out, model, Lang.JSONLD);
		InputStream input = new FileInputStream("src\\main\\java\\context.json");
		Object jsonObject = JsonUtils.fromString(gson.toJson(query_result));
		final Object context = JsonUtils.fromInputStream(input);
JsonLdOptions options = new JsonLdOptions();
		options.format = "application/ld+json";
		Object compact = JsonLdProcessor.compact(jsonObject, context, options);
		
		
		
		
//		System.out.println( JsonUtils.toPrettyString(compact));
		
/*		get("/search", (req, res) -> {
			String index = req.queryParams("Index");
			String searchlabel = req.queryParams("term");
			List<Result> query_result = getEndpointResult(index, searchlabel);
			
			log.info("Responding to Query");
			if (flag == true) {
				log.info("Choosing default index");
				flag = false;
//			System.out.println(swaggerJson);
//				InputStream input = new FileInputStream("src\\main\\java\\a.json");
				Object jsonObject = JsonUtils.fromString(gson.toJson("RETURNING THE DEFAULT SEARCH" + query_result));
//				Map context = new HashMap();
//				JsonLdOptions options = new JsonLdOptions();
//				Object compact = JsonLdProcessor.(jsonObject, context, options);
				Object compact = JsonLdProcessor.(jsonObject);
				
				
				return JsonUtils.toPrettyString(compact);

			} else {
				Object jsonObject = JsonUtils.fromString(gson.toJson("RETURNING THE DEFAULT SEARCH" + query_result));
//				Map context = new HashMap();
//				JsonLdOptions options = new JsonLdOptions();
//				Object compact = JsonLdProcessor.(jsonObject, context, options);
				Object compact = JsonLdProcessor.toRDF(jsonObject);
				
				return JsonUtils.toPrettyString(compact);

			}
		});
*/
	}

}
