package com.company.chat.websocket.component;

import com.company.chat.dao.manager.UserService;
import com.company.chat.dao.model.User;
import com.company.chat.websocket.component.model.UserAction;
import com.company.chat.websocket.component.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.inject.Inject;

@Component
public class WebSocketListener extends AbstractComponent {

	private static final Logger chatLog = LoggerFactory.getLogger("CHAT_LOGGER");

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Inject
	private UserService userService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		log.debug("Received Connection to WebSocket");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws Exception {

		log.debug("Received Disconnection to WebSocket");

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		User u = (User) headerAccessor.getSessionAttributes().get("user");
		if (u != null) {

			chatLog.info("Disconnecting User={}", u);
			String userId = u.getId();
			try {
				// (1) Delete the User
				chatLog.debug("Deleting UserId={}", userId);
				User deletedUser = userService.delete(userId);

				chatLog.debug("Successfully deleted User={}", deletedUser);
			} catch (Exception e) {
				String msg = String.format("Exception while deleting UserId=%s", userId);
				throw new Exception(msg, e);
			}

			// (2) Build and send response
			UserMessage userMessage = new UserMessage();
			userMessage.setAction(UserAction.LEAVE.name());
			userMessage.setUser(u);

			messagingTemplate.convertAndSend("/topic/mychat", userMessage);
		}
	}
}