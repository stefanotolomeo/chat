package com.company.chat.mq.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractComponent {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${active-mq.topic}")
	protected String topic;
}
