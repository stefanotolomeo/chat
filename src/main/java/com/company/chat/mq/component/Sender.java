package com.company.chat.mq.component;

import com.company.chat.dao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender extends AbstractComponent {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessage(Message message) {
		log.info("Sending Message={} to Topic={}", message, topic);
		jmsTemplate.convertAndSend(topic, message);
	}
}
