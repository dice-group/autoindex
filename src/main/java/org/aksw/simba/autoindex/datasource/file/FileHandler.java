package org.aksw.simba.autoindex.datasource.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aksw.simba.autoindex.es.model.Entity;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.PipedRDFIterator;
import org.apache.jena.riot.lang.PipedRDFStream;
import org.apache.jena.riot.lang.PipedTriplesStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHandler{
	private static final Logger log = LoggerFactory
            .getLogger(FileHandler.class);

	
	public String getFileType(File file) throws IOException {
		String type = FilenameUtils.getExtension(file.getCanonicalPath());
		return type;
	}
	
	public ArrayList<Entity> indexInputFile(String fileName) throws IOException {
		ArrayList<Entity> entity_list = null;
		File file = new File(fileName);
		String type = getFileType(file);
		log.debug("Filetype=" + type);
		entity_list = indexRDFData(file);
		return entity_list;
	}
	
	public ArrayList<Entity> generateOutputEntities(PipedRDFIterator<Triple> iter){
		ArrayList<Entity> entity_list = new ArrayList<Entity>();
		while (iter.hasNext()) {
			Triple next = iter.next();
			Entity entity = new Entity(next.getSubject().toString() , next.getObject().toString());
			entity_list.add(entity);
		}
		return entity_list;
	}
	public ArrayList<Entity> indexRDFData(File file) throws IOException {
		String fileName = file.getCanonicalPath();
		PipedRDFIterator<Triple> iter = new PipedRDFIterator<>();
        final PipedRDFStream<Triple> inputStream = new PipedTriplesStream(iter);
        // PipedRDFStream and PipedRDFIterator need to be on different threads
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Create a runnable for our parser thread
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                // Call the parsing process.
                RDFDataMgr.parse(inputStream, fileName);
            }
        };
        // Start the parser on another thread
        executor.submit(parser);
        // We will consume the input on the main thread here
        // We can now iterate over data as it is parsed, parsing only runs as
        // far ahead of our consumption as the buffer size allows
        ArrayList<Entity> entity_list = generateOutputEntities(iter);
        return entity_list;
    }
	
}