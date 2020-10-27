package com.company.chat.testconfig;

import com.company.chat.config.DaoConfig;
import com.company.chat.config.WebConfig;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = { TestContext.class, WebConfig.class, DaoConfig.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest
// @TestPropertySource(locations = "classpath:application-unit.yml")
@ActiveProfiles("unit")
public abstract class BaseWebTest {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@MockBean
	public RedisTemplate<String, Object> redisTemplate;

	@MockBean
	public HashOperations<String, String, User> userHashOperations;

	@MockBean
	public HashOperations<String, String, Message> messageHashOperations;

	@MockBean
	public HashOperations<String, String, Audit> auditHashOperations;

	@MockBean
	public ValueOperations<String, Object> valueOperations;

	protected String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
