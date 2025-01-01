package com.instape.app.service;

import com.instape.app.request.InternalImpsInqPayload;
import com.instape.app.response.ResponseDTO;

public interface ImpsEnquiryService {
	ResponseDTO getImpsEnquiry(InternalImpsInqPayload payload);
}