package com.company.chat.mq.component;

import com.company.chat.dao.model.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class Receiver extends AbstractComponent implements MessageListener {

	@Override
	@JmsListener(destination = "${active-mq.topic}")
	public void onMessage(javax.jms.Message jmsMessage) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
			Message message = (Message) objectMessage.getObject();
			//do additional processing
			log.info("Received Message: " + message.toString());
		} catch (Exception e) {
			String msg = String.format("Exception while receiving Message on topic=%s", topic);
			log.error(msg, e);
		}

	}
}
