package com.instape.app.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.instape.app.exception.handler.InstapeException;
import com.instape.app.request.InternalImpsInqPayload;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.ImpsEnquiryService;
import com.instape.app.utils.StatusCode;

@Service
public class ImpsEnquiryServiceImpl implements ImpsEnquiryService {

	private static final Logger logger = LogManager.getLogger(ImpsEnquiryServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${impsEnquiryUrl}")
	private String impsEnquiryUrl;

	@Override
	public ResponseDTO getImpsEnquiry(InternalImpsInqPayload payload) {
		logger.info("Strat of loan enquiry Api ");
		try {
			logger.info("inqByCifUrl:{}", impsEnquiryUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			HttpEntity<InternalImpsInqPayload> httpEntity = new HttpEntity<InternalImpsInqPayload>(payload, headers);
			ResponseDTO response = restTemplate.exchange(impsEnquiryUrl, HttpMethod.POST, httpEntity, ResponseDTO.class)
					.getBody();
			logger.info("End of imps enquiry, returning Response");
			return response;
		} catch (Exception e) {
			throw new InstapeException("Something went wrong," + e.getMessage(), StatusCode.INTERNAL_ERROR);
		}

	}

}