package com.company.chat.dao.model;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

@Component
public class Message extends AbstractItem implements Serializable {

	private static final long serialVersionUID = 5061588246570750234L;

	private String id;

	@JsonIgnore
	private LocalDateTime timestamp;

	private String content;

	private String sender;

	private String topic;

	public Message(String id, LocalDateTime timestamp, String content, String sender, String topic) {
		this.id = id;
		this.timestamp = timestamp;
		this.content = content;
		this.sender = sender;
		this.topic = topic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "Message{" + "id='" + id + '\'' + ", timestamp=" + timestamp + ", content='" + content + '\'' + ", sender='" + sender + '\''
				+ ", topic='" + topic + '\'' + '}';
	}
}