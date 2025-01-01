package com.instape.app.firebase.service.impl;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.instape.app.cloudstore.dto.NoSQLDocument;
import com.instape.app.cloudstore.service.CloudNoSQLService;
import com.instape.app.firebase.service.FirestoreServiceHandler;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Dec-2023
 * @ModifyDate - 04-Dec-2023
 * @Desc -
 */
@Service
public class FirestoreServiceHandlerImpl implements FirestoreServiceHandler {

	static final Logger logger = LogManager.getFormatterLogger(FirestoreServiceHandlerImpl.class);

	@Autowired
	private CloudNoSQLService cloudNoSQLService;

	public NoSQLDocument getDocRef(String... pointer) {
		return cloudNoSQLService.getNoSQLDocument(pointer);
	}

	public Map<String, Object> getFSDocument(String... pointer) {
		NoSQLDocument docRef = cloudNoSQLService.getNoSQLDocument(pointer);

		if (!docRef.isDocumentExist())
			return null;

		return docRef.getData();
	}

	@Override
	public Map<String, Object> createFSDocument(Map<String, Object> docData, String... pointer) {
		NoSQLDocument docRef = cloudNoSQLService.getNoSQLDocument(pointer);
		if (!docRef.isDocumentExist())
			docRef.set(docData/* ,SetOptions.merge() */);

		return docRef.getData();
	}

	@Override
	public NoSQLDocument createAndUpdateFSDocument(Map<String, Object> docData, String... pointer) {
		NoSQLDocument docRef = cloudNoSQLService.getNoSQLDocument(pointer);
		if (!docRef.isDocumentExist()) {
			docRef.set(docData);
		} else {
			docRef.update(docData);
		}
		return docRef;
	}
}
