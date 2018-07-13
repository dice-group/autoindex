var entityInput = "";
var propertyInput = "";
var classInput = "";
var isEntityChanged = false;
var isPropertyChanged = false;
var isClassChanged = false;

function handleChangeBehavior(textAreaId , collapsibleId){
	var input = document.getElementById(textAreaId).value;
	document.getElementById(collapsibleId).click(); 
	return input;
};
function handleCancelBehavior(textAreaId , collapsibleId){
	document.getElementById(collapsibleId).click(); 
	return document.getElementById(textAreaId).value; 
};
var changeEntity = function(){
	entityInput = handleChangeBehavior("entity_textarea" , "collapsible_entity");
	isEntityChanged=true;
};
var changeProperty = function(){
	propertyInput = handleChangeBehavior("property_textarea" , "collapsible_property");
	isPropertyChanged = true;
};
var changeClass = function(){
	classInput = handleChangeBehavior("class_textarea" , "collapsible_class");
	isClassChanged=true;
};
var handleEntityCancel = function(){
	entityInput = handleCancelBehavior("entity_textarea" , "collapsible_entity");
};
var handlePropertyCancel = function(){
	propertyInput = handleCancelBehavior("property_textarea" , "collapsible_property");
};
var handleClassCancel = function(){
	classInput = handleCancelBehavior("class_textarea" , "collapsible_class");
};

function createRequestParameters(url, requestType){
	var data = {};
	if(url){
		var endPointParameters={
								"url":url, 
								"isEntityCustomized":isEntityChanged, 
								"isPropertyCustomized":isPropertyChanged, 
								"isClassCustomized":isClassChanged,
								"entitySelectQuery":entityInput, 
								"propertySelectQuery":propertyInput, 
								"classSelectQuery":classInput 
								};
		console.log(endPointParameters);
		data["endPointParameters"]=endPointParameters;
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
		timeout: 100000
	};
	if(isMultipart === true){
		params.async = false;
		params.enctype = "multipart/form-data";
		params.processData = false;
		params.url="/index/uploadFile";
		params.contentType=false;
		params.cache=false;
		params.data=data;
	}
	else {
		params.dataType="text";
		params.url="/index/create";
		params.contentType="application/json";
		params.data=JSON.stringify(data);
		params.async = true;
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

function sendAjax(queryUrl , classId , id){
	$.ajax({
		type : "POST",
		dataType: "text",
		url: queryUrl,
		timeout: 100000,
		contentType: "application/json",
		async: false,		
	})
	.done(function(data){
		document.getElementById(classId).value=data;
		id=data;
	});
};

var getClassQuery = function(){	
	sendAjax("/getClassQuery" , "class_textarea" , classInput);
};

var getPropertyQuery = function(){	
	sendAjax("/getPropertyQuery" , "property_textarea" , propertyInput);
};

var getEntityQuery = function(){
	sendAjax("/getEntityQuery" , "entity_textarea" , entityInput);
};

var addEventListeners = function(){
	document.getElementById("entity_save").addEventListener("click", changeEntity);
	document.getElementById("prop_save").addEventListener("click", changeProperty);
	document.getElementById("class_save").addEventListener("click", changeClass);
	document.getElementById("entity_cancel").addEventListener("click", handleEntityCancel);
	document.getElementById("prop_cancel").addEventListener("click", handlePropertyCancel);
	document.getElementById("class_cancel").addEventListener("click", handleClassCancel);
};

var toggleCollapsibleButtons = function(){
	$("button.collapsible").on("click", function() {
        this.classList.toggle("active");
        var content = this.nextElementSibling;
        if (content.style.display === "block") {
            content.style.display = "none";
        } else {
            content.style.display = "block";
        }
	});
};

$(document).ready(function() {
	getEntityQuery();
	getPropertyQuery();
	getClassQuery();
	toggleCollapsibleButtons();
	addEventListeners();

});