package org.aksw.simba.searcher.indexcreation;

import java.io.File;
import com.hp.hpl.jena.graph.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class IndexCreator {
	private static Logger log = LoggerFactory.getLogger(IndexCreator.class);

	@SuppressWarnings("deprecation")
	public static final Version LUCENE_VERSION = Version.LUCENE_44;

	private Analyzer urlAnalyzer;
	private Analyzer literalAnalyzer;
	private DirectoryReader ireader;
	private IndexWriter iwriter;
	private MMapDirectory directory;

	@SuppressWarnings("deprecation")
	public void createTextIndex(ResultSet results, String idxDirectory, String baseURI) {
		try {
			urlAnalyzer = new SimpleAnalyzer(LUCENE_VERSION);
			literalAnalyzer = new LiteralAnalyzer(LUCENE_VERSION);
			Map<String, Analyzer> mapping = new HashMap<String, Analyzer>();
			mapping.put("url", urlAnalyzer);
			mapping.put("label", literalAnalyzer);
			PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(urlAnalyzer, mapping);
			File indexDirectory = new File(idxDirectory);
			indexDirectory.mkdir();
			directory = new MMapDirectory(indexDirectory);
			IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION, perFieldAnalyzer);
			iwriter = new IndexWriter(directory, config);
			iwriter.commit();
			while (results.hasNext()) {
				QuerySolution row = results.next();
				String url = row.getResource("type").getURI();
				String label = row.getLiteral("label").getString();
				Document doc = new Document();
				doc.add(new StringField("url", url, Store.YES));
				doc.add(new TextField("label", label, Store.YES));
				iwriter.addDocument(doc);
			}
			iwriter.commit();
			iwriter.close();
			ireader = DirectoryReader.open(directory);
		} catch (Exception e) {
			log.error("Error while creating TripleIndex.", e);
		}
	}

	@SuppressWarnings("deprecation")
	public void createIndex(ResultSet results, String idxDirectory, String baseURI) {
		try {
			urlAnalyzer = new SimpleAnalyzer(LUCENE_VERSION);
			literalAnalyzer = new LiteralAnalyzer(LUCENE_VERSION);
			Map<String, Analyzer> mapping = new HashMap<String, Analyzer>();
			mapping.put("url", urlAnalyzer);
			mapping.put("label", literalAnalyzer);
			mapping.put("pagerank", urlAnalyzer);
			mapping.put("pagerank_sort", urlAnalyzer);
			PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(urlAnalyzer, mapping);
			File indexDirectory = new File(idxDirectory);
			indexDirectory.mkdir();
			directory = new MMapDirectory(indexDirectory);
			IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION, perFieldAnalyzer);
			iwriter = new IndexWriter(directory, config);
			iwriter.commit();
			while (results.hasNext()) {
				QuerySolution row = results.next();
				String url = row.getResource("type").getURI();
				String label = row.getLiteral("label").getString();
				String pagerank = row.getLiteral("v").getString();
				Document doc = new Document();
				doc.add(new StringField("url", url, Store.YES));
				doc.add(new TextField("label", label, Store.YES));
				doc.add(new DoubleField("pagerank", Double.parseDouble(pagerank), Store.YES));
				doc.add(new FloatDocValuesField("pagerank_sort", Float.parseFloat(pagerank)));
				iwriter.addDocument(doc);
			}
			iwriter.commit();
			iwriter.close();
			ireader = DirectoryReader.open(directory);
		} catch (Exception e) {
			log.error("Error while creating TripleIndex.", e);
		}
	}

	@SuppressWarnings("deprecation")
	public void createDumpIndex(Set<Node> results, String idxDirectory, String baseURI) {
		try {
			urlAnalyzer = new SimpleAnalyzer(LUCENE_VERSION);
			literalAnalyzer = new LiteralAnalyzer(LUCENE_VERSION);
			Map<String, Analyzer> mapping = new HashMap<String, Analyzer>();
			mapping.put("url", urlAnalyzer);
			mapping.put("label", literalAnalyzer);
			PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(urlAnalyzer, mapping);
			File indexDirectory = new File(idxDirectory);
			indexDirectory.mkdir();
			directory = new MMapDirectory(indexDirectory);
			IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION, perFieldAnalyzer);
			iwriter = new IndexWriter(directory, config);
			iwriter.commit();
			for (Node row : results) {
				String url = row.getURI();
				String label = row.getLocalName();
				Document doc = new Document();
				doc.add(new StringField("url", url, Store.YES));
				doc.add(new StringField("label", label, Store.YES));
				iwriter.addDocument(doc);
			}
			iwriter.commit();
			iwriter.close();
			ireader = DirectoryReader.open(directory);
		} catch (Exception e) {
			log.error("Error while creating Index.", e);
		}
	}

	public void close() throws IOException {
		if (ireader != null) {
			ireader.close();
		}
		if (directory != null) {
			directory.close();
		}
	}

}