package com.company.chat.controller;

import com.company.chat.dao.manager.AuditService;
import com.company.chat.dao.model.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/status")
public class StatusController extends AbstractController {

	@Autowired
	private AuditService auditService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	// Get all Audit record.
	@GetMapping("/audit/all")
	public Map<String, Audit> findAll() {
		log.debug("Getting all Audits");
		// TODO: eventually sort the results
		return auditService.findAll();
	}

	@GetMapping("/audit/{id}")
	public Audit findById(
			@PathVariable("id")
			final String id) {
		log.debug("Getting AuditId= " + id);
		return auditService.findById(id);
	}
}
