package com.company.chat.controller;

import com.company.chat.dao.model.Message;
import com.company.chat.testconfig.BaseWebTest;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class MessageControllerTest extends BaseWebTest {

	@Inject
	private MessageController messageController;

	@Inject
	private MockMvc mockMvc;

	private final static String BASE_URI = "/api/redis/message/";
	private final static String GET_ALL_URI = BASE_URI + "all";

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

	private final Map<String, Message> messageMap = new HashMap<String, Message>() {
		private static final long serialVersionUID = -1718177775945603652L;

		{
			put(id_1, m1);
			put(id_2, m2);
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

		Mockito.when(messageController.findAll()).thenReturn(messageMap);

		//@formatter:off
		MvcResult result = mockMvc.perform(get(GET_ALL_URI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		String expectedContent = "{\"1\":{\"id\":\"1\",\"timestamp\":\"2020-10-22T20:45:00\",\"content\":\"Ehi, you!\",\"sender\":\"1\",\"topic\":\"chat_topic\"},\"2\":{\"id\":\"2\",\"timestamp\":\"2020-10-22T20:50:00\",\"content\":\"Hi, man!\",\"sender\":\"2\",\"topic\":\"chat_topic\"}}";
		Assertions.assertEquals(expectedContent, content);

	}

	@DisplayName("FindByID Test: retrieve Message by ID")
	@Test
	void findBy_Test() throws Exception {

		Mockito.when(messageController.findById(id_1)).thenReturn(m1);

		String completeURI = BASE_URI + id_1;

		//@formatter:off
		MvcResult result = mockMvc.perform(get(completeURI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		String expectedContent = "{\"id\":\"1\",\"timestamp\":\"2020-10-22T20:45:00\",\"content\":\"Ehi, you!\",\"sender\":\"1\",\"topic\":\"chat_topic\"}";
		Assertions.assertEquals(expectedContent, content);

	}

	@DisplayName("Delete Test: delete a Message by ID")
	@Test
	void delete_Test() throws Exception {

		String output = "Successfully deleted Message with ID = " + id_1;
		Mockito.when(messageController.delete(id_1)).thenReturn(output);

		String completeURI = BASE_URI + id_1;

		//@formatter:off
		MvcResult result = mockMvc.perform(delete(completeURI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		Assertions.assertEquals(output, content);

	}

	@DisplayName("Save Test: save a new Message")
	@Test
	@Disabled
	void save_Test() throws Exception {

		String output = "Successfully added Message with ID = " + id_1;
		Mockito.when(messageController.save(m1)).thenReturn(output);

		String inputJSON = asJsonString(m1);

		//@formatter:off
		MvcResult result = mockMvc.perform(post(BASE_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
            	.content(inputJSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		Assertions.assertEquals(output, content);
	}
}
