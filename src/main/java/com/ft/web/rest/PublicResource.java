package com.ft.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api/public")
public class PublicResource {
	
	private final Logger log = LoggerFactory.getLogger(PublicResource.class);

    @GetMapping("/ussd")
    @Timed
    public ResponseEntity<String> ussdMenuEntry() {
    	log.debug("Got a request come to USSD Menu");
    	return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).body("Welcome to USSD Menu Entry:\n"
    			+ "1. HTTP Status Reference\n"
    			+ "2. SMPP Status Reference\n"
    			+ "3. USSDGW Operation Code Values\n"
    			+ "4. Prompt Guide lines\n"
    			);
    }
    
    @PostMapping("/ussd")
    @Timed
    public ResponseEntity<String> ussdMenuProcessor(@RequestParam("menu") String menu, @RequestParam(name="input", required=false) String input) {
    	log.debug("Got a POST request to USSD Menu Processor, menu=" + menu, "input=" + input);
    	if (input == null) return ussdMenuEntry();
    	else if (input.equalsIgnoreCase("1")) {
    		return ResponseEntity.status(HttpStatus.ACCEPTED)
    				.body("This one mean we successfully handle the request, and the converter should be able to trigger one USSD NE to the gateway");
    	} else if (input.equalsIgnoreCase("2")) {
    		return ResponseEntity.status(HttpStatus.OK)
    				.body("This one mean we should terminate the session successfully.");
    	}
    	return ResponseEntity.badRequest()
    			.body("Sorry, invalid option. Please try again");
    }
}
