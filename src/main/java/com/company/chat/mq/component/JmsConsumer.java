package com.company.chat.mq.component;

import com.company.chat.dao.model.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class JmsConsumer extends AbstractComponent implements MessageListener {

	@Override
	@JmsListener(destination = "${active-mq.topic}")
	public void onMessage(javax.jms.Message message) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Message employee = (Message) objectMessage.getObject();
			//do additional processing
			log.info("Received Message: " + employee.toString());
		} catch (Exception e) {
			log.error("Received Exception : " + e);
		}

	}
}
