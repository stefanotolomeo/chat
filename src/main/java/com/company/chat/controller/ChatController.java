package com.company.chat.controller;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.model.Message;
import com.company.chat.mq.component.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/chat")
public class ChatController extends AbstractController {

	@Autowired
	private Sender sender;

	@Autowired
	private MessageService messageService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sendMessage(
			@RequestBody
					Message message) {
		log.info("Received Message={}", message);

		String outcome;
		try {
			// (1) Save Message/Audit record in cache
			String savedId = messageService.save(message);

			// (2) Send message
			sender.sendMessage(message);

			// (3) Set the Outcome
			outcome = "Successfully sent Message with ID = " + savedId;

		} catch (Exception e) {
			String msg = String.format("Exception while saving Message=%s", message);
			log.error(msg, e);
			outcome = "Unexpected Internal Error";
		}

		return outcome;
	}
}
