package com.company.chat.dao.manager;

import com.company.chat.dao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class MessageService implements IService<Message> {

	private final String MESSAGE_CACHE = "MESSAGE";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, Message> hashOperations;

	@PostConstruct
	private void intializeHashOperations() {
		hashOperations = redisTemplate.opsForHash();
	}

	// Save operation.
	public void save(final Message employee) {
		hashOperations.put(MESSAGE_CACHE, employee.getId(), employee);
	}

	// Find by employee id operation.
	public Message findById(final String id) {
		return (Message) hashOperations.get(MESSAGE_CACHE, id);
	}

	// Find all employees' operation.
	public Map<String, Message> findAll() {
		return hashOperations.entries(MESSAGE_CACHE);
	}

	// Delete employee by id operation.
	public void delete(String id) {
		hashOperations.delete(MESSAGE_CACHE, id);
	}

}