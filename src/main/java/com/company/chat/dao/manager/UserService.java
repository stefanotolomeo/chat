package com.company.chat.dao.manager;

import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.model.ItemType;
import com.company.chat.dao.model.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class UserService extends AbstractService implements IService<User> {

	private HashOperations<String, String, User> hashOperations;

	@PostConstruct
	private void intializeHashOperations() {
		initializeIndexesCaches();
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public String save(User user) throws FailedCRUDException {

		return makeTransactionalInsert(ItemType.USER, user);
	}

	@Override
	public User findById(String id) {
		return hashOperations.get(USER_CACHE, id);
	}

	@Override
	public Map<String, User> findAll() {
		return hashOperations.entries(USER_CACHE);
	}

	@Override
	public User delete(String id) throws FailedCRUDException {

		User u = findById(id);
		makeTransactionalDelete(ItemType.USER, u);

		return u;
	}
}
