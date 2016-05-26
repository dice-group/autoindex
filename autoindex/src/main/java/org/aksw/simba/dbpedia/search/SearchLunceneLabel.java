package org.aksw.simba.dbpedia.search;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.URLDecoder;
import org.apache.lucene.util.Version;

import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import com.google.gson.Gson;

public class SearchLunceneLabel
{
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

	public List<Result> search(IndexSearcher searcher, String queryString, Integer limit) {
		if (limit == 0)
			limit = 10;
		BooleanQuery query = queryFromString(queryString);
		int hitsPerPage = limit * TIMES_MORE_RESULTS;
		Sort sort = Sort(SortField.FIELD_SCORE, SortedNumericSortField("pagerank_sort", SortField.Type.FLOAT, true));
		TopFieldDocs hits = searcher.search(query, hitsPerPage, sort);

		List<Result> res = new ArrayList<Result>();

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Result result = new Result (doc.get("url"), doc.get("label"), Double.parseDouble(doc.get("pagerank")));
			res.add(result);
		}
		return res;
	}
	public static void main(String[] args) {
		
	}

}
