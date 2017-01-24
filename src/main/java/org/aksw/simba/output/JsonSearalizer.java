package org.aksw.simba.output;

import java.util.List;

import org.aksw.simba.dataformat.Result;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSearalizer {
	Gson gson;
	String json;

	public JsonSearalizer() {
		// TODO Auto-generated constructor stub
		gson = new GsonBuilder().setPrettyPrinting().create();
		json = null;
	}

	public String getJsonOutput(List<Result> query_result) {
		json += gson.toJson(query_result);
		return json;
	}
}