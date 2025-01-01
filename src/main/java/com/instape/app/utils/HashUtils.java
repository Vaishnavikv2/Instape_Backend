package com.instape.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Dec-2023
 * @ModifyDate - 12-Dec-2023
 * @Desc -
 */
@Component
public class HashUtils {

	public String getSHA3(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA3-512");
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert the byte array to a hexadecimal string
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
