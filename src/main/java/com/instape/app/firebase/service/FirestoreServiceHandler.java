package com.instape.app.firebase.service;

import java.util.Map;
import com.instape.app.cloudstore.dto.NoSQLDocument;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Dec-2023
 * @ModifyDate - 04-Dec-2023
 * @Desc -
 */
public interface FirestoreServiceHandler {

	
	public Map<String, Object> createFSDocument(Map<String, Object> docData, String... pointer);
	
	public Map<String, Object> getFSDocument(String... pointer);
	
	public NoSQLDocument getDocRef(String... pointer);
	
	public NoSQLDocument createAndUpdateFSDocument(Map<String, Object> docData, String... pointer);

}
