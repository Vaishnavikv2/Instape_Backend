package com.instape.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.instape.app.cloudstore.service.CloudStorageService;
import com.instape.app.cloudstore.service.EncryptionDecryptionService;
import com.instape.app.cloudstore.service.impl.EncryptionDecryptionServiceImpl;
import com.instape.app.cloudstore.service.impl.GoogleCloudStorageService;

@Component
public class InitConfiguration {

	@Autowired
	Resource resourceFile;

	@Value("${BUCKET_NAME}")
	private String bucketName;
	
	@Value("${LOCATION_ID}")
	private String locationId;

	@Value("${KEY_RING_ID}")
	private String keyRingId;
	
	@Value("${PROJECT_ID}")
	private String projectId;

	@Bean
	public CloudStorageService getStorage() {
		return new GoogleCloudStorageService(resourceFile, bucketName);
	}
	
	@Bean
	public EncryptionDecryptionService getencryptionDecryptionService() {
		return new EncryptionDecryptionServiceImpl(projectId, locationId, keyRingId);
	}
}