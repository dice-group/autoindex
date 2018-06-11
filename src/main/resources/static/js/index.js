$("#btn_submit").click(function(){
	var url = $("#index").val();
	var data = {};
	data["url"] = url;
	data["useLocalDataSource"] = "false";
	data["default_graph"] = "";
	data["requestType"] = "URI";
	data["userId"] = "00000000001";
	data["limit"] = $("#count").val();	
	var file = [];
	data["fileList"] = file;
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(data),
		url: "/index/create",
		timeout: 100000,
		contentType: "application/json",
		async: true,		
	})
	.done(function(data){	
		alert("Indexing done");
	})
	.fail(function(data){
		alert("Error occured");
	});
});

$("#btn_submitfile").click(function(){
	var selectedFile = document.getElementById("inputFile").files[0];
	var myForm = new FormData();
	myForm.append("file", selectedFile);
	$.ajax({
		type : "POST",
		data: myForm,
		url: "/index/uploadFile",
		timeout: 100000,
		processData: false,
		enctype :  "multipart/form-data",
		contentType: false,
		async: false,
		cache: false,
	})
	.done(function(data){ 
		alert("Indexing done");
	})
	.fail(function(data){
		alert("Error occured");
	});
});
$("#submit_labeled_url").click(function(){
	var data = {};
	var urlField = "#indexed_url";
	var labelField = "#indexed_label";
	var selectedIndexType = $("input:radio[name=optradio]:checked").val();
	var urlVal = $(urlField).val();
	var labelVal = $(labelField).val();
	data["useLocalDataSource"] = "false";
	data["requestType"] = "custom";
	data["userId"] = "00000000001";
	var file = [];
	data["fileList"] = file;
	data["keys"] = {"firstKey": urlVal ,"secondKey": labelVal,"category": selectedIndexType};
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(data),
		url: "/index/create",
		timeout: 100000,
		contentType: "application/json",
		async: true,		
	})
	.done(function(data){	
		alert("Indexing done");
	})
	.fail(function(data){
		alert("Error occured");
	});
});
