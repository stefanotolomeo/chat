package com.company.chat.dao.manager;

import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class UserService implements IService<User> {

	private final String USER_CACHE = "USER";

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, Message> hashOperations;

	// This annotation makes sure that the method needs to be executed after
	// dependency injection is done to perform any initialization.
	@PostConstruct
	private void intializeHashOperations() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(User item) {
		// TODO
	}

	@Override
	public User findById(String id) {
		// TODO
		return null;
	}

	@Override
	public Map<String, User> findAll() {
		// TODO
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO
	}
}
