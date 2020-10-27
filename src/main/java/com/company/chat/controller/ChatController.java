package com.company.chat.controller;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.manager.UserService;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.User;
import com.company.chat.websocket.component.model.ChatMessage;
import com.company.chat.websocket.component.model.UserAction;
import com.company.chat.websocket.component.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

// This is the Controller used for WebSocket communication
@Controller
public class ChatController extends AbstractController {

	private static final Logger chatLog = LoggerFactory.getLogger("CHAT_LOGGER");

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@MessageMapping("/chat/sendMessage")
	@SendTo("/topic/mychat")
	public ChatMessage sendMessage(
			@Payload
					Message newMessage) {
		chatLog.info("Received NewMessage={}", newMessage);
		ChatMessage chatMessage = new ChatMessage();

		try {
			// (1) Save Message/Audit record in cache
			String savedId = messageService.save(newMessage);
			newMessage.setId(savedId);

			chatLog.info("Successfully added Message with ID={}", savedId);
		} catch (Exception e) {
			String msg = String.format("Exception while saving Message=%s", newMessage);
			chatLog.error(msg, e);
			chatMessage.setError(e.getMessage());
			return chatMessage;
		}

		// (2) Build and return response
		chatMessage.setMessage(newMessage);
		return chatMessage;
	}

	@MessageMapping("/chat/newUser")
	@SendTo("/topic/mychat")
	public UserMessage newUser(
			@Payload
					User newUser, SimpMessageHeaderAccessor headerAccessor) {
		chatLog.info("Received NewUser={}", newUser);

		UserMessage userMessage = new UserMessage();
		try {
			// (1) Save or retrieve the User with username
			String savedId = userService.save(newUser);
			newUser.setId(savedId);

			chatLog.info("User with ID={}", savedId);
		} catch (Exception e) {
			String msg = String.format("Exception while saving User=%s", newUser);
			chatLog.error(msg, e);
			userMessage.setError(e.getMessage());
			userMessage.setUser(newUser);
			return userMessage;
		}

		// (2) Save User into current session
		headerAccessor.getSessionAttributes().put("user", newUser);

		// (3) Build and return response
		userMessage.setAction(UserAction.JOIN.name());
		userMessage.setUser(newUser);
		return userMessage;
	}

}