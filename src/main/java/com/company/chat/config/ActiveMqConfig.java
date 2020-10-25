package com.company.chat.config;

import com.company.chat.mq.component.Receiver;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@ComponentScan(basePackageClasses = { Receiver.class })
public class ActiveMqConfig {

	@Value("${active-mq.broker-url}")
	private String brokerUrl;

	// Beans for Message Producer
	@Bean(name = "producerFactory")
	public ConnectionFactory producerConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		return activeMQConnectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(producerConnectionFactory());
		jmsTemplate.setPubSubDomain(true);  // enable for Pub Sub to topic. Not Required for Queue.
		return jmsTemplate;
	}

	// Beans for Message Consumer
	@Bean(name = "consumerFactory")
	public ConnectionFactory consumerConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		// TODO: in a real scenario, do not user this property
		activeMQConnectionFactory.setTrustAllPackages(true);
		return activeMQConnectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(consumerConnectionFactory());
		factory.setPubSubDomain(true);
		return factory;
	}
}
