package com.company.chat.controller;

import com.company.chat.config.Constants;
import com.company.chat.dao.model.Audit;
import com.company.chat.dao.model.OperationType;
import com.company.chat.testconfig.BaseWebTest;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class StatusControllerTest extends BaseWebTest {

	@Inject
	private StatusController statusController;

	@Inject
	private MockMvc mockMvc;

	private final static String BASE_URI = "/api/status//audit/";
	private final static String GET_ALL_URI = BASE_URI + "all";

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

	private final Map<String, Audit> auditMap = new HashMap<String, Audit>() {
		private static final long serialVersionUID = -1718177775945603652L;

		{
			put(id_1, a1);
			put(id_2, a2);
		}
	};

	@BeforeEach
	void setup() {

	}

	@AfterEach
	void tearDown() {

	}

	@DisplayName("FindAll Test: retrieve all Messages into cache")
	@Test
	void findAll_Test() throws Exception {

		Mockito.when(statusController.findAll()).thenReturn(auditMap);

		//@formatter:off
		MvcResult result = mockMvc.perform(get(GET_ALL_URI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		String expectedContent = "{\"1\":{\"id\":\"1\",\"timestamp\":\"2020-10-22T20:45:00\",\"operationType\":\"INSERT\",\"table\":\"USER\",\"recordId\":\"1\",\"recordContent\":\"to string di user\"},\"2\":{\"id\":\"2\",\"timestamp\":\"2020-10-22T20:50:00\",\"operationType\":\"DELETE\",\"table\":\"MESSAGE\",\"recordId\":\"4\",\"recordContent\":\"to string di message\"}}";
		Assertions.assertEquals(expectedContent, content);

	}

	@DisplayName("FindByID Test: retrieve Message by ID")
	@Test
	void findBy_Test() throws Exception {

		Mockito.when(statusController.findById(id_1)).thenReturn(a1);

		String completeURI = BASE_URI + id_1;

		//@formatter:off
		MvcResult result = mockMvc.perform(get(completeURI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		String expectedContent = "{\"id\":\"1\",\"timestamp\":\"2020-10-22T20:45:00\",\"operationType\":\"INSERT\",\"table\":\"USER\",\"recordId\":\"1\",\"recordContent\":\"to string di user\"}";
		Assertions.assertEquals(expectedContent, content);

	}
}
