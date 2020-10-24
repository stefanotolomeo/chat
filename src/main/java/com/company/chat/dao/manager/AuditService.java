package com.company.chat.dao.manager;

import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.OperationType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class AuditService extends AbstractService implements IService<Audit> {

	private final String AUDIT_CACHE = "AUDIT";

	private HashOperations<String, String, Audit> hashOperations;

	@PostConstruct
	private void intializeHashOperations() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public String save(Audit audit) throws FailedCRUDException {
		throw new FailedCRUDException(OperationType.DELETE, "Unsupported operation: cannot manually insert an AUDIT record");
	}

	@Override
	public Audit findById(String id) {
		return hashOperations.get(AUDIT_CACHE, id);
	}

	@Override
	public Map<String, Audit> findAll() {
		return hashOperations.entries(AUDIT_CACHE);
	}

	@Override
	public Audit delete(String id) throws FailedCRUDException {
		throw new FailedCRUDException(OperationType.DELETE, "Unsupported operation: cannot manually delete an AUDIT record");
	}
}
