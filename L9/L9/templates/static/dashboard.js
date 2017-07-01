//Transforms
var transforms = {
		"statistics": [
        [
        {"<>":"tr","html":[
            {"<>":"th","class":"left","html":"${name}"},
            {"<>":"th","class":"right","html":"${value}"}
          ]}
          ]
		],

		"management": [
[
  {"<>":"tr","html":[
      {"<>":"th","class":"left","html":"${heapName}"},
      {"<>":"th","class":"right","html":"${heapSize}"}
    ]},
  {"<>":"tr","html":[
      {"<>":"th","class":"left","html":"${memoryName}"},
      {"<>":"th","class":"right","html":[
          {"<>":"select","id":"policySelect","html":[
              {"<>":"option","value":"LRU","html":"LRU"},
              {"<>":"option","value":"LFU","html":"LFU"},
              {"<>":"option","value":"FIFO","html":"FIFO"}
            ]}
        ]}
    ]},
  {"<>":"tr","html":[
      {"<>":"th","class":"left","html":"${timeName}"},
      {"<>":"th","class":"right","html":[
          {"<>":"input","type":"number","id":"time","min":"0","value":"${time}","html":""}
        ]}
    ]},
  {"<>":"tr","html":[
      {"<>":"th","class":"center","colspan":"2","html":[
          {"<>":"button","type":"button","id":"apply","html":"Apply"}
        ]}
    ]}
]
]
	};

function init() {
$.getJSON('/statistics.json', function(data) {
 $('#infoTable').json2html(data, transforms.statistics);

 $.getJSON('/management.json', function(data) {
 $('#infoTable').json2html(data, transforms.management);

 $('#policySelect').val(data.policy);

 $('#apply').click(function(e) {
   $.ajax({
       url: '/management.json',
       type: "POST",
       dataType: "json",
       data: JSON.stringify({
                               policy:$('#policySelect').val(),
                               time:parseInt($('#time').val())
                               }),
       contentType: "application/json; charset=UTF-8"
   });
 });

});

});
}

function update() {
$.getJSON('/statistics.json', function(data) {
var table = document.getElementById('infoTable');

for(var i = 1; i < data.length; i += 1){
table.rows[i].cells.item(1).innerHTML = data[i].value;
  }
})
.fail(function(jqXHR, textStatus, errorThrown) { clearInterval(updater); });
}

init();
var updater = setInterval(update, 1000);