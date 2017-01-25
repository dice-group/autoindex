package org.aksw.simba.urimapper;

import java.util.List;
import java.util.Map;

import org.aksw.simba.dataformat.MapperDataStruct;
import org.aksw.simba.dataformat.NGramStruct;
import org.aksw.simba.index.ESInterface;

public interface MapperInterface {
	public void BuildMappings(ESInterface node, List<NGramStruct> ngramlist);

	public Map<Integer, MapperDataStruct> getMappings();
}
