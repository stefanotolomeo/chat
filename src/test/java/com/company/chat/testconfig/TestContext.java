package com.company.chat.testconfig;

import com.company.chat.controller.MessageController;
import com.company.chat.controller.StatusController;
import com.company.chat.controller.UserController;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({})
public class TestContext {

	@MockBean
	public MessageController messageController;

	@MockBean
	public UserController userController;

	@MockBean
	public StatusController statusController;
}
