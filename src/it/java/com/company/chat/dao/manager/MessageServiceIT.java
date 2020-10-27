package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.FailedCRUDException;
import com.company.chat.dao.exceptions.InvalidInputException;
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

public class MessageServiceIT extends BaseIT {

	@Inject
	private MessageService messageService;

	private final String topic = "chat_topic";

	// Message 1
	private final String id_1 = "1";
	private final LocalDateTime timestamp_1 = LocalDateTime.of(2020, Month.OCTOBER, 22, 20, 45);
	private final String sender_1 = "1";
	private final String content_1 = "Ehi, you!";

	// Message 2
	private final String id_2 = "2";
	private final LocalDateTime timestamp_2 = LocalDateTime.of(2020, Month.OCTOBER, 22, 20, 50);
	private final String sender_2 = "2";
	private final String content_2 = "Hi, man!";

	private final Message m1 = new Message(id_1, timestamp_1, content_1, sender_1, topic);
	private final Message m2 = new Message(id_2, timestamp_2, content_2, sender_2, topic);

	@BeforeEach
	void setup() {
		clearAllCaches();
	}

	@AfterEach
	void tearDown() {
		clearAllCaches();
	}

	@DisplayName("FindAll Test: retrieve all Messages into cache")
	@Test
	void findAll_Test() {

		// (1) Empty map
		Map<String, Message> res_1 = messageService.findAll();
		Assertions.assertNotNull(res_1);
		Assertions.assertTrue(res_1.isEmpty());

		// (2) Add one message, then check it
		messageHashOperations.put(Constants.MESSAGE_CACHE, id_1, m1);
		Map<String, Message> res_2 = messageService.findAll();
		Assertions.assertNotNull(res_2);
		Assertions.assertFalse(res_2.isEmpty());
		makeAssertionsOnMessages(m1, res_2.get(m1.getId()));

		// (3) Add the second user, then check it
		messageHashOperations.put(Constants.MESSAGE_CACHE, id_2, m2);
		Map<String, Message> res_3 = messageService.findAll();
		Assertions.assertNotNull(res_3);
		Assertions.assertEquals(2, res_3.size());
		makeAssertionsOnMessages(m1, res_3.get(m1.getId()));
		makeAssertionsOnMessages(m2, res_3.get(m2.getId()));
	}

	@DisplayName("FindByID Test: retrieve Message by ID")
	@Test
	void findBy_Test() {

		// (1) Empty map
		Message res_1 = messageService.findById(m1.getId());
		Assertions.assertNull(res_1);

		// (2) Add one message, then check it
		messageHashOperations.put(Constants.MESSAGE_CACHE, id_1, m1);
		Message res_2 = messageService.findById(m1.getId());
		Assertions.assertNotNull(res_2);
		makeAssertionsOnMessages(m1, res_2);

	}

	@DisplayName("Delete Test: delete a Message by ID")
	@Test
	void delete_Test() throws Exception {

		// (1) ID to be deleted not found
		ItemNotFoundException e = Assertions.assertThrows(ItemNotFoundException.class, () -> messageService.delete(m1.getId()));
		Assertions.assertEquals("Cannot Delete: Message ID not found", e.getMessage());

		// (2) Add one user, the check it
		messageHashOperations.put(Constants.MESSAGE_CACHE, id_1, m1);
		Message res_1 = messageService.delete(m1.getId());
		makeAssertionsOnMessages(m1, res_1);

		// (3) Ensure the audit record has been inserted
		List<Audit> auditList = auditHashOperations.values(Constants.AUDIT_CACHE);
		Assertions.assertNotNull(auditList);
		Assertions.assertFalse(auditList.isEmpty());
		Audit expected_audit = new Audit(null, null, OperationType.DELETE, Constants.MESSAGE_CACHE, m1.getId(), m1.toString());
		makeAssertionsOnAudit(expected_audit, auditList.get(0));
	}

	@DisplayName("Save Test: save a new Message")
	@Test
	void save_Test() throws Exception {

		// (1) Invalid Message: null
		InvalidInputException e1 = Assertions.assertThrows(InvalidInputException.class, () -> messageService.save(null));
		Assertions.assertEquals("Invalid Message: sender, content or topic is null", e1.getMessage());

		// (2) Invalid Message: null sender, content and topic
		Message invalidMessage = new Message(null, null, null, null, null);
		InvalidInputException e2 = Assertions.assertThrows(InvalidInputException.class, () -> messageService.save(invalidMessage));
		Assertions.assertEquals("Invalid Message: sender, content or topic is null", e2.getMessage());

		// (3) Invalid Message: null content and topic
		invalidMessage.setSender("Sender set");
		InvalidInputException e3 = Assertions.assertThrows(InvalidInputException.class, () -> messageService.save(invalidMessage));
		Assertions.assertEquals("Invalid Message: sender, content or topic is null", e3.getMessage());

		// (3) Invalid Message: null topic
		invalidMessage.setContent("Content set");
		InvalidInputException e4 = Assertions.assertThrows(InvalidInputException.class, () -> messageService.save(invalidMessage));
		Assertions.assertEquals("Invalid Message: sender, content or topic is null", e4.getMessage());

		// (4) Add the new Message
		Message newMessage = new Message(null, null, content_1, sender_1, topic);
		String res_1 = messageService.save(newMessage);
		Assertions.assertNotNull(res_1);
		Assertions.assertEquals(newMessage.getId(), res_1);

		// (2) Ensure the audit record has been inserted
		List<Audit> auditList = auditHashOperations.values(Constants.AUDIT_CACHE);
		Assertions.assertNotNull(auditList);
		Assertions.assertFalse(auditList.isEmpty());
		Audit expected_audit = new Audit(null, null, OperationType.INSERT, Constants.MESSAGE_CACHE, newMessage.getId(),
				newMessage.toString());
		makeAssertionsOnAudit(expected_audit, auditList.get(0));
	}

	@DisplayName("Update Test: unsupported operation")
	@Test
	void update_Test() {

		FailedCRUDException e = Assertions.assertThrows(FailedCRUDException.class, () -> messageService.update(m1));
		Assertions.assertEquals(OperationType.UPDATE, e.getOperation());
		Assertions.assertEquals("Unsupported operation: cannot manually update a MESSAGE record", e.getMessage());
	}
}
