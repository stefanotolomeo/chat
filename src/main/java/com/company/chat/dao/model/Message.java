package com.company.chat.dao.model;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
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

	private String userSenderId;

	private String userReceiverId;

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

	public String getUserSenderId() {
		return userSenderId;
	}

	public void setUserSenderId(String userSenderId) {
		this.userSenderId = userSenderId;
	}

	public String getUserReceiverId() {
		return userReceiverId;
	}

	public void setUserReceiverId(String userReceiverId) {
		this.userReceiverId = userReceiverId;
	}

	@Override
	public String toString() {
		return "Message{" + "id='" + id + '\'' + ", timestamp=" + timestamp + ", content='" + content + '\'' + ", userSenderId='"
				+ userSenderId + '\'' + ", userReceiverId='" + userReceiverId + '\'' + '}';
	}
}