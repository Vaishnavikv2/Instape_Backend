package com.instape.app.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.instape.app.cloudsql.model.UserSecret;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 06-May-2024
 * @ModifyDate - 06-May-2024
 * @Desc -
 */
public class EncryptionHelper {
	public static Map<String, String> getUserSKUuids(List<UserSecret> userSecrets) {
		Map<String, String> sks = null;
		sks = new HashMap<>();
		if (userSecrets != null) {
			for (UserSecret us : userSecrets) {
				sks.put(us.getUuid(), us.getKmsEncryptedSecretKey());
			}
		}
		return sks;
	}
}
