package com.company.chat.dao.manager;

import com.company.chat.config.Constants;
import com.company.chat.dao.exceptions.InvalidInputException;
import com.company.chat.dao.exceptions.ItemAlreadyExistException;
import com.company.chat.dao.exceptions.ItemNotFoundException;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.OperationType;
import com.company.chat.dao.model.User;
import com.company.chat.itconfig.BaseIT;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class UserServiceIT extends BaseIT {

	@Inject
	private UserService userService;

	private final String id_1 = "1";
	private final String id_2 = "2";

	private final String username_1 = "username_1";
	private final String username_2 = "username_2";

	private final User u1 = new User(id_1, username_1);
	private final User u2 = new User(id_2, username_2);

	@BeforeEach
	void setup() {
		clearAllCaches();
	}

	@AfterEach
	void tearDown() {
		clearAllCaches();
	}

	@DisplayName("FindAll Test: retrieve all Users into cache")
	@Test
	void findAll_Test() {

		// (1) Empty map
		Map<String, User> res_1 = userService.findAll();
		Assertions.assertNotNull(res_1);
		Assertions.assertTrue(res_1.isEmpty());

		// (2) Add one user, then check it
		userHashOperations.put(Constants.USER_CACHE, id_1, u1);
		Map<String, User> res_2 = userService.findAll();
		Assertions.assertNotNull(res_2);
		Assertions.assertFalse(res_2.isEmpty());
		makeAssertionsOnUsers(u1, res_2.get(u1.getId()));

		// (3) Add the second user, then check it
		userHashOperations.put(Constants.USER_CACHE, id_2, u2);
		Map<String, User> res_3 = userService.findAll();
		Assertions.assertNotNull(res_3);
		Assertions.assertEquals(2, res_3.size());
		makeAssertionsOnUsers(u1, res_3.get(u1.getId()));
		makeAssertionsOnUsers(u2, res_3.get(u2.getId()));
	}

	@DisplayName("FindByID Test: retrieve User by ID")
	@Test
	void findBy_Test() {

		// (1) Empty map
		User res_1 = userService.findById(u1.getId());
		Assertions.assertNull(res_1);

		// (2) Add one user, then check it
		userHashOperations.put(Constants.USER_CACHE, id_1, u1);
		User res_2 = userService.findById(u1.getId());
		Assertions.assertNotNull(res_2);
		makeAssertionsOnUsers(u1, res_2);

	}

	@DisplayName("Delete Test: delete a User by ID")
	@Test
	void delete_Test() throws Exception {

		// (1) ID to be deleted not found
		ItemNotFoundException e = Assertions.assertThrows(ItemNotFoundException.class, () -> userService.delete(u1.getId()));
		Assertions.assertEquals("Cannot Delete: User ID not found", e.getMessage());

		// (2) Add one user, the check it
		userHashOperations.put(Constants.USER_CACHE, id_1, u1);
		User res_1 = userService.delete(u1.getId());
		makeAssertionsOnUsers(u1, res_1);

		// (3) Ensure the audit record has been inserted
		List<Audit> auditList = auditHashOperations.values(Constants.AUDIT_CACHE);
		Assertions.assertNotNull(auditList);
		Assertions.assertFalse(auditList.isEmpty());
		Audit expected_audit = new Audit(null, null, OperationType.DELETE, Constants.USER_CACHE, u1.getId(), u1.toString());
		makeAssertionsOnAudit(expected_audit, auditList.get(0));
	}

	@DisplayName("Update Test: update a User")
	@Test
	void update_Test() throws Exception {

		// (1) Invalid User: null
		InvalidInputException e1 = Assertions.assertThrows(InvalidInputException.class, () -> userService.update(null));
		Assertions.assertEquals("Invalid User: id or username is null", e1.getMessage());

		// (2) Invalid User: null ID and username
		User invalidUser = new User(null, null);
		InvalidInputException e2 = Assertions.assertThrows(InvalidInputException.class, () -> userService.update(invalidUser));
		Assertions.assertEquals("Invalid User: id or username is null", e2.getMessage());

		// (3) Invalid User: null ID
		invalidUser.setUsername("Valid username");
		InvalidInputException e3 = Assertions.assertThrows(InvalidInputException.class, () -> userService.update(invalidUser));
		Assertions.assertEquals("Invalid User: id or username is null", e3.getMessage());

		// (4) Invalid User: null ID
		invalidUser.setUsername(null);
		invalidUser.setId("9999");
		InvalidInputException e4 = Assertions.assertThrows(InvalidInputException.class, () -> userService.update(invalidUser));
		Assertions.assertEquals("Invalid User: id or username is null", e4.getMessage());

		// (5) User ID to be deleted not found
		ItemNotFoundException e5 = Assertions.assertThrows(ItemNotFoundException.class, () -> userService.update(u1));
		Assertions.assertEquals("Cannot Update: User ID not found", e5.getMessage());

		// (6) Add one user, the check it
		userHashOperations.put(Constants.USER_CACHE, id_1, u1);
		User res_1 = userService.update(u1);
		makeAssertionsOnUsers(u1, res_1);

	}

	@DisplayName("Save Test: save a new User")
	@Test
	void save_Test() throws Exception {

		// (1) Invalid User: null
		InvalidInputException e1 = Assertions.assertThrows(InvalidInputException.class, () -> userService.save(null));
		Assertions.assertEquals("Invalid User: username is null", e1.getMessage());

		// (2) Invalid User: null username
		User invalidUser = new User("8888", null);
		InvalidInputException e2 = Assertions.assertThrows(InvalidInputException.class, () -> userService.save(invalidUser));
		Assertions.assertEquals("Invalid User: username is null", e2.getMessage());

		// (3) Add the new User
		User newUser = new User(null, username_1);
		String res_1 = userService.save(newUser);
		Assertions.assertNotNull(res_1);
		Assertions.assertEquals(newUser.getId(), res_1);

		// (4) Same username, throw exception
		ItemAlreadyExistException e3 = Assertions.assertThrows(ItemAlreadyExistException.class, () -> userService.save(u1));
		Assertions.assertEquals("Cannot Save: username is not unique", e3.getMessage());

		// (5) Ensure the audit record has been inserted
		List<Audit> auditList = auditHashOperations.values(Constants.AUDIT_CACHE);
		Assertions.assertNotNull(auditList);
		Assertions.assertFalse(auditList.isEmpty());
		Audit expected_audit = new Audit(null, null, OperationType.INSERT, Constants.USER_CACHE, newUser.getId(), newUser.toString());
		makeAssertionsOnAudit(expected_audit, auditList.get(0));
	}

}
