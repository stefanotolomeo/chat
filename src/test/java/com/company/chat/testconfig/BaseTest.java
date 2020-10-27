package com.company.chat.testconfig;

import com.company.chat.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = { TestContext.class, RedisConfig.class })
@ExtendWith(SpringExtension.class)
@JsonTest
// @TestPropertySource(locations = "classpath:application-unit.yml")
@ActiveProfiles("unit")
public abstract class BaseTest extends BDDMockito {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

}