package com.company.chat.controller;

import com.company.chat.dao.model.User;
import com.company.chat.testconfig.BaseWebTest;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class UserControllerTest extends BaseWebTest {

	@Inject
	private UserController userController;

	@Inject
	private MockMvc mockMvc;

	private final static String BASE_URI = "/api/redis/user/";
	private final static String GET_ALL_URI = BASE_URI + "all";

	private final String id_1 = "1";
	private final String id_2 = "2";

	private final String username_1 = "username_1";
	private final String username_2 = "username_2";

	private final User u1 = new User(id_1, username_1);
	private final User u2 = new User(id_2, username_2);

	private final Map<String, User> userMap = new HashMap<String, User>() {
		private static final long serialVersionUID = -1718177775945603652L;

		{
			put(id_1, u1);
			put(id_2, u2);
		}
	};

	@BeforeEach
	void setup() {

	}

	@AfterEach
	void tearDown() {

	}

	@DisplayName("FindAll Test: retrieve all Users into cache")
	@Test
	void findAll_Test() throws Exception {

		Mockito.when(userController.findAll()).thenReturn(userMap);

		//@formatter:off
		MvcResult result = mockMvc.perform(get(GET_ALL_URI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		String expectedContent = "{\"1\":{\"id\":\"1\",\"username\":\"username_1\"},\"2\":{\"id\":\"2\",\"username\":\"username_2\"}}";
		Assertions.assertEquals(expectedContent, content);

	}

	@DisplayName("FindByID Test: retrieve User by ID")
	@Test
	void findBy_Test() throws Exception {

		Mockito.when(userController.findById(id_1)).thenReturn(u1);

		String completeURI = BASE_URI + id_1;

		//@formatter:off
		MvcResult result = mockMvc.perform(get(completeURI))
				// .andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		String expectedContent = "{\"id\":\"1\",\"username\":\"username_1\"}";
		Assertions.assertEquals(expectedContent, content);

	}

	@DisplayName("Delete Test: delete a User by ID")
	@Test
	void delete_Test() throws Exception {

		String output = "Successfully deleted User=" + id_1;
		Mockito.when(userController.delete(id_1)).thenReturn(output);

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

	@DisplayName("Update Test: update a User")
	@Test
	@Disabled
	void update_Test() throws Exception {

		String output = "Successfully updated User with ID = " + id_1;
		Mockito.when(userController.update(u1)).thenReturn(output);

		String inputJSON = asJsonString(u1);

		//@formatter:off
		MvcResult result = mockMvc.perform(put(BASE_URI)
				.content(inputJSON)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		//@formatter:on

		String content = result.getResponse().getContentAsString();
		Assertions.assertEquals(output, content);
	}

	@DisplayName("Save Test: save a new User")
	@Test
	@Disabled
	void save_Test() throws Exception {

		String output = "Successfully added User with ID = " + id_1;
		Mockito.when(userController.save(u1)).thenReturn(output);

		String inputJSON = asJsonString(u1);

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