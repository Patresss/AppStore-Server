var stompClient = null;
var username = null;

$( document ).ready(function() {
	connect();

	$("#remove-image").on('click', function(e){
		e.preventDefault();
		$("#image-contener").remove();
		$("#ghimage").val("")
	});

	$("#remove-icon").on('click', function(e){
		e.preventDefault();
		$("#icon-contener").remove();
		$("#ghicon").val("")
	});

	$("#message-input").on('keydown', function(e){
		var keyCode = e.keyCode || e.which;
		if (keyCode === 13) { 
		    e.preventDefault();
		}	
	});
	
	$("#message-input").on('keyup', function(e){
		var keyCode = e.keyCode || e.which;
		if (keyCode === 13) { 
		    e.preventDefault();
		    var message = $('#message-input').val();
	    	stompClient.send("/app/chat", {},
	        	JSON.stringify({'name':username, 'text':message}));
        	$('#message-input').val('');
		}	
	});
	
});

window.onbeforeunload = function (e) {
	disconnect();
}

function showMessageOutput(messageOutput) {    
    if(username == messageOutput.from){
    	var message = '<div class="message"><img src="static/images/svg/user.svg" class="rounded float-left rounded-circle m-2" alt="user" width="50"><p class="mb-0"><small class="text-secondary">'+messageOutput.from+'</small></p><p>'+messageOutput.text+'</p><div class="clearfix"></div></div>';
    } else {
    	var message = '<div class="message"><img src="static/images/svg/user.svg" class="rounded float-right rounded-circle m-2" alt="user" width="50"><p class="text-right mb-0"><small class="text-secondary">'+messageOutput.from+'</small></p><p class="text-right">'+messageOutput.text+'</p><div class="clearfix"></div></div>';
    }
    
    $('#messenger-contener').append(message);
    var $target = $('#messenger-contener'); 
	$target.animate({scrollTop: '+=66px'}, 200);
}

function connect() {;
    username = "Unknow"+(Math.floor((Math.random(1000)*1000)));
    var socket = new SockJS('localhost:8080/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/messages', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function disconnect() {
    if(stompClient != null) {
    	stompClient.send("/app/chat", {},
	        	JSON.stringify({'name':username, 'text':'Logged out'}));
        stompClient.disconnect();
    }
}