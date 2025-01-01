package com.instape.app.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 23-Nov-2023
 * @ModifyDate - 23-Nov-2023
 * @Desc -
 */
public class StringUtil {

	static final Logger logger = LogManager.getLogger(StringUtil.class);

	public static String pojoToString(Object entity) {
		try {
			ObjectMapper obj = new ObjectMapper();
			obj.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			String entityAsString = obj.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
			return entityAsString;
		} catch (Exception ex) {
			logger.info("Exception in Parsing Object to json: {}", ex.getMessage());
			return "";
		}
	}

	public static String getEmployerCodeFromEmployerId(String employerId) {
		String[] array = employerId.split("-");
		if (array.length >= 2) {
			return array[1];
		}
		return array[0];
	}

	public static Object jsonStringToObject(Object jsonObject) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode object = mapper.readTree(jsonObject.toString());
		return object;
	}

	public static Object jsonStringToPojo(Object json, final Class<?> target) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		String jsonString = StringUtil.pojoToString(json);
		Object res = objectMapper.readValue(jsonString, target);

		return res;
	}

}
