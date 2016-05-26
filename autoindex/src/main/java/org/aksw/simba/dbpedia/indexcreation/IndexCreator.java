package org.aksw.simba.dbpedia.indexcreation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

	public static final Version LUCENE_VERSION = Version.LUCENE_44;

	private Analyzer urlAnalyzer;
	private Analyzer literalAnalyzer;

	private DirectoryReader ireader;
	private IndexWriter iwriter;
	private MMapDirectory directory;



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

//	private void addDocumentToIndex(IndexWriter iwriter, String subject, String predicate, String object, boolean isUri)
//			throws IOException {
//		Document doc = new Document();
//		log.debug(subject + " " + predicate + " " + object);
//		doc.add(new StringField(TripleIndex.FIELD_NAME_SUBJECT, subject, Store.YES));
//		doc.add(new StringField(TripleIndex.FIELD_NAME_PREDICATE, predicate, Store.YES));
//		if (isUri) {
//			doc.add(new StringField(TripleIndex.FIELD_NAME_OBJECT_URI, object, Store.YES));
//		} else {
//			doc.add(new TextField(TripleIndex.FIELD_NAME_OBJECT_LITERAL, object, Store.YES));
//		}
//		iwriter.addDocument(doc);
//	}
//
	public void close() throws IOException {
		if (ireader != null) {
			ireader.close();
		}
		if (directory != null) {
			directory.close();
		}
	}
//
//	private class OnlineStatementHandler extends RDFHandlerBase {
//		@Override
//		public void handleStatement(Statement st) {
//			String subject = st.getSubject().stringValue();
//			String predicate = st.getPredicate().stringValue();
//			String object = st.getObject().stringValue();
//			try {
//				addDocumentToIndex(iwriter, subject, predicate, object, st.getObject() instanceof URI);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	
}