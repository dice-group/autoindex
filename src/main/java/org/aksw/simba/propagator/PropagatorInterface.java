package org.aksw.simba.propagator;

import java.util.List;

import org.aksw.simba.dataformat.ResultDataStruct;
import org.neo4j.graphdb.GraphDatabaseService;

public interface PropagatorInterface {
	public void PropagateInit(GraphDatabaseService db,
			List<ResultDataStruct> results);
}
