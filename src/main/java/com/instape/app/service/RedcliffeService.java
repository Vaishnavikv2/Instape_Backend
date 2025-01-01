package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.request.BookBloodTestRequestPayload;
import com.instape.app.request.BookingSummaryRequestPayload;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
public interface RedcliffeService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 09-Jul-2024
	 * @ModifyDate - 09-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> bookBlodTest(BookBloodTestRequestPayload payload);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 17-Jul-2024
	 * @ModifyDate - 17-Jul-2024
	 * @Desc -
	 */
	public Map<Object, Object> getBookingSummary(BookingSummaryRequestPayload payload, PageRequest pageRequest);

}