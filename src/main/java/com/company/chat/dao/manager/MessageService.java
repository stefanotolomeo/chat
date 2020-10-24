package com.company.chat.dao.manager;

import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.model.ItemType;
import com.company.chat.dao.model.Message;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MessageService extends AbstractService implements IService<Message> {

	private HashOperations<String, String, Message> hashOperations;

	@PostConstruct
	private void intializeHashOperations() {
		initializeIndexesCaches();
		hashOperations = redisTemplate.opsForHash();
	}

	/**
	 * @param message: the message to be saved
	 * @return the ID of the saved message
	 * @throws FailedCRUDException: if at least one error occurs
	 */
	public String save(final Message message) throws FailedCRUDException {

		// (1) Set Timestamp for Message
		LocalDateTime timestamp = LocalDateTime.now();
		message.setTimestamp(timestamp);

		log.debug("Saving Message={}", message);

		// (2) Execute a Transactional operation
		return makeTransactionalInsert(ItemType.MESSAGE, message);

	}

	public Message findById(final String id) {
		return hashOperations.get(MESSAGE_CACHE, id);
	}

	public Map<String, Message> findAll() {
		return hashOperations.entries(MESSAGE_CACHE);
	}

	// Delete employee by id operation.
	public Message delete(String id) throws FailedCRUDException {

		Message m = findById(id);
		makeTransactionalDelete(ItemType.MESSAGE, m);

		return m;
	}

}