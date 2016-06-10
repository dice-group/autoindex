package org.aksw.simba.dbpedia.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.net.URLDecoder;
import org.apache.lucene.store.NIOFSDirectory;

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

public class SearchLuceneLabel {
	public static final String APP_PACKAGE = "org.aksw.simba.dbpedia";
	final static int TIMES_MORE_RESULTS = 10;

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

}
