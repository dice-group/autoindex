package org.aksw.simba.dataformat;

import java.util.ArrayList;
import java.util.List;

public class MapperDataStruct {
	private List<String> URIList = new ArrayList<String>();
	private List<String> LabelList = new ArrayList<String>();

	public MapperDataStruct(List<String> URIs, List<String> Labels, List<Double> EnergyScore) {
		this.URIList.addAll(URIs);
		this.LabelList.addAll(Labels);
	}

	public void setURIList(List<String> URIs) {
		this.URIList.clear();
		this.URIList.addAll(URIs);
	}

	public void setLabelList(List<String> Labels) {
		this.LabelList.clear();
		this.LabelList.addAll(Labels);
	}

	public List<String> getURIList() {
		return this.URIList;
	}

	public List<String> getLabelList() {
		return this.LabelList;
	}

}
