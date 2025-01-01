package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.instape.app.cloudsql.model.EmailNotificationsDTO;
import com.instape.app.cloudsql.model.Notification;
import com.instape.app.cloudsql.repository.EmailNotificationRepository;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.PageResponse;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.service.EmailNotificationsService;
import com.instape.app.utils.StatusCode;

@Service
public class EmailNotificationsServiceImpl implements EmailNotificationsService {

	private final Logger logger = LogManager.getLogger(EmailNotificationsServiceImpl.class);

	@Autowired
	private EmailNotificationRepository emailNotificationRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public EmailNotificationsDTO getById(Long id) {
		logger.info("getting Email Notification by id");
		Notification notification = emailNotificationRepository.getNotificationById(id);
		if (notification == null) {
			throw new InstapeException("Email Notification does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		return mapper.map(notification, EmailNotificationsDTO.class);
	}

	@Override
	public EmailNotificationsDTO create(Long loggedInUserId, EmailNotificationsDTO emailNotificationDto) {
		logger.info("Creating Email Notification");
		if (emailNotificationRepository.existsByType(emailNotificationDto.getType())) {
			logger.error("Email Notification already exists with same type, type:{}", emailNotificationDto.getType());
			throw new InstapeException("Email Notification already exists with same type", StatusCode.ALREADY_EXIST);
		}
		Notification notification = mapper.map(emailNotificationDto, Notification.class);
		notification.setCreatedBy(loggedInUserId.toString());
		notification.setUpdatedBy(loggedInUserId.toString());
		notification.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		notification.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		notification.setDeleted(false);
		notification = emailNotificationRepository.save(notification);
		return mapper.map(notification, EmailNotificationsDTO.class);
	}

	@Override
	public boolean isExistWithSameType(String type) {
		logger.info("checking email notification exisitance");
		return emailNotificationRepository.existsByType(type);
	}

	@Override
	public EmailNotificationsDTO update(Long loggedInUserId, EmailNotificationsDTO emailNotificationDto) {
		logger.info("updating email notification");
		Notification notification = emailNotificationRepository.getNotificationById(emailNotificationDto.getId());
		if (notification == null) {
			throw new InstapeException("Email notification does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		if (emailNotificationRepository.existsByTypeForUpdate(emailNotificationDto.getType(),
				emailNotificationDto.getId())) {
			throw new InstapeException("Email notification with same type already exist",
					HttpStatus.BAD_REQUEST.value());
		}
		notification.setType(emailNotificationDto.getType());
		notification.setContractCode(emailNotificationDto.getContractCode());
		notification.setDescription(emailNotificationDto.getDescription());
		notification.setSendTo(emailNotificationDto.getSendTo());
		notification.setSubject(emailNotificationDto.getSubject());
		notification.setBody(emailNotificationDto.getBody());
		notification.setUpdatedBy(loggedInUserId.toString());
		notification = emailNotificationRepository.save(notification);
		return mapper.map(notification, EmailNotificationsDTO.class);
	}

	@Override
	public void delete(Long loggedInUserId, Long id) {
		logger.info("deleting Email Notification");
		Notification notification = emailNotificationRepository.getNotificationById(id);
		if (notification == null) {
			throw new InstapeException("Email notification does not exist", StatusCode.ENTITY_NOT_FOUND);
		}
		notification.setDeleted(true);
		notification.setUpdatedBy(loggedInUserId.toString());
		emailNotificationRepository.save(notification);
	}

	@Override
	public PageableResponseDTO getAll(String searchtext, PageRequest pageRequest) {
		logger.info("getting Email Notification  list");
		Page<Notification> page = null;
		if (StringUtils.hasText(searchtext)) {
			page = emailNotificationRepository.getAllNotificationWithSearch(searchtext, pageRequest);
		} else {
			page = emailNotificationRepository.getAllNotification(pageRequest);
		}
		List<EmailNotificationsDTO> notificationDtoList = page.getContent().stream()
				.map(resource -> mapper.map(resource, EmailNotificationsDTO.class)).collect(Collectors.toList());
		PageResponse pagination = new PageResponse(page.getTotalElements(), page.getPageable().getPageNumber(),
				page.getNumberOfElements(), page.getSize());
		return new PageableResponseDTO(notificationDtoList, pagination);
	}

}
