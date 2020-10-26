package com.company.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class HomeController extends AbstractController {

	@GetMapping(value = { "/" })
	public String getHomepage() {
		log.info("Into GetHomepage");
		return "index";
	}
}