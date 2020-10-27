package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Inject
	private ValueOperations<String, Object> valueOperations;

	String getNextId(ItemType itemType) {
		Long newId = null;
		switch (itemType) {
		case MESSAGE:
			newId = valueOperations.increment(Constants.INDEX_MESSAGE);
			break;
		case USER:
			newId = valueOperations.increment(Constants.INDEX_USER);
			break;
		case AUDIT:
			newId = valueOperations.increment(Constants.INDEX_AUDIT);
			break;
		}
		log.debug("NextId is {}", newId);
		return String.valueOf(newId);
	}

	protected String makeTransactionalInsert(ItemType itemType, AbstractItem generalItem) throws FailedCRUDException {

		OperationType opType = OperationType.INSERT;
		String nextAuditId = getNextId(ItemType.AUDIT);
		validateId(opType, nextAuditId);

		String cache;
		Audit audit;
		String nextItemId;

		switch (itemType) {
		case MESSAGE:
			nextItemId = getNextId(ItemType.MESSAGE);
			validateId(opType, nextItemId);
			cache = Constants.MESSAGE_CACHE;
			Message m = (Message) generalItem;
			m.setId(nextItemId);
			audit = new Audit(nextAuditId, m.getTimestamp(), opType, cache, m.getId(), m.toString());
			break;

		case USER:
			nextItemId = getNextId(ItemType.USER);
			validateId(opType, nextItemId);
			cache = Constants.USER_CACHE;
			User u = (User) generalItem;
			u.setId(nextItemId);
			audit = new Audit(nextAuditId, LocalDateTime.now(), opType, cache, u.getId(), u.toString());
			break;
		default:
			String msg = String.format("Unsupported Transactional-Insert operation for ItemType=%s", itemType);
			throw new FailedCRUDException(opType, msg);
		}

		//execute a Transactional operation: This will contain the results of all operations in the transaction
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
			public List<Object> execute(RedisOperations operations) throws DataAccessException {

				// (1) Start Transactional operation
				operations.multi();

				// (2) Save data
				operations.opsForHash().put(cache, nextItemId, generalItem);
				operations.opsForHash().put(Constants.AUDIT_CACHE, audit.getId(), audit);

				// (3) Execute operations
				return operations.exec();
			}
		});

		if (txResults == null || txResults.size() != 2) {
			String msg = String.format("Cannot save Item: error while inserting into cache. TxResults is %s", txResults);
			throw new FailedCRUDException(opType, msg);
		}

		return nextItemId;
	}

	protected void makeTransactionalDelete(ItemType itemType, AbstractItem generalItem) throws FailedCRUDException {

		log.debug("Try deleting ItemType={}", itemType);

		OperationType opType = OperationType.DELETE;
		String nextAuditId = getNextId(ItemType.AUDIT);
		validateId(opType, nextAuditId);

		String itemId;
		String cache;
		Audit audit;
		switch (itemType) {
		case MESSAGE:
			cache = Constants.MESSAGE_CACHE;
			Message m = (Message) generalItem;
			itemId = m.getId();
			audit = new Audit(nextAuditId, LocalDateTime.now(), opType, cache, m.getId(), m.toString());
			break;
		case USER:
			cache = Constants.USER_CACHE;
			User u = (User) generalItem;
			itemId = u.getId();
			audit = new Audit(nextAuditId, LocalDateTime.now(), opType, cache, u.getId(), u.toString());
			break;
		default:
			String msg = String.format("Unsupported Transactional-Delete operation for ItemType=%s", itemType);
			throw new FailedCRUDException(opType, msg);
		}

		//execute a Transactional operation: This will contain the results of all operations in the transaction
		List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
			public List<Object> execute(RedisOperations operations) throws DataAccessException {

				// (1) Start Transactional operation
				operations.multi();

				// (2) Delete data
				operations.opsForHash().delete(cache, itemId);
				operations.opsForHash().put(Constants.AUDIT_CACHE, audit.getId(), audit);

				// (3) Execute operations
				return operations.exec();
			}
		});

		if (txResults == null || txResults.size() != 2) {
			String msg = String
					.format("Cannot delete ItemType=%s with ItemId=%s: error while deleting into cache. TxResults is %s", itemType, itemId,
							txResults);
			throw new FailedCRUDException(opType, msg);
		}
	}

	private void validateId(OperationType operationType, String nextId) throws FailedCRUDException {
		if (nextId == null || nextId.isEmpty()) {
			throw new FailedCRUDException(operationType, "Cannot obtain the next Audit Id");
		}
	}
}
