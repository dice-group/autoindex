var entity_input = "";
var property_input = "";
var class_input = "";
var isEntityChanged = false;
var isPropertyChanged = false;
var isClassChanged = false;

function handleChangeBehavior(textAreaId , collapsibleId){
	entity_input = document.getElementById(textAreaId).value;
	document.getElementById(collapsibleId).click(); 
};
function handleCancelBehavior(textAreaId , collapsibleId){
	document.getElementById(collapsibleId).click(); 
	document.getElementById(textAreaId).value = entity_input; 
};
var changeEntity = function(){
	handleChangeBehavior("entity_textarea" , "collapsible_entity");
	isEntityChanged=true;
};
var changeProperty = function(){
	handleChangeBehavior("property_textarea" , "collapsible_property");
	isPropertyChanged = true;
};
var changeClass = function(){
	handleChangeBehavior("class_textarea" , "collapsible_class");
	isClassChanged=true;
};
var handleEntityCancel = function(){
	handleCancelBehavior("entity_textarea" , "collapsible_entity");
};
var handlePropertyCancel = function(){
	handleCancelBehavior("property_textarea" , "collapsible_property");
};
var handleClassCancel = function(){
	handleCancelBehavior("class_textarea" , "collapsible_class");
};

function createRequestParameters(url, requestType){
	var data = {};
	if(url){
		data["url"] = url;
		var endPointParameters={"url":url, "isEntityCustomized":isEntityChanged , "isPropertyCustomized":isPropertyChanged, "isClassCustomized":isClassChanged,"entitySelectQuery":entity_input , "propertySelectQuery":property_input , "classSelectQuery":class_input };
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
	sendAjax("/getClassQuery" , "class_textarea" , class_input);
};

var getPropertyQuery = function(){	
	sendAjax("/getPropertyQuery" , "property_textarea" , property_input);
};

var getEntityQuery = function(){
	sendAjax("/getEntityQuery" , "entity_textarea" , entity_input);
}
var addEventListeners = function(){
	document.getElementById("entity_save").addEventListener("click", changeEntity);
	document.getElementById("prop_save").addEventListener("click", changeProperty);
	document.getElementById("class_save").addEventListener("click", changeClass);
	document.getElementById("entity_cancel").addEventListener("click", handleEntityCancel);
	document.getElementById("prop_cancel").addEventListener("click", changeProperty);
	document.getElementById("class_cancel").addEventListener("click", changeClass);
};

var toggleButtonHandler = function(){
	var coll = document.getElementsByClassName("collapsible");
	var i;
	for (i = 0; i < coll.length; i++) {
	    coll[i].addEventListener("click", function() {
	        this.classList.toggle("active");
	        var content = this.nextElementSibling;
	        if (content.style.display === "block") {
	            content.style.display = "none";
	        } else {
	            content.style.display = "block";
	        }
	    });
	}
};

$(document).ready(function() {
	getEntityQuery();
	getPropertyQuery();
	getClassQuery();
	toggleButtonHandler();
	addEventListeners();

});