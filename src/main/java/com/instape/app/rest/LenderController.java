package com.instape.app.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instape.app.cloudsql.model.LenderDTO;
import com.instape.app.exception.handler.InstapeException;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.response.ResponseDTO;
import com.instape.app.service.LenderService;
import com.instape.app.utils.StatusCode;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/lender")
public class LenderController {
//	private final Logger logger = LogManager.getLogger(LenderController.class);
//	@Autowired
//	private LenderService lenderService;
//
//	@GetMapping
//	@PreAuthorize("hasAnyAuthority('LENDER.MAIN.READ')")
//	public ResponseEntity<PageableResponseDTO> getAll(@RequestParam(defaultValue = "0") final Integer pageNumber,
//			@RequestParam(defaultValue = "10") final Integer size) {
//		logger.info("Get all lender request received");
//		PageableResponseDTO lenderList = lenderService.getAll(PageRequest.of(pageNumber, size));
//		logger.info("toatl lender fetched:{}", lenderList.getPagination().getSize());
//		return new ResponseEntity<PageableResponseDTO>(lenderList, HttpStatus.OK);
//	}
//
//	@GetMapping("/{id}")
//	@PreAuthorize("hasAnyAuthority('LENDER.MAIN.READ')")
//	public ResponseEntity<LenderDTO> getById(@PathVariable Long id, HttpServletRequest request) {
//		logger.info("Get lender request received, lenderId:{}", id);
//		LenderDTO lenderDto = lenderService.getById(id);
//		return new ResponseEntity<LenderDTO>(lenderDto, HttpStatus.OK);
//	}
//
//	@PostMapping
//	@PreAuthorize("hasAnyAuthority('LENDER.MAIN.CREATE')")
//	public ResponseEntity<LenderDTO> create(@RequestBody LenderDTO lender, Authentication auth) {
//		logger.info("Create lender request received, lender name:{}", lender.getName());
//		validatePayload(lender);
//		if (lenderService.isExistWithSameCode(lender.getCode())) {
//			throw new InstapeException("Lender with same code already exist", HttpStatus.BAD_REQUEST.value());
//		}
//		Long loggedInUserId = Long.parseLong(auth.getName());
//		LenderDTO lenderDto = lenderService.create(loggedInUserId, lender);
//		return new ResponseEntity<LenderDTO>(lenderDto, HttpStatus.OK);
//	}
//
//	@PutMapping
//	@PreAuthorize("hasAnyAuthority('LENDER.MAIN.UPDATE')")
//	public ResponseEntity<LenderDTO> update(@RequestBody LenderDTO lender, Authentication auth) {
//		logger.info("Update lender request received, lender name:{}", lender.getName());
//		Long loggedInUserId = Long.parseLong(auth.getName());
//		LenderDTO lenderDto = lenderService.update(loggedInUserId, lender);
//		return new ResponseEntity<LenderDTO>(lenderDto, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/{id}")
//	@PreAuthorize("hasAnyAuthority('LENDER.MAIN.DELETE')")
//	public ResponseEntity<ResponseDTO> delete(@PathVariable Long id, Authentication auth) {
//		logger.info("Delete lender request received, lenderId:{}", id);
//		Long loggedInUserId = Long.parseLong(auth.getName());
//		lenderService.delete(loggedInUserId, id);
//		return new ResponseEntity<ResponseDTO>(new ResponseDTO("Lender has been deleted", StatusCode.OK, null),
//				HttpStatus.OK);
//	}
//
//	private void validatePayload(LenderDTO lender) {
//		if (!StringUtils.hasText(lender.getCode())) {
//			throw new InstapeException("Lender code is mandatory", HttpStatus.BAD_REQUEST.value());
//		}
//	}
}
