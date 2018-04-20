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
	var url_validation = false;
	var data = {};
	var labeled_url_field = "#labeled_index";
	var lebeled_url = $(labeled_url_field).val();
    var is_url_valid = validate(lebeled_url);
    if(is_url_valid == true){
    $(this).parent().find('#labeled_index').removeClass('alert-danger');
    console.log("Post Funcationality");
    }
    else{
    $(this).parent().find('#labeled_index').addClass('alert-danger');
    }
	
});
    function validate(lebeled_url) {
        var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        if (pattern.test(lebeled_url)) {
            return true;
        } 
            return false;
    }