package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.exceptions.ItemNotFoundException;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.OperationType;
import com.company.chat.itconfig.BaseIT;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class AuditServiceIT extends BaseIT {

	@Inject
	private AuditService auditService;

	private final String topic = "chat_topic";

	// Audit 1
	private final String id_1 = "1";
	private final LocalDateTime timestamp_1 = LocalDateTime.of(2020, Month.OCTOBER, 22, 20, 45);
	private final OperationType opType_1 = OperationType.INSERT;
	private final String table_1 = Constants.USER_CACHE;
	private final String recordId_1 = "1";
	private final String recordContent_1 = "to string di user";

	// Audit 1
	private final String id_2 = "2";
	private final LocalDateTime timestamp_2 = LocalDateTime.of(2020, Month.OCTOBER, 22, 20, 50);
	private final OperationType opType_2 = OperationType.DELETE;
	private final String table_2 = Constants.MESSAGE_CACHE;
	private final String recordId_2 = "4";
	private final String recordContent_2 = "to string di message";

	private final Audit a1 = new Audit(id_1, timestamp_1, opType_1, table_1, recordId_1, recordContent_1);
	private final Audit a2 = new Audit(id_2, timestamp_2, opType_2, table_2, recordId_2, recordContent_2);

	@BeforeEach
	void setup() {
		clearAllCaches();
	}

	@AfterEach
	void tearDown() {
		clearAllCaches();
	}

	@DisplayName("FindAll Test: retrieve all Audit into cache")
	@Test
	void findAll_Test() {

		// (1) Empty map
		Map<String, Audit> res_1 = auditService.findAll();
		Assertions.assertNotNull(res_1);
		Assertions.assertTrue(res_1.isEmpty());

		// (2) Add one audit, then check it
		auditHashOperations.put(Constants.AUDIT_CACHE, id_1, a1);
		Map<String, Audit> res_2 = auditService.findAll();
		Assertions.assertNotNull(res_2);
		Assertions.assertFalse(res_2.isEmpty());
		makeAssertionsOnAudit(a1, res_2.get(a1.getId()));

		// (3) Add the second audit, then check it
		auditHashOperations.put(Constants.AUDIT_CACHE, id_2, a2);
		Map<String, Audit> res_3 = auditService.findAll();
		Assertions.assertNotNull(res_3);
		Assertions.assertEquals(2, res_3.size());
		makeAssertionsOnAudit(a1, res_3.get(a1.getId()));
		makeAssertionsOnAudit(a2, res_3.get(a2.getId()));
	}

	@DisplayName("FindByID Test: retrieve Audit by ID")
	@Test
	void findBy_Test() {

		// (1) Empty map
		Audit res_1 = auditService.findById(a1.getId());
		Assertions.assertNull(res_1);

		// (2) Add one audit, then check it
		auditHashOperations.put(Constants.AUDIT_CACHE, id_1, a1);
		Audit res_2 = auditService.findById(a1.getId());
		Assertions.assertNotNull(res_2);
		makeAssertionsOnAudit(a1, res_2);

	}

	@DisplayName("Delete Test: unsupported operation")
	@Test
	void delete_Test() {

		FailedCRUDException e = Assertions.assertThrows(FailedCRUDException.class, () -> auditService.delete(a1.getId()));
		Assertions.assertEquals(OperationType.DELETE, e.getOperation());
		Assertions.assertEquals("Unsupported operation: cannot manually delete an AUDIT record", e.getMessage());
	}

	@DisplayName("Save Test: unsupported operation")
	@Test
	void save_Test() {

		FailedCRUDException e = Assertions.assertThrows(FailedCRUDException.class, () -> auditService.save(a1));
		Assertions.assertEquals(OperationType.INSERT, e.getOperation());
		Assertions.assertEquals("Unsupported operation: cannot manually save an AUDIT record", e.getMessage());
	}

	@DisplayName("Update Test: unsupported operation")
	@Test
	void update_Test() {

		FailedCRUDException e = Assertions.assertThrows(FailedCRUDException.class, () -> auditService.update(a1));
		Assertions.assertEquals(OperationType.UPDATE, e.getOperation());
		Assertions.assertEquals("Unsupported operation: cannot manually update an AUDIT record", e.getMessage());
	}
}
