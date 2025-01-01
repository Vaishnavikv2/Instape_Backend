package com.instape.app.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import com.instape.app.request.FileUploadRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 11-Jan-2024
 * @ModifyDate - 11-Jan-2024
 * @Desc -
 */
public interface ClientMasterService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 15-Jan-2024
	 * @ModifyDate - 15-Jan-2024
	 * @Desc -
	 */
	public Map<Object, Object> uploadFile(MultipartFile file, String custId, String contractCode, String userId,
			String fileType);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 17-Jan-2024
	 * @ModifyDate - 17-Jan-2024
	 * @Desc -
	 */
	public Map<Object, Object> getFileUploadList(FileUploadRequestPayload payload, PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 18-Jan-2024
	 * @ModifyDate - 18-Jan-2024
	 * @Desc -
	 */
	public Map<Object, Object> downloadTemplate(String templateType);

}
