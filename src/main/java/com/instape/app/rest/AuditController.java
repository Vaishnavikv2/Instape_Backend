package com.instape.app.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.AuditCompareRequestDTO;
import com.instape.app.request.AuditTimestampDTO;
import com.instape.app.response.AuditCompareResponse;
import com.instape.app.service.AuditService;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
	private static final Logger logger = LogManager.getLogger(AuditController.class);

	@Autowired
	private AuditService auditService;

	@GetMapping("/{resourceName}/{resourceId}")
	@PreAuthorize("hasAnyAuthority('AUDIT.MAIN.READ')")
	public ResponseEntity<List<AuditTimestampDTO>> getResourceAuditTimestamp(
			@PathVariable("resourceName") String resourceName, @PathVariable("resourceId") Long resourceId) {
		logger.info("Request received to get audit log timestamp, Resource:{},Id:{}", resourceName, resourceId);
		List<AuditTimestampDTO> timestampList = auditService.getAuditTimestamp(resourceName, resourceId);
		return new ResponseEntity<List<AuditTimestampDTO>>(timestampList, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('AUDIT.MAIN.READ')")
	public ResponseEntity<AuditCompareResponse> getResourceAuditCompare(@RequestBody AuditCompareRequestDTO payload) {
		logger.info("Request received to get audit log timestamp, Resource:{},Id:{}", payload.getResourceName(),
				payload.getResourceId());
		AuditCompareResponse response = auditService.getAuditCompare(payload);
		return new ResponseEntity<AuditCompareResponse>(response, HttpStatus.OK);
	}
}
