function createRequestParameters(url, requestType){
	var data = {};
	if(url){
		data["url"] = url;
	}
	data["useLocalDataSource"] = "false";
	data["default_graph"] = "";
	data["requestType"] = requestType;
	data["userId"] = "00000000001";
	return data;
}

function sendRequest(data , isMultipart){
	var asyncFlag = true;
	var params = {
		type : "POST",
		dataType: "text",
		data: JSON.stringify(data),
		url: "/index/create",
		timeout: 100000,
		contentType: "application/json",
		async: asyncFlag
	};
	if(isMultipart === true){
		params.async = false;
		params.enctype = "multipart/form-data";
		params.processData = false;
		params.url="/index/uploadFile";
		params.contentType=false;
		params.cache=false;
	}
	$.ajax(params)
	.done(function(data){	
		alert("Indexing done");
	})
	.fail(function(data){
		alert("Error occured");
	});
}
$("#btn_submit").click(function(){
	var url = $("#index").val();
	var data = createRequestParameters(url , "URI");
	data["limit"] = $("#count").val();	
	sendRequest(data , false);
});

$("#btn_submitfile").click(function(){
	var selectedFile = document.getElementById("inputFile").files[0];
	var myForm = new FormData();
	myForm.append("file", selectedFile);
	sendRequest(myForm , true);
});
$("#submit_labeled_url").click(function(){
	var urlField = "#indexed_url";
	var labelField = "#indexed_label";
	var selectedIndexType = $("input:radio[name=optradio]:checked").val();
	var urlVal = $(urlField).val();
	var labelVal = $(labelField).val();
	var data = createRequestParameters("" , "custom");
	data["keys"] = {"firstKey": labelVal ,"secondKey": urlVal,"category": selectedIndexType};
	sendRequest(data , false);
});
