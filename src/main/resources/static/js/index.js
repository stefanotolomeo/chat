'use strict';

document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)

var stompClient = null;
var name = null;

function connect(event) {
	name = document.querySelector('#name').value.trim();

	if (name) {
		document.querySelector('#welcome-page').classList.add('hidden');
		document.querySelector('#dialogue-page').classList.remove('hidden');

		var socket = new SockJS('/websocketApp');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, connectionSuccess);
	}
	event.preventDefault();
}

function connectionSuccess() {
	stompClient.subscribe('/topic/mychat', onMessageReceived);

	/*stompClient.send("/app/chat.newUser", {}, JSON.stringify({
		sender : name,
		type : 'newUser'
	}))*/

	stompClient.send("/app/chat.newUser", {}, JSON.stringify({
    		name : name
    	}))

}

function sendMessage(event) {
	var messageContent = document.querySelector('#chatMessage').value.trim();

	if (messageContent && stompClient) {
		var chatMessage = {
			userSenderId : name,
			content : document.querySelector('#chatMessage').value
		};

		stompClient.send("/app/chat.sendMessage", {}, JSON
				.stringify(chatMessage));
		document.querySelector('#chatMessage').value = '';
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var msg = JSON.parse(payload.body);

	var messageElement = document.createElement('li');

    var contentToBeShown
    console.log(msg)
	switch(msg.type) {
	    case "USER":
            if(msg.action == "JOIN"){
                messageElement.classList.add('event-data');
            	contentToBeShown = msg.user.name + ' has joined the chat';
            } else if(msg.action == "LEAVE"){
                messageElement.classList.add('event-data');
                contentToBeShown = msg.user.name + ' has left the chat';
            } else {
                console.log("Unrecognized Action for UserMessage="+msg)
            }
            break;

      case "MESSAGE":
            messageElement.classList.add('message-data');
            contentToBeShown = msg.message.content;

        	var element = document.createElement('i');
        	var text = document.createTextNode(contentToBeShown);
        	element.appendChild(text);

        	messageElement.appendChild(element);

        	var usernameElement = document.createElement('span');
        	var usernameText = document.createTextNode(msg.message.userSenderId);
        	usernameElement.appendChild(usernameText);
        	messageElement.appendChild(usernameElement);
        break;
      default:
        // code block
        console.log("Error: unrecognized Message type")
    }

    console.log("Content to be shown is: "+contentToBeShown)

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(contentToBeShown);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	document.querySelector('#messageList').appendChild(messageElement);
	document.querySelector('#messageList').scrollTop = document
			.querySelector('#messageList').scrollHeight;

}