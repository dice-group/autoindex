package org.aksw.simba.searcher.search;

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
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aksw.simba.searcher.indexcreation.Handler_SparqlEndpoint;
import org.aksw.simba.searcher.output.JsonLdOutput;
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
	private static Logger log = LoggerFactory.getLogger(Search.class);
	boolean flag = false;
	public final String APP_PACKAGE = "org.aksw.simba.dbpedia";
	final int TIMES_MORE_RESULTS = 10;
	String index = null;
	String indent = null;
	int limit = 0;
	String searchlabel = null;
	String endpoint = null;
	String endpointuri = null;

	private void setDefault() {
		if (this.indent == null)
			this.indent = "no";
		if (this.limit == 0)
			this.limit = 10;
		if (this.index == null)
			this.index = "instance";
		if (this.endpoint == null) {
			endpoint = "RANDOM";
		}
	}

	public DirectoryReader readerFromIndex(NIOFSDirectory dir)
			throws IOException {
		return DirectoryReader.open(dir);
	}

	public BooleanQuery queryFromString(String queryString)
			throws UnsupportedEncodingException {
		BooleanQuery query = new BooleanQuery();
		PhraseQuery phraseQuery = new PhraseQuery();
		String[] words = URLDecoder.decode(queryString, "UTF-8").toLowerCase()
				.split(" ");
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

	public List<Result> search(IndexSearcher searcher, String queryString,
			Integer limit) throws IOException {
		if (limit == 0)
			limit = 10;
		BooleanQuery query = queryFromString(queryString);
		int hitsPerPage = limit * TIMES_MORE_RESULTS;
		Sort sort = new Sort(SortField.FIELD_SCORE, new SortedNumericSortField(
				"pagerank_sort", SortField.Type.FLOAT, true));
		TopFieldDocs hits = searcher.search(query, hitsPerPage, sort);
		List<Result> res = new ArrayList<Result>();
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Result result = new Result(doc.get("url"), doc.get("label"),
					Double.parseDouble(doc.get("pagerank")));
			res.add(result);
		}
		return res;
	}

	public List<Result> search_norank(IndexSearcher searcher,
			String queryString, Integer limit) throws IOException {
		if (limit == 0)
			limit = 10;
		BooleanQuery query = queryFromString(queryString);
		int hitsPerPage = limit * TIMES_MORE_RESULTS;
		Sort sort = new Sort(
				new SortField("label", SortField.Type.STRING, true));
		TopFieldDocs hits = searcher.search(query, hitsPerPage, sort);
		List<Result> res = new ArrayList<Result>();
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Result result = new Result(doc.get("url"), doc.get("label"));
			res.add(result);
		}
		return res;
	}

	public List<Result> searchEndpoint(String index, String term, int limit,
			String endp) throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream(
				"src/main/java/properties/autoindex.properties");
		prop.load(input);
		IndexSearcher searcher = null;
		List<Result> resultlist = null;
		switch (index.toUpperCase()) {
		case "CLASS": {
			String indexDir = prop.getProperty("folderWithIndexFiles");
			indexDir = indexDir + File.separator + endp + File.separator
					+ "index_class";
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(
					indexDir)));
			searcher = new IndexSearcher(reader);
			break;
		}
		case "INSTANCE": {
			String indexDir = prop.getProperty("folderWithIndexFiles");
			indexDir = indexDir + File.separator + endp + File.separator
					+ "index_instance";
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(
					indexDir)));
			searcher = new IndexSearcher(reader);
			break;
		}
		case "PROPERTY": {
			String indexDir = prop.getProperty("folderWithIndexFiles");
			indexDir = indexDir + File.separator + endp + File.separator
					+ "index_property";
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(
					indexDir)));
			searcher = new IndexSearcher(reader);
			break;
		}
		default:
			String indexDir = prop.getProperty("folderWithIndexFiles");
			indexDir = indexDir + File.separator + endp + File.separator
					+ "index_instance";
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(
					indexDir)));
			searcher = new IndexSearcher(reader);
			flag = true;
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

	public void Indexgenerator(String epname, String ep) {
		Handler_SparqlEndpoint hs = new Handler_SparqlEndpoint();

		hs.generateIndex(epname, ep, "class");
		hs.generateIndex(epname, ep, "instances");
		hs.generateIndex(epname, ep, "property");
	}

	public static boolean checkEndpoint(String url) throws IOException {

		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			int code = connection.getResponseCode();
			if (code == 200) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		Search searcher = new Search();
		searcher.Indexgenerator("Dbpedia", "http://dbpedia.org/sparql");
		port(8080);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		get("/search",
				(req, res) -> {
					searcher.index = req.queryParams("index");
					searcher.searchlabel = req.queryParams("query");
					searcher.indent = req.queryParams("indent");
					searcher.limit = Integer.parseInt(req.queryParams("limit"));
					searcher.endpoint = req.queryParams("endpoint");
					searcher.endpointuri = req.queryParams("epuri");
					searcher.setDefault();
					if (checkEndpoint(searcher.endpointuri)) {
						log.info("Valid endpoint");
					} else {
						log.info("Invalid endpoint switched to default");
						searcher.endpointuri = "http://dbpedia.org/sparql";
						searcher.endpoint = "Dbpedia";
					}

					List<Result> query_result = searcher.searchEndpoint(
							searcher.index, searcher.searchlabel,
							searcher.limit, searcher.endpoint);
					res.type("application/json");
					log.info("Responding to Query");
					log.info("Result siye " + query_result.size());
					if (searcher.flag == true) {
						log.info("Choosing default index");
						searcher.flag = false;
						if (searcher.indent.toUpperCase().equals("YES")) {
							if (query_result.size() > 1) {
								return JsonLdOutput.getJsonLDoutput(
										query_result, searcher.index,
										searcher.limit);
							} else {
								return gson.toJson("NO MATCH FOUND");
							}
						} else {
							return gson.toJson(query_result);
						}
					} else {
						if (searcher.indent.toUpperCase().equals("YES")) {
							if (query_result.size() > 1) {
								return JsonLdOutput.getJsonLDoutput(
										query_result, searcher.index,
										searcher.limit);
							} else {
								return gson.toJson("NO MATCH FOUND");
							}
						} else {
							return gson.toJson(query_result);
						}
					}
				});

	}

}
