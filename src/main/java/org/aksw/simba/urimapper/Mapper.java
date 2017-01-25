package org.aksw.simba.urimapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aksw.simba.dataformat.MapperDataStruct;
import org.aksw.simba.dataformat.NGramStruct;
import org.aksw.simba.index.ESInterface;

public class Mapper implements MapperInterface {
	private final Map<Integer, MapperDataStruct> mappings = new HashMap<Integer, MapperDataStruct>();

	@Override
	public Map<Integer, MapperDataStruct> getMappings() {
		return this.mappings;
	}

	@Override
	public void BuildMappings(ESInterface node, List<NGramStruct> ngramlist) {

		List<String> tempURIs;
		List<String> tempLabels;

		for (NGramStruct ngram : ngramlist) {
			tempURIs = new ArrayList<String>();
			tempLabels = new ArrayList<String>();
			QuerySearch buildquery = new QuerySearch(node, ngram);
			MapperDataStruct mapping = new MapperDataStruct(
					buildquery.getURIList(), buildquery.getLabelList(),
					buildquery.getEnergyScoreList());
			mappings.put(ngram.getIndex(), mapping);
		}
	}
}
