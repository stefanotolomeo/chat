package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.exceptions.InvalidInputException;
import com.company.chat.dao.exceptions.ItemNotFoundException;
import com.company.chat.dao.model.ItemType;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.OperationType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MessageService extends AbstractService implements IService<Message> {

	@Inject
	private HashOperations<String, String, Message> hashOperations;

	/**
	 * @param message: the message to be saved
	 * @return the ID of the saved message
	 * @throws FailedCRUDException: if at least one error occurs
	 */
	public String save(final Message message) throws Exception {

		if(message== null || message.getSender() == null || message.getContent() == null || message.getTopic() == null){
			throw new InvalidInputException("Invalid Message: sender, content or topic is null");
		}

		// (1) Set Timestamp for Message
		LocalDateTime timestamp = LocalDateTime.now();
		message.setTimestamp(timestamp);

		log.debug("Saving Message={}", message);

		// (2) Execute a Transactional operation
		return makeTransactionalInsert(ItemType.MESSAGE, message);

	}

	@Override
	public Message update(Message item) throws Exception {
		throw new FailedCRUDException(OperationType.UPDATE, "Unsupported operation: cannot manually update a MESSAGE record");
	}

	public Message findById(final String id) {
		return hashOperations.get(Constants.MESSAGE_CACHE, id);
	}

	public Map<String, Message> findAll() {
		return hashOperations.entries(Constants.MESSAGE_CACHE);
	}

	// Delete employee by id operation.
	public Message delete(String id) throws Exception {

		Message m = findById(id);
		if (m == null) {
			throw new ItemNotFoundException("Cannot Delete: Message ID not found");
		}

		makeTransactionalDelete(ItemType.MESSAGE, m);

		return m;
	}

}