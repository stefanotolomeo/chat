'use strict';

var activeUsers = {}

function loadUsers( jQuery ) {
    $.get("http://localhost:10091/api/redis/user/allByUsername", function(data, status){
        console.log(data)
        activeUsers = data
      });
}

$(document).ready(loadUsers);

document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)
// $('#welcomeForm').on('submit', connect, true)
// $('#dialogueForm').on('submit', sendMessage, true)


var stompClient = null;
var name = null;

const WEB_SOCKET_APP_NAME = '/websocketApp'

const TOPIC_NAME = "mychat"
const TOPIC_PATH = '/topic/' + TOPIC_NAME

const NEW_USER_ENDPOINT = "/app/chat.newUser"
const SEND_MESSAGE_ENDPOINT = "/app/chat.sendMessage"

function connect(event) {
	name = document.querySelector('#name').value.trim().toLowerCase();
    console.log("........ ActiveUsers")
    console.log(activeUsers)
    console.log("Current Name is: "+name)
    console.log(activeUsers[name])
	if(!name){
	    console.log("Invalid Username. Enter a value")
	    return
	}

	if(activeUsers[name]){
        console.log("Username already in use")
        document.querySelector('#usernameError').classList.remove('hidden');
        event.preventDefault();
    } else {
        document.querySelector('#usernameError').classList.add('hidden');
        document.querySelector('#welcome-page').classList.add('hidden');

        var socket = new SockJS(WEB_SOCKET_APP_NAME);
        stompClient = Stomp.over(socket);

        stompClient.connect({}, connectionSuccess);
        event.preventDefault();
    }
}

function connectionSuccess() {
	stompClient.subscribe(TOPIC_PATH, onMessageReceived);

	stompClient.send(NEW_USER_ENDPOINT, {}, JSON.stringify({
    		username : name
    	}))

}

function sendMessage(event) {
	var messageContent = document.querySelector('#chatMessage').value.trim();

	if (messageContent && stompClient) {
		var chatMessage = {
			sender : name,
			content : document.querySelector('#chatMessage').value,
			topic : TOPIC_NAME
		};

		stompClient.send(SEND_MESSAGE_ENDPOINT, {}, JSON
				.stringify(chatMessage));
		document.querySelector('#chatMessage').value = '';
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var msg = JSON.parse(payload.body);

	var messageElement = document.createElement('li');

    var isError = false
    var contentToBeShown
    console.log(msg)
    console.log("-----> ActiveUsers List is")
    console.log(activeUsers)
	switch(msg.type) {
	    case "USER":
	        const currentUser = msg.user.username
            if(msg.action == "JOIN"){
                messageElement.classList.add('event-data');
            	contentToBeShown = msg.user.username + ' has joined the chat';

                // CurrentUser is a new Logged User
                document.querySelector('#welcome-page').classList.add('hidden');
                document.querySelector('#dialogue-page').classList.remove('hidden');

                activeUsers[currentUser] = currentUser;

            } else if(msg.action == "LEAVE"){
                messageElement.classList.add('event-data');
                contentToBeShown = msg.user.username + ' has left the chat';

                delete activeUsers[currentUser];
            } else {
                // For UserMessage an error occurred if action does not match
                console.log("ERROR for UserMessage="+msg)
                isError = true;

                delete activeUsers[currentUser];

            }
            break;

      case "MESSAGE":
            if(msg.message.error){
             // For ChatMessage an error occurred if Error is not empty
             console.log("ERROR for ChatMessage="+msg)
             isError = true;

             // TODO: manage
            }
            messageElement.classList.add('message-data');
            contentToBeShown = msg.message.content;

        	var element = document.createElement('i');
        	var text = document.createTextNode(contentToBeShown);
        	element.appendChild(text);

        	messageElement.appendChild(element);

        	var usernameElement = document.createElement('span');
        	var usernameText = document.createTextNode(msg.message.sender);
        	usernameElement.appendChild(usernameText);
        	messageElement.appendChild(usernameElement);
        break;
      default:
        console.log("Error: unrecognized Message type")
    }

    console.log("<----- ActiveUsers List is")
    console.log(activeUsers)
    if(isError){
        console.log("An error occurred")
        return
    }

    var textElement = document.createElement('p')
    var messageText = document.createTextNode(contentToBeShown)
    textElement.appendChild(messageText)

    messageElement.appendChild(textElement)

    document.querySelector('#messageList').appendChild(messageElement)
    document.querySelector('#messageList').scrollTop =
        document.querySelector('#messageList').scrollHeight

}