package com.instape.app.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@GetMapping("/resetData")
	public ResponseEntity<?> collectDataToReset() {
		logger.info("Going to collect reset data");
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
