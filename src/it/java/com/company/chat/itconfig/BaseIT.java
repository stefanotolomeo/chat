package com.company.chat.itconfig;

import com.company.chat.config.Constants;
import com.company.chat.config.RedisConfig;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Set;

@ContextConfiguration(classes = { ITContext.class, RedisConfig.class })
@ExtendWith(SpringExtension.class)
@JsonTest
// @TestPropertySource(locations = "classpath:application-unit.yml")
@ActiveProfiles("unit")
public abstract class BaseIT extends BDDMockito {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Inject
	protected HashOperations<String, String, User> userHashOperations;

	@Inject
	protected HashOperations<String, String, Message> messageHashOperations;

	@Inject
	protected HashOperations<String, String, Audit> auditHashOperations;

	protected void clearAllCaches() {
		// Clear AUDIT cache
		Set<String> auditKeys = auditHashOperations.keys(Constants.AUDIT_CACHE);
		for (String s : auditKeys) {
			auditHashOperations.delete(Constants.AUDIT_CACHE, s);
		}

		// Clear USER cache
		Set<String> userKeys = userHashOperations.keys(Constants.USER_CACHE);
		for (String s : userKeys) {
			userHashOperations.delete(Constants.USER_CACHE, s);
		}

		// Clear MESSAGE cache
		Set<String> messageKeys = messageHashOperations.keys(Constants.MESSAGE_CACHE);
		for (String s : messageKeys) {
			messageHashOperations.delete(Constants.MESSAGE_CACHE, s);
		}
	}


	protected void makeAssertionsOnUsers(User expected, User actual) {
		Assertions.assertEquals(expected.getId(), actual.getId());
		Assertions.assertEquals(expected.getUsername(), actual.getUsername());
	}

	protected void makeAssertionsOnAudit(Audit expected, Audit actual) {
		// Cannot know ID and Timestamp: no assertions for them
		Assertions.assertEquals(expected.getOperationType(), actual.getOperationType());
		Assertions.assertEquals(expected.getTable(), actual.getTable());
		Assertions.assertEquals(expected.getRecordId(), actual.getRecordId());
		Assertions.assertEquals(expected.getRecordContent(), actual.getRecordContent());
	}

	protected void makeAssertionsOnMessages(Message expected, Message actual){
		Assertions.assertEquals(expected.getId(), actual.getId());
		Assertions.assertEquals(expected.getContent(), actual.getContent());
		Assertions.assertEquals(expected.getSender(), actual.getSender());
		Assertions.assertEquals(expected.getTopic(), actual.getTopic());
	}
}