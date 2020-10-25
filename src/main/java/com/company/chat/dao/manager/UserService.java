package com.company.chat.dao.manager;

import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.exceptions.ItemAlreadyExistException;
import com.company.chat.dao.exceptions.ItemNotFoundException;
import com.company.chat.dao.model.ItemType;
import com.company.chat.dao.model.OperationType;
import com.company.chat.dao.model.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends AbstractService implements IService<User> {

	private HashOperations<String, String, User> hashOperations;

	@PostConstruct
	private void intializeHashOperations() {
		initializeIndexesCaches();
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public String save(User user) throws Exception {
		// Only "Logged" users are into UserCache.
		// This check is to avoid multiple login with the same username
		// TODO: future improvements (e.g. use username as KEY)
		String newUsername = user.getUsername();
		Optional<User> optUser = hashOperations.entries(USER_CACHE).values().stream().filter(el -> el.getUsername().equalsIgnoreCase(newUsername))
				.findFirst();

		if (optUser.isPresent()) {
			throw new ItemAlreadyExistException("Cannot Save: User ID not found");
		}

		return makeTransactionalInsert(ItemType.USER, user);
	}

	@Override
	public User update(User user) throws Exception {
		if(hashOperations.get(USER_CACHE, user.getId()) == null){
			throw new ItemNotFoundException("Cannot Update: User ID not found");
		}

		hashOperations.put(USER_CACHE, user.getId(), user);
		log.info("Updated User={}", user);

		return user;
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
	public User delete(String id) throws Exception {

		User u = findById(id);
		if(u == null){
			throw new ItemNotFoundException("Cannot Delete: User ID not found");
		}
		makeTransactionalDelete(ItemType.USER, u);

		return u;
	}
}
