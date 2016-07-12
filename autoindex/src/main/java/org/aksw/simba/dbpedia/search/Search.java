package org.aksw.simba.dbpedia.search;

import static spark.Spark.port;
import static spark.Spark.get;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.net.URLDecoder;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aksw.simba.dbpedia.indexcreation.Handler_SparqlEndpoint;
import org.aksw.simba.dbpedia.output.JsonLdOutput;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
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

public class Search {
	// static boolean flag = false;
	public static final String APP_PACKAGE = "org.aksw.simba.dbpedia";
	final static int TIMES_MORE_RESULTS = 10;
	static String index = null;
	static String indent = null;
	static int limit = 0;
	static String searchlabel = null;
	static String endpoint = null;
	static String endpointuri = null;

	private static void setDefault() {
		if (Search.indent.equals(null))
			indent = "no";
		if (Search.limit == 0)
			limit = 10;
		if (Search.index.equals(null))
			index = "instance";
		if (Search.endpoint.equals(null)&&(Search.endpointuri.equals(null))){
			endpointuri = "http://dbpedia.org/sparql";
			endpoint="Dbpedia";
			}
		if (Search.endpoint.equals(null)&&(!Search.endpointuri.equals(null))){
			endpointuri = "http://dbpedia.org/sparql";
			endpoint="RANDOM";
			}
	}

	public DirectoryReader readerFromIndex(NIOFSDirectory dir) throws IOException {
		return DirectoryReader.open(dir);
	}
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

	public List<Result> search_norank(IndexSearcher searcher, String queryString, Integer limit) throws IOException {
		if (limit == 0)
			limit = 10;
		BooleanQuery query = queryFromString(queryString);

		int hitsPerPage = limit * TIMES_MORE_RESULTS;
		Sort sort = new Sort(new SortField("label", SortField.Type.STRING, true));
		TopFieldDocs hits = searcher.search(query, hitsPerPage, sort);

		List<Result> res = new ArrayList<Result>();

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Result result = new Result(doc.get("url"), doc.get("label"));
			res.add(result);
		}
		return res;
	}

	public static List<Result> searchEndpoint(String index, String term, int limit) throws IOException {
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
			// flag = true;
			break;

		}

		Search tester;

		try {
			tester = new Search();

			resultlist = tester.search(searcher, term, 0);

		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(resultlist, new Comparator<Result>() {
			public int compare(Result a, Result b) {
				return a.getPagerank().compareTo(b.getPagerank());
			}
		});
		resultlist.subList(limit + 1, resultlist.size()).clear();
		return resultlist;
	}

	private static Logger log = LoggerFactory.getLogger(Search.class);

	public static void Indexgenerator(String epname, String ep) {
		Handler_SparqlEndpoint.generateIndex(epname,ep, "class");
		Handler_SparqlEndpoint.generateIndex(epname,ep, "instances");
		Handler_SparqlEndpoint.generateIndex(epname,ep, "property");

	}

	public static void main(String[] args) throws IOException {

		// final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
		Search.Indexgenerator("Dbpedia", "http://dbpedia.org/sparql");
		port(8080);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// List<Result> query_result =
		// SearchLuceneLabel.searchEndpoint("instance", "berlin");
		// String x = JsonLdOutput.getJsonLDoutput(query_result,"instance");

		// gson.toJson(query_result);

		get("/search", (req, res) -> {
			try {
				Search.index = req.queryParams("index");
				Search.searchlabel = req.queryParams("query");
				Search.indent = req.queryParams("indent");
				Search.limit = Integer.parseInt(req.queryParams("limit"));
				Search.endpoint = req.queryParams("endpoint");
				Search.endpointuri= req.queryParams("epuri");

			} catch (Exception e2) {
				// TODO: handle exception
				Search.setDefault();
//				Search.Indexgenerator(endpoint,endpointuri);
			}

			List<Result> query_result = Search.searchEndpoint(index, searchlabel, limit);
			res.type("application/json");
			log.info("Responding to Query");

			if (indent.toUpperCase().equals("YES")) {
				System.out.println(JsonLdOutput.getJsonLDoutput(query_result, index, limit));
				return JsonLdOutput.getJsonLDoutput(query_result, index, limit);
			}

			return gson.toJson(query_result);
			/*
			 * if (flag == true) {
			 * 
			 * log.info("Choosing default index"); flag = false; if
			 * (indent.toUpperCase().equals("YES")) {
			 * System.out.println(JsonLdOutput.getJsonLDoutput(query_result,
			 * index, limit)); return JsonLdOutput.getJsonLDoutput(query_result,
			 * index, limit); }
			 * 
			 * return gson.toJson(query_result);
			 * 
			 * } else { if (indent.toUpperCase().equals("YES")) {
			 * System.out.println(JsonLdOutput.getJsonLDoutput(query_result,
			 * index, limit)); return JsonLdOutput.getJsonLDoutput(query_result,
			 * index, limit); } else
			 * 
			 * return gson.toJson(query_result); }
			 */
		});

	}

}
