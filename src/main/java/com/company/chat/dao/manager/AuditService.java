package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.OperationType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

@Service
public class AuditService extends AbstractService implements IService<Audit> {

	@Inject
	private HashOperations<String, String, Audit> hashOperations;

	@Override
	public String save(Audit audit) throws Exception {
		throw new FailedCRUDException(OperationType.INSERT, "Unsupported operation: cannot manually save an AUDIT record");
	}

	@Override
	public Audit update(Audit item) throws Exception {
		throw new FailedCRUDException(OperationType.UPDATE, "Unsupported operation: cannot manually update an AUDIT record");
	}

	@Override
	public Audit findById(String id) {
		return hashOperations.get(Constants.AUDIT_CACHE, id);
	}

	@Override
	public Map<String, Audit> findAll() {
		return hashOperations.entries(Constants.AUDIT_CACHE);
	}

	@Override
	public Audit delete(String id) throws Exception {
		throw new FailedCRUDException(OperationType.DELETE, "Unsupported operation: cannot manually delete an AUDIT record");
	}
}
