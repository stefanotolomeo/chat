package com.company.chat.controller;

import com.company.chat.dao.model.Message;
import com.company.chat.mq.component.JmsProducer;
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
	private JmsProducer jmsProducer;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Message sendMessage(@RequestBody Message message){
		log.info("Received Message={}", message);
		jmsProducer.sendMessage(message);
		// TODO: return an ack
		return message;
	}
}
