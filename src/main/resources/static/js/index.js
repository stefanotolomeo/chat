'use strict';

var activeUsers = {}

function loadUsers( jQuery ) {
    $.get("http://localhost:10091/api/redis/user/allByUsername", function(data, status){
        console.log(data)
        activeUsers = data
      });
}

$(document).ready(loadUsers);

$('#welcomeForm').submit(connect)
$('#dialogueForm').submit(sendMessage)


var stompClient = null;
var name = null;

const WEB_SOCKET_APP_NAME = '/websocketApp'

const TOPIC_NAME = "mychat"
const TOPIC_PATH = '/topic/' + TOPIC_NAME

const NEW_USER_ENDPOINT = "/app/chat.newUser"
const SEND_MESSAGE_ENDPOINT = "/app/chat.sendMessage"

function connect(event) {
	var rawName = $('#name').val();
    console.log("Current Raw Name is: "+rawName)
	if(!rawName){
	    console.log("Invalid Username. Enter a value")
	    $('#invalidUsernameError').removeClass('hidden')
	    event.preventDefault()
	    return
	}

    $('#invalidUsernameError').addClass('hidden')

	name = rawName.trim().toLowerCase()
	console.log("Current Name LOWERCASE is: "+name)

	if(activeUsers[name]){
        console.log("Username already in use")
        $('#usernameError').removeClass('hidden')
        event.preventDefault();
    } else {
        $('#usernameError').addClass('hidden');
        $('#welcome-page').addClass('hidden');

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
	var rawMessageContent = $('#chatMessage').val().trim();

	if (rawMessageContent && stompClient) {
		var chatMessage = {
			sender : name,
			content : $('#chatMessage').val(),
			topic : TOPIC_NAME
		};

		stompClient.send(SEND_MESSAGE_ENDPOINT, {}, JSON
				.stringify(chatMessage));
		$('#chatMessage').val('');
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
                $('#welcome-page').addClass('hidden');
                $('#dialogue-page').removeClass('hidden');

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

    $('#messageList').append(messageElement)
    const scrollHeight = $('#messageList').prop('scrollHeight')
    $('#messageList').scrollTop(scrollHeight)

}