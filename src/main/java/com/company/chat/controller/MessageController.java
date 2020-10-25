package com.company.chat.controller;

import com.company.chat.dao.exceptions.ItemNotFoundException;
import com.company.chat.dao.manager.MessageService;
import com.company.chat.dao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

// Just used for manual HTTP request
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
			// (1) Save the Message
			log.debug("Received Message={}", message);
			String savedId = messageService.save(message);

			// (2) Set the Outcome
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
		return messageService.findAll().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
		} catch (ItemNotFoundException e) {
			String msg = String.format("Cannot delete MessageId=%s", id);
			log.error(msg, e);
			outcome = e.getMessage();
		} catch (Exception e) {
			String msg = String.format("Exception while deleting MessageId=%s", id);
			log.error(msg, e);
			outcome = "Unexpected Internal Error";
		}
		return outcome;
	}
}