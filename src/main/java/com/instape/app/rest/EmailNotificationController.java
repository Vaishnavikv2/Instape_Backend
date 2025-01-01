package com.instape.app.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.cloudsql.model.EmailNotificationsDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.EmailNotificationsService;
import com.instape.app.utils.StatusCode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notification")
public class EmailNotificationController {
	private final Logger logger = LogManager.getLogger(EmailNotificationController.class);
	@Autowired
	private EmailNotificationsService emailNotificationService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('EMAIL_NOTIFICATION.MAIN.READ')")
	public ResponseEntity<PageableResponseDTO> getAll(@RequestParam(defaultValue = "0") final Integer pageNumber,
            @RequestParam(defaultValue = "10") final Integer size,@RequestParam(defaultValue = "") final String text) {
		logger.info("Get all emailNotification request received");
		PageableResponseDTO emailNotificationList = emailNotificationService.getAll(text,PageRequest.of(pageNumber, size));
		logger.info("toatl emailNotification fetched:{}", emailNotificationList.getPagination().getTotalcount());
		return new ResponseEntity<PageableResponseDTO>(emailNotificationList, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('EMAIL_NOTIFICATION.MAIN.READ')")
	public ResponseEntity<EmailNotificationsDTO> getById(@PathVariable Long id, HttpServletRequest request) {
		logger.info("Get emailNotification request received, emailNotificationId:{}", id);
		EmailNotificationsDTO emailNotificationDto = emailNotificationService.getById(id);
		return new ResponseEntity<EmailNotificationsDTO>(emailNotificationDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('EMAIL_NOTIFICATION.MAIN.CREATE')")
	public ResponseEntity<EmailNotificationsDTO> create(@RequestBody EmailNotificationsDTO emailNotification, Authentication auth) {
		logger.info("Create emailNotification request received, emailNotification Type:{}", emailNotification.getType());
		validatePayload(emailNotification);
		if (emailNotificationService.isExistWithSameType(emailNotification.getType())) {
			throw new InstapeException("Email Notification with same type already exist", HttpStatus.BAD_REQUEST.value());
		}
		Long loggedInUserId = Long.parseLong(auth.getName());
		EmailNotificationsDTO emailNotificationDto = emailNotificationService.create(loggedInUserId, emailNotification);
		return new ResponseEntity<EmailNotificationsDTO>(emailNotificationDto, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('EMAIL_NOTIFICATION.MAIN.UPDATE')")
	public ResponseEntity<EmailNotificationsDTO> update(@RequestBody EmailNotificationsDTO emailNotification, Authentication auth) {
		logger.info("Update emailNotification request received, emailNotification name:{}", emailNotification.getType());
		Long loggedInUserId = Long.parseLong(auth.getName());
		EmailNotificationsDTO emailNotificationDto = emailNotificationService.update(loggedInUserId, emailNotification);
		return new ResponseEntity<EmailNotificationsDTO>(emailNotificationDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('EMAIL_NOTIFICATION.MAIN.DELETE')")
	public ResponseEntity<ResponseDTO> delete(@PathVariable Long id, Authentication auth) {
		logger.info("Delete emailNotification request received, emailNotificationId:{}", id);
		Long loggedInUserId = Long.parseLong(auth.getName());
		emailNotificationService.delete(loggedInUserId, id);
		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Email Notification has been deleted",StatusCode.OK,null), HttpStatus.OK);
	}

	private void validatePayload(EmailNotificationsDTO emailNotification) {
		if (!StringUtils.hasText(emailNotification.getType())) {
			throw new InstapeException("Email Notification type is mandatory", HttpStatus.BAD_REQUEST.value());
		}
	}
}
