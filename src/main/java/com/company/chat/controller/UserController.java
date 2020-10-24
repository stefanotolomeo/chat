package com.company.chat.controller;

import com.company.chat.dao.manager.UserService;
import com.company.chat.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/redis/user")
public class UserController extends AbstractController {

	@Autowired
	private UserService userService;

	@PostMapping
	public String save(
			@RequestBody
			final User user) {
		String outcome;
		try {
			// (1) Save the User
			log.debug("Saving User={}", user);
			String savedId = userService.save(user);

			// (3) Set the Outcome
			outcome = "Successfully added User with ID = " + savedId;
		} catch (Exception e) {
			String msg = String.format("Exception while saving User=%s", user);
			log.error(msg, e);
			outcome = "Unexpected Internal Error";
		}
		return outcome;

	}

	@GetMapping("/all")
	public Map<String, User> findAll() {
		log.debug("Getting all Users");
		return userService.findAll().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@GetMapping("/{id}")
	public User findById(
			@PathVariable("id")
			final String id) {
		log.debug("Getting UserId= " + id);
		return userService.findById(id);
	}

	@DeleteMapping("/{id}")
	public String delete(
			@PathVariable("id")
			final String id) {
		log.info("Deleting UserId= " + id);

		String outcome;
		try {
			// (1) Delete the User
			log.debug("Deleting MessageId={}", id);
			User deletedUser = userService.delete(id);

			// (2) Set the Outcome
			outcome = "Successfully deleted User=" + deletedUser;
		} catch (Exception e) {
			String msg = String.format("Exception while deleting UserId=%s", id);
			log.error(msg, e);
			outcome = "Unexpected Internal Error";
		}
		return outcome;
	}

}
