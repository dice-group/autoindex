package org.aksw.simba.Initializer;

import java.util.List;
import java.util.Map;

import org.aksw.simba.dataformat.MapperDataStruct;
import org.aksw.simba.dataformat.NGramStruct;
import org.aksw.simba.dataformat.ResultDataStruct;

public interface InitializerInterface {
	public void initiate(Map<Integer, MapperDataStruct> urimaps,
			List<NGramStruct> ngrams);

	public List<ResultDataStruct> getResultsList();
}
