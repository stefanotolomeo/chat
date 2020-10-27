package com.company.chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/version")
public class VersionController extends AbstractController {

	// TODO: better understand the requirements

	@Value("${app.version}")
	private String version;

	// Get Version
	@GetMapping
	public String getVersion() {
		return version;
	}
}
