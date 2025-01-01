package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractDTO;
import com.instape.app.cloudsql.model.ContractOfferDTO;
import com.instape.app.cloudsql.model.ContractOffers;
import com.instape.app.cloudsql.model.ContractOffersDTO;
import com.instape.app.cloudsql.model.Offers;
import com.instape.app.cloudsql.model.OffersDTO;
import com.instape.app.cloudsql.repository.ContractOffersRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.OffersRepository;
import com.instape.app.request.AssignOfferRequestPayload;
import com.instape.app.request.ContractOffersRequestPayload;
import com.instape.app.request.CreateOfferRequestPayload;
import com.instape.app.request.OffersRequestPayload;
import com.instape.app.request.UpdateContractOfferPayload;
import com.instape.app.request.UpdateOfferRequestPayload;
import com.instape.app.service.OfferService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StringUtil;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Jul-2024
 * @ModifyDate - 04-Jul-2024
 * @Desc -
 */
@Service
public class OfferServiceImpl implements OfferService {

	static final Logger logger = LogManager.getFormatterLogger(OfferServiceImpl.class);

	@Autowired
	private OffersRepository offersRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ContractOffersRepository contractOffersRepository;

	@Override
	public Map<Object, Object> createOffer(CreateOfferRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Create Offer Service");

			Offers offerCodeExist = offersRepository.findByOfferCode(payload.getOfferCode());

			if (offerCodeExist == null) {
				Offers offer = new Offers();
				offer.setOfferCode(payload.getOfferCode());
				offer.setOfferName(payload.getOfferName());
				offer.setOfferDesc(payload.getOfferDesc());
				offer.setType(payload.getOfferType());
				offer.setProductCode(payload.getProductCode());
				offer.setActive(true);
				offer.setStartDate(Timestamp.valueOf(payload.getStartDate()));
				offer.setEndDate(Timestamp.valueOf(payload.getEndDate()));
				offer.setCreatedBy(payload.getUserId());
				offer.setUpdatedBy(payload.getUserId());
				offer.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				offer.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				offersRepository.save(offer);

				logger.info("Creation of Offer in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Create Offer Success");
				logger.info(String.format("Time taken on Create Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Offer Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.OFFERCODEALREADYEXIST);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Create Offer failed");
				logger.info(String.format("Offer Code Already Exist :-"));
				logger.info(String.format("Time taken on Create Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Create Offer Service %s", CommonMessageLog.OFFERCODEALREADYEXIST));
				return responseMap;
			}

		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Create Offer Service.", e);
			logger.info(String.format("Time taken on Create Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Create Offer Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getOffers(OffersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Offers Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Page<Offers> offers = null;

			if (payload.getOfferCode() != null && !payload.getOfferCode().isEmpty()) {
				offers = offersRepository.findByOfferCode(payload.getOfferCode(), pageRequest);
			} else {
				offers = offersRepository.getAllOffers(pageRequest);
			}

			if (offers != null && !offers.isEmpty()) {
				logger.info("Offers Recods Exist");
				List<OffersDTO> offersDTOs = new ArrayList<OffersDTO>();
				for (Offers offer : offers) {
					OffersDTO offerDTO = new OffersDTO();
					offerDTO.setId(offer.getId().toString());
					offerDTO.setOfferCode(offer.getOfferCode());
					offerDTO.setOfferName(offer.getOfferName());
					offerDTO.setOfferDesc(offer.getOfferDesc());
					offerDTO.setOfferType(offer.getType());
					offerDTO.setProductCode(offer.getProductCode());
					offerDTO.setActive(offer.isActive());
					offerDTO.setStartDate(offer.getStartDate());
					offerDTO.setEndDate(offer.getEndDate());
					offersDTOs.add(offerDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("count", offers.getTotalElements());
				responseMap.put("data", offersDTOs);
				stopWatch.stop();
				logger.info("Get Offers Service Success");
				logger.info(String.format("Time taken on get Offers Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Offers Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("count", 0);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Offers Service failed");
				logger.info("Offers Not Found :- ");
				logger.info(String.format("Time taken on get Offers Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Offers Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Offers Service.", e);
			logger.info(String.format("Time taken on get Offers Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Offers Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateOffer(UpdateOfferRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Offer Service");

			Offers offerExist = offersRepository.findById(Long.parseLong(payload.getOfferId())).orElse(null);

			if (offerExist != null) {
				Offers offer = offerExist;
				offer.setOfferName(payload.getOfferName());
				offer.setOfferDesc(payload.getOfferDesc());
				offer.setProductCode(payload.getProductCode());
				offer.setStartDate(Timestamp.valueOf(payload.getStartDate()));
				offer.setEndDate(Timestamp.valueOf(payload.getEndDate()));
				offer.setUpdatedBy(payload.getUserId());
				offer.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				offersRepository.save(offer);

				logger.info("Updation of Offer in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Offer Success");
				logger.info(String.format("Time taken on Update Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Offer Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Offer failed");
				logger.info(String.format("Offer Not Exist :-"));
				logger.info(String.format("Time taken on Update Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Offer Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Offer Service.", e);
			logger.info(String.format("Time taken on Update Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Offer Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getUnassignedContracts(OffersRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Unassigned Contracts Service : " + payload.getOfferCode());
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Offers offers = offersRepository.findByOfferCode(payload.getOfferCode());

			if (offers != null) {
				logger.info("Offers Recods Exist");

				List<Contract> contracts = contractRepository.getAllContractDetailsByStatus(PortalConstant.ACTIVE);

				List<ContractOffers> contractOffers = offers.getContractOffers();

				Set<Long> existingContractIds = contractOffers.stream().map(o -> o.getContract().getId())
						.collect(Collectors.toSet());

				System.err.println(existingContractIds);

				List<ContractDTO> unassignedContractDTOs = new ArrayList<ContractDTO>();

				for (Contract contract : contracts) {
					if (!existingContractIds.contains(contract.getId())) {
						ContractDTO contractDTO = new ContractDTO();
						contractDTO.setContractId(contract.getId().toString());
						contractDTO.setContractCode(contract.getCode());
						contractDTO.setContractName(contract.getContractName());
						unassignedContractDTOs.add(contractDTO);
					}
				}
				logger.info("Total Contracts count : " + contracts.size());
				logger.info("Total Assigned Contracts count : " + existingContractIds.size());
				logger.info("Total Unassigned Contracts count : " + unassignedContractDTOs.size());
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", unassignedContractDTOs);
				stopWatch.stop();
				logger.info("Get Unassigned Contracts Service Success");
				logger.info(String.format("Time taken on get Unassigned Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Unassigned Contracts Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Unassigned Contracts Service failed");
				logger.info("Offers Not Found :- ");
				logger.info(String.format("Time taken on get Unassigned Contracts Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(
						String.format("End of get Unassigned Contracts Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Unassigned Contracts Service.", e);
			logger.info(String.format("Time taken on get Unassigned Contracts Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Unassigned Contracts Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> assignOffer(AssignOfferRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Assign Offer Service");

			Offers offer = offersRepository.findByOfferCode(payload.getOfferCode());

			if (offer != null) {

				List<Long> data = payload.getData();

				List<ContractOffers> contractsOffers = new ArrayList<ContractOffers>();

				for (Long contractId : data) {

					Contract contract = contractRepository.findById(contractId).orElse(null);
					if (contract != null) {
						ContractOffers contractOffers = new ContractOffers();
						contractOffers.setActive(true);
						contractOffers.setContract(contract);
						contractOffers.setCreatedBy(payload.getUserId());
						contractOffers.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						contractOffers.setStartDate(Timestamp.valueOf(payload.getStartDate()));
						contractOffers.setEndDate(Timestamp.valueOf(payload.getEndDate()));
						contractOffers.setOffers(offer);
						contractOffers.setUpdatedBy(payload.getUserId());
						contractOffers.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
						contractsOffers.add(contractOffers);
					}
				}

				contractOffersRepository.saveAll(contractsOffers);

				logger.info("Assign of Offer in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Assign Offer Success");
				logger.info(String.format("Time taken on Assign Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Assign Offer Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Assign Offer failed");
				logger.info(String.format("Offer Not Found :-"));
				logger.info(String.format("Time taken on Assign Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Assign Offer Service %s", CommonMessageLog.OFFERCODEALREADYEXIST));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Assign Offer Service.", e);
			logger.info(String.format("Time taken on Assign Offer Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Assign Offer Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getContractOffers(ContractOffersRequestPayload payload, PageRequest pageRequest) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Contract Offers Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<ContractOffers> contractOffers = null;

			if (payload.getOfferCode() != null && !payload.getOfferCode().isEmpty()) {
				contractOffers = contractOffersRepository.findByOfferCode(payload.getOfferCode(),
						payload.getContractCode());
			} else {
				contractOffers = contractOffersRepository.getAllOffers(payload.getContractCode());
			}

			if (contractOffers != null && !contractOffers.isEmpty()) {
				logger.info("Contract Offers Recods Exist");
				List<ContractOffersDTO> contractOffersDTOs = new ArrayList<ContractOffersDTO>();
				for (ContractOffers contractOffer : contractOffers) {
					ContractOffersDTO contractOfferDTO = new ContractOffersDTO();
					contractOfferDTO.setContractOfferId(contractOffer.getId());
					contractOfferDTO.setContractCode(contractOffer.getContract().getCode());
					contractOfferDTO.setOfferCode(contractOffer.getOffers().getOfferCode());
					contractOfferDTO.setCreatedTimestamp(contractOffer.getCreatedDate());
					contractOfferDTO.setStartDate(contractOffer.getStartDate());
					contractOfferDTO.setEndDate(contractOffer.getEndDate());
					contractOfferDTO.setTriggerType(contractOffer.getTriggerType());
					contractOfferDTO.setStatus(contractOffer.isActive());
					contractOfferDTO.setCampCode(contractOffer.getCampCode());
					if (contractOffer.getPackageValue() != null) {
						contractOfferDTO.setPackageDetails(contractOffer.getPackageValue());
						//contractOfferDTO.setPackageDetails(StringUtil.jsonStringToPojo(contractOffer.getPackageValue(),Object.class));
					}
					contractOffersDTOs.add(contractOfferDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", contractOffersDTOs);
				stopWatch.stop();
				logger.info("Get Contract Offers Service Success");
				logger.info(String.format("Time taken on get Contract Offers Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contract Offers Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Contract Offers Service failed");
				logger.info("Contract Offers Not Found :- ");
				logger.info(String.format("Time taken on get Contract Offers Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contract Offers Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Contract Offers Service.", e);
			logger.info(
					String.format("Time taken on get Contract Offers Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Contract Offers Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> updateContractOffer(UpdateContractOfferPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			logger.info("Start of Update Contract Offer Service");

			ContractOffers contractOfferExist = contractOffersRepository
					.findById(Long.parseLong(payload.getContractOfferId())).orElse(null);

			if (contractOfferExist != null) {
				ContractOffers contractoffer = contractOfferExist;
				contractoffer.setTriggerType(payload.getTriggerType());
				contractoffer.setCampCode(payload.getCampCode());
				contractoffer.setPackageValue(StringUtil.pojoToString(payload.getPackageDetails()));
				contractoffer.setStartDate(Timestamp.valueOf(payload.getStartDate()));
				contractoffer.setEndDate(Timestamp.valueOf(payload.getEndDate()));
				contractoffer.setUpdatedBy(payload.getUserId());
				contractoffer.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
				contractOffersRepository.save(contractoffer);

				logger.info("Updation of Contract Offer in DB Success");
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Update Contract Offer Success");
				logger.info(String.format("Time taken on Update Contract Offer Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Offer Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Update Contract Offer failed");
				logger.info(String.format("Contract Offer Not Exist :-"));
				logger.info(String.format("Time taken on Update Contract Offer Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Update Contract Offer Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Update Contract Offer Service.", e);
			logger.info(String.format("Time taken on Update Contract Offer Service :- %s",
					stopWatch.getTotalTimeSeconds()));
			logger.info("End of Update Contract Offer Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> getContractOffers(ContractOffersRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info("Start of get Contract Offers Service : ");
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			List<ContractOffers> contractOffers = contractOffersRepository.getAllOffers(payload.getContractCode(),
					true);

			if (contractOffers != null && !contractOffers.isEmpty()) {
				logger.info("Contract Offers Recods Exist");
				List<ContractOfferDTO> contractOffersDTOs = new ArrayList<ContractOfferDTO>();
				for (ContractOffers contractOffer : contractOffers) {
					ContractOfferDTO contractOfferDTO = new ContractOfferDTO();
					contractOfferDTO.setOfferCode(contractOffer.getOffers().getOfferCode());
					contractOfferDTO.setOfferName(contractOffer.getOffers().getOfferName());
					contractOffersDTOs.add(contractOfferDTO);
				}
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", contractOffersDTOs);
				stopWatch.stop();
				logger.info("Get Contract Offers Service Success");
				logger.info(String.format("Time taken on get Contract Offers Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contract Offers Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.RECORDSNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", new ArrayList());
				stopWatch.stop();
				logger.info("Get Contract Offers Service failed");
				logger.info("Contract Offers Not Found :- ");
				logger.info(String.format("Time taken on get Contract Offers Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Contract Offers Service %s", CommonMessageLog.RECORDSNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get Contract Offers Service.", e);
			logger.info(
					String.format("Time taken on get Contract Offers Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get Contract Offers Service.");
			return responseMap;
		}
	}
}
