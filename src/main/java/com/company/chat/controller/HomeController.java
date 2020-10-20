package com.company.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/")
@RestController
public class HomeController extends AbstractController {

	@GetMapping
	public String getWelcome() {
		log.debug("Requesting Welcome");
		return "WELCOME";
	}
}