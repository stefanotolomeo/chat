package com.company.chat.mq.component;

import com.company.chat.controller.AbstractController;
import com.company.chat.dao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer extends AbstractController {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${active-mq.topic}")
	private String topic;

	public void sendMessage(Message message){
		try{
			log.info("Attempting Send message to Topic: "+ topic);
			jmsTemplate.convertAndSend(topic, message);
		} catch(Exception e){
			log.error("Recieved Exception during send Message: ", e);
		}
	}
}
