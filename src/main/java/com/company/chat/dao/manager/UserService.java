package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.InvalidInputException;
import com.company.chat.dao.exceptions.ItemAlreadyExistException;
import com.company.chat.dao.exceptions.ItemNotFoundException;
import com.company.chat.dao.model.ItemType;
import com.company.chat.dao.model.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends AbstractService implements IService<User> {

	@Inject
	private HashOperations<String, String, User> hashOperations;

	@Override
	public String save(User user) throws Exception {
		if(user == null || user.getUsername() == null){
			throw new InvalidInputException("Invalid User: username is null");
		}
		// Only "Logged" users are into UserCache.
		// This check is to avoid multiple login with the same username
		// TODO: future improvements (e.g. use username as KEY)
		String newUsername = user.getUsername();
		Optional<User> optUser = hashOperations.entries(Constants.USER_CACHE).values().stream()
				.filter(el -> el.getUsername().equalsIgnoreCase(newUsername)).findFirst();

		if (optUser.isPresent()) {
			throw new ItemAlreadyExistException("Cannot Save: username is not unique");
		}

		return makeTransactionalInsert(ItemType.USER, user);
	}

	@Override
	public User update(User user) throws Exception {
		if(user == null || user.getId() == null || user.getUsername() == null){
			throw new InvalidInputException("Invalid User: id or username is null");
		}
		if (hashOperations.get(Constants.USER_CACHE, user.getId()) == null) {
			throw new ItemNotFoundException("Cannot Update: User ID not found");
		}

		// No audit record is needed here
		hashOperations.put(Constants.USER_CACHE, user.getId(), user);
		log.info("Updated User={}", user);

		return user;
	}

	@Override
	public User findById(String id) {
		return hashOperations.get(Constants.USER_CACHE, id);
	}

	@Override
	public Map<String, User> findAll() {
		return hashOperations.entries(Constants.USER_CACHE);
	}

	@Override
	public User delete(String id) throws Exception {

		User u = findById(id);
		if (u == null) {
			throw new ItemNotFoundException("Cannot Delete: User ID not found");
		}
		makeTransactionalDelete(ItemType.USER, u);

		return u;
	}
}
