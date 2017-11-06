$( document ).ready(function() {
	/*
	// ------- auto load content by fragment url
	var hash = window.location.hash;
	if(!window.location.hash) {
		hash = '#home';
	}
	var link = $('#v-pills-tab').find('a[href="'+hash+'"]');
	$(link).tab('show');
	//====================


	// ------- load content by click menu
	$('#v-pills-tab a').on('click', function (e) {
	  $(this).tab('show')
	})
	//==================
	*/



        var stompClient = null;
	            
	$('#connect').on('click', function(){
	  connect();
	})
});

function connect() {
    console.log('Connectiong...');
    var socket = new SockJS('127.0.0.1:8080/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    //setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var from = document.getElementById('from').value;
    var text = document.getElementById('text').value;
    stompClient.send("/app/chat", {},
        JSON.stringify({'name':from, 'text':text}));
}

function showMessageOutput(messageOutput) {
    // var response = document.getElementById('response');
    // var p = document.createElement('p');
    // p.style.wordWrap = 'break-word';
    // p.appendChild(document.createTextNode(messageOutput.from + ": "
    //     + messageOutput.text + " (" + messageOutput.time + ")"));
    // response.appendChild(p);
    console.log(messageOutput.text);
}

