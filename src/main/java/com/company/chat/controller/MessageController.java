package com.company.chat.controller;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// In this class, we have left the caching approach for tutorial simplicity.
// If users which they can enable caching in this application.
@RestController
@RequestMapping(value = "/api/redis/message")
public class MessageController extends AbstractController {

	@Autowired private MessageService service;

	// Save a new message.
	// Url - http://localhost:10091/api/redis/message
	@PostMapping
	public String save(
			@RequestBody
			final Message message) {
		log.info("Saving the new message to the redis.");
		service.save(message);
		return "Successfully added. Message with id= " + message.getId();
	}

	// Get all messages.
	// Url - http://localhost:10091/api/redis/message/getall
	@GetMapping("/getall")
	public Map<String, Message> findAll() {
		log.info("Fetching all messages from the redis.");
		final Map<String, Message> messageMap = service.findAll();
		// Todo - Sort the map (optional).
		return messageMap;
	}

	// Get message by id.
	// Url - http://localhost:10091/api/redis/message/get/<message_id>
	@GetMapping("/get/{id}")
	public Message findById(
			@PathVariable("id")
			final String id) {
		log.info("Fetching message with id= " + id);
		return service.findById(id);
	}

	// Delete message by id.
	// Url - http://localhost:10091/api/redis/message/delete/<message_id>
	@DeleteMapping("/delete/{id}")
	public Map<String, Message> delete(
			@PathVariable("id")
			final String id) {
		log.info("Deleting message with id= " + id);
		// Deleting the message.
		service.delete(id);
		// Returning the all messages (post the deleted one).
		return findAll();
	}
}