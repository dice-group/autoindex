package org.aksw.simba.ngram;

import java.util.List;

import org.aksw.simba.dataformat.NGramStruct;

public interface NGramInterface {
	public void CreateNGramModel(String keyword);

	public List<NGramStruct> getNGramMod();
}