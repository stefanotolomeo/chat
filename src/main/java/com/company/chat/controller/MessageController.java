package com.company.chat.controller;

import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

// Just for manual management of Redis Cache
@RestController
@RequestMapping(value = "/api/redis/message")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@PostMapping
	public String save(
			@RequestBody
			final Message message) {

		String outcome;
		try {
			// (1) Set Message Timestamp
			LocalDateTime timestamp = LocalDateTime.now();
			message.setTimestamp(timestamp);

			// (2) Save the Message
			log.debug("Saving Message={}", message);
			String savedId = messageService.save(message);

			// (3) Set the Outcome
			outcome = "Successfully added Message with ID = " + savedId;
		} catch (Exception e) {
			String msg = String.format("Exception while saving Message=%s", message);
			log.error(msg, e);
			outcome = "Unexpected Internal Error";
		}
		return outcome;
	}

	@GetMapping("/all")
	public Map<String, Message> findAll() {
		log.debug("Getting all Messages");
		// TODO: eventually sort the results
		return messageService.findAll();
	}

	@GetMapping("/{id}")
	public Message findById(
			@PathVariable("id")
			final String id) {
		log.debug("Getting MessageId= " + id);
		return messageService.findById(id);
	}

	@DeleteMapping("/{id}")
	public String delete(
			@PathVariable("id")
			final String id) {
		log.info("Deleting MessageId= " + id);

		String outcome;
		try {
			// (1) Delete the Message
			log.debug("Deleting MessageId={}", id);
			Message deletedMessage = messageService.delete(id);

			// (2) Set the Outcome
			outcome = "Successfully deleted Message=" + deletedMessage;
		} catch (Exception e) {
			String msg = String.format("Exception while deleting MessageId=%s", id);
			log.error(msg, e);
			outcome = "Unexpected Internal Error";
		}
		return outcome;
	}
}