package com.company.chat.controller;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.manager.UserService;
import com.company.chat.dao.model.Message;
import com.company.chat.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/redis/user")
public class UserController extends AbstractController {

	@Autowired
	private UserService service;

	@PostMapping
	public String save(@RequestBody final User user) {
		log.info("Saving the new user to the redis.");
		service.save(user);
		return "Successfully added. User with id= " + user.getId();
	}

	@GetMapping("/getall")
	public Map<String, User> findAll() {
		log.info("Fetching all users from the redis.");
		final Map<String, User> userMap = service.findAll();
		// Todo - Sort the map (optional).
		return userMap;
	}

	@GetMapping("/get/{id}")
	public User findById(@PathVariable("id") final String id) {
		log.info("Fetching user with id= " + id);
		return service.findById(id);
	}

	// Delete message by id.
	// Url - http://localhost:10091/api/redis/message/delete/<message_id>
	@DeleteMapping("/delete/{id}")
	public Map<String, User> delete(@PathVariable("id") final String id) {
		log.info("Deleting user with id= " + id);
		// Deleting the user.
		service.delete(id);
		// Returning the all users(post the deleted one).
		return findAll();
	}

}
