package com.instape.app.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.instape.app.request.EmailRequestPayload;
import com.instape.app.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {
	
	static final Logger logger = LogManager.getFormatterLogger(EmailServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${send-email-url}")
	private String sendEmailUrl;
	
	@Override
	public void sendLoginOtpEmail(String name,String email, String otp) {
		    try {
			Instant start = Instant.now();
			logger.info("Start of sending Otp on email Api API ");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			EmailRequestPayload payload = new EmailRequestPayload(name,email, otp);
			HttpEntity<EmailRequestPayload> httpEntity = new HttpEntity<>(payload, headers);
			String response = restTemplate.exchange(sendEmailUrl, HttpMethod.POST, httpEntity, String.class).getBody();
			Instant end = Instant.now();
			Duration timeElapsed = Duration.between(start, end);
			logger.info(String.format("Time taken on send user Otp :-  %s", timeElapsed.toSeconds()));
			logger.info("End of send user Otp Rest APIresponse: %s", response);
		    }catch(Exception ex) {
		    	logger.error("error in sending otp email,{}",ex.getMessage());
		    }

	}

}
