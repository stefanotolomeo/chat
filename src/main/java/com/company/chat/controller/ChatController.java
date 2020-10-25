package com.company.chat.controller;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.manager.UserService;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.User;
import com.company.chat.mq.model.ChatMessage;
import com.company.chat.mq.model.UserAction;
import com.company.chat.mq.model.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

// This is the Controller used for WebSocket communication
@Controller
public class ChatController extends AbstractController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/mychat")
	public ChatMessage sendMessage(
			@Payload
					Message newMessage) {
		log.info("Received NewMessage={}", newMessage);
		ChatMessage chatMessage = new ChatMessage();

		try {
			// (1) Save Message/Audit record in cache
			String savedId = messageService.save(newMessage);
			newMessage.setId(savedId);

			log.info("Successfully added Message with ID={}", savedId);
		} catch (Exception e) {
			String msg = String.format("Exception while saving Message=%s", newMessage);
			log.error(msg, e);
			chatMessage.setError(e.getMessage());
			return chatMessage;
		}

		// (2) Build and return response
		chatMessage.setMessage(newMessage);
		return chatMessage;
	}

	@MessageMapping("/chat.newUser")
	@SendTo("/topic/mychat")
	public UserMessage newUser(
			@Payload
					User newUser, SimpMessageHeaderAccessor headerAccessor) {
		log.info("Received NewUser={}", newUser);

		UserMessage userMessage = new UserMessage();
		try {
			// (1) Save or retrieve the User with username
			String savedId = userService.save(newUser);
			newUser.setId(savedId);

			log.info("User with ID={}", savedId);
		} catch (Exception e) {
			String msg = String.format("Exception while saving User=%s", newUser);
			log.error(msg, e);
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