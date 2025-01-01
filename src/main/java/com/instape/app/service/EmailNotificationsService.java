package com.instape.app.service;

import org.springframework.data.domain.PageRequest;

import com.instape.app.cloudsql.model.EmailNotificationsDTO;
import com.instape.app.response.PageableResponseDTO;

public interface EmailNotificationsService {

	EmailNotificationsDTO getById(Long id);

	EmailNotificationsDTO create(Long loggedInUserId, EmailNotificationsDTO emailNotification);

	boolean isExistWithSameType(String type);

	EmailNotificationsDTO update(Long loggedInUserId, EmailNotificationsDTO emailNotification);

	void delete(Long loggedInUserId, Long id);

	PageableResponseDTO getAll(String text, PageRequest pageRequest);

}
