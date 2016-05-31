package org.aksw.simba.dbpedia.search;

import java.io.File;
import static spark.Spark.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.net.URLDecoder;
import org.apache.lucene.store.NIOFSDirectory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.aksw.simba.dbpedia.indexcreation.Handler;
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

public class SearchLuceneLabel {
	final static int TIMES_MORE_RESULTS = 10;

	public DirectoryReader readerFromIndex(NIOFSDirectory dir) throws IOException {
		return DirectoryReader.open(dir);
	}

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

	public static List<Result>  getResut(String index, String term) throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/java/properties/autoindex.properties");
		prop.load(input);
		IndexSearcher searcher = null;
		
		switch (index.toUpperCase()) {
		case "CLASS": 
		{
			
			String indexDir = prop.getProperty("index_class");
			
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);
			

			break;
			}
		case "INSTANCE":
		
		{
			
			String indexDir = prop.getProperty("index_instence");
			
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);
			break;}
		case "PROPERTIES":
		{
			String indexDir = prop.getProperty("index_properties");
			
			@SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
			searcher = new IndexSearcher(reader);
			break;
		}
		default:
			break;
		}
		
		
		SearchLuceneLabel tester;
		List<Result> resultlist=null;
		try {
			tester = new SearchLuceneLabel();

			 resultlist = tester.search(searcher, term, 0);
			

			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultlist;
	}

	public static void main(String[] args) throws IOException {
		Handler.generateIndexforClass();
		Handler.generateIndexforProperties();
		Handler.generateIndexforInstances();
		
		port(8080);
		Gson gson = new GsonBuilder().create();
		
		get("/search", (req, res) -> {
			String index = req.queryParams("Index");
			String searchlabel = req.queryParams("term");
			return gson.toJson(getResut(index, searchlabel));
		});

	}

}
