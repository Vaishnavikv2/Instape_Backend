package com.instape.app.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.instape.app.cloudsql.model.Contract;
import com.instape.app.cloudsql.model.ContractProcessConfig;
import com.instape.app.cloudsql.model.ProcessConfig;
import com.instape.app.cloudsql.model.ProcessConfigDTO;
import com.instape.app.cloudsql.model.WorkFlowRunDTO;
import com.instape.app.cloudsql.model.WorkflowConfig;
import com.instape.app.cloudsql.model.WorkflowConfigDTO;
import com.instape.app.cloudsql.model.WorkflowRun;
import com.instape.app.cloudsql.model.WorkflowStep;
import com.instape.app.cloudsql.model.WorkflowStepsDTO;
import com.instape.app.cloudsql.repository.ContractProcessConfigRepository;
import com.instape.app.cloudsql.repository.ContractRepository;
import com.instape.app.cloudsql.repository.ProcessConfigRepository;
import com.instape.app.cloudsql.repository.WorkflowConfigRepository;
import com.instape.app.cloudsql.repository.WorkflowRunRepository;
import com.instape.app.cloudsql.repository.WorkflowStepRepository;
import com.instape.app.cloudsql.specification.WorkflowRunSpecification;
import com.instape.app.request.ContractDetailRequestPayload;
import com.instape.app.request.WorkflowReportRequest;
import com.instape.app.response.PageResponse;
import com.instape.app.response.PageableResponseDTO;
import com.instape.app.response.WorkflowResponsePayload;
import com.instape.app.service.WorkflowService;
import com.instape.app.utils.CommonMessageLog;
import com.instape.app.utils.DateUtils;
import com.instape.app.utils.PortalConstant;
import com.instape.app.utils.StatusCode;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 22-Jul-2024
 * @ModifyDate - 22-Jul-2024
 * @Desc -
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

	static final Logger logger = LogManager.getLogger(WorkflowServiceImpl.class);

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ContractProcessConfigRepository contractProcessConfigRepository;

	@Autowired
	private ProcessConfigRepository processConfigRepository;

	@Autowired
	private WorkflowConfigRepository workflowConfigRepository;

	@Autowired
	private WorkflowStepRepository workflowStepRepository;

	@Autowired
	private WorkflowRunRepository workflowRunRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public Map<Object, Object> getWorkFlows(ContractDetailRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info(String.format("Start of get WorkFlow Service : %s", payload.getContractCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Contract contractExist = contractRepository.findByCode(payload.getContractCode());
			if (contractExist != null) {
				logger.info("Contract Found Successfully...");

				WorkflowResponsePayload responsePayload = new WorkflowResponsePayload();
				responseMap.put("data", responsePayload);
				List<ContractProcessConfig> contractProcessConfigs = contractExist.getContractProcessConfigs();
				if (contractProcessConfigs != null && !contractProcessConfigs.isEmpty()) {
					ContractProcessConfig contractProcessConfig = contractProcessConfigs.get(0);
					responsePayload.setContractCode(contractExist.getCode());
					responsePayload.setStatus(contractProcessConfig.getStatus());
					responsePayload.setId(contractProcessConfig.getId());
					responsePayload.setUserId(0);
					List<ProcessConfig> processConfigs = contractProcessConfig.getProcessConfigs();
					if (processConfigs != null && !processConfigs.isEmpty()) {
						List<ProcessConfig> sortedProcessConfigs = processConfigs.stream()
								.filter(processConfig -> processConfig.getIsDeleted() == false)
								.sorted(Comparator.comparingInt(ProcessConfig::getProcessConfigSeq))
								.collect(Collectors.toList());
						List<ProcessConfigDTO> processConfigDTOs = new ArrayList<ProcessConfigDTO>();
						for (ProcessConfig processConfig : sortedProcessConfigs) {
							ProcessConfigDTO processConfigDTO = new ProcessConfigDTO();
							processConfigDTO.setName(processConfig.getProcessName());
							processConfigDTO.setStatus(processConfig.getStatus());
							processConfigDTO.setDeleted(processConfig.getIsDeleted());
							processConfigDTO.setId(processConfig.getPid());
							processConfigDTO.setSeqId(processConfig.getProcessConfigSeq());
							List<WorkflowConfig> workflowConfigs = processConfig.getWorkflowConfigs();
							if (workflowConfigs != null && !workflowConfigs.isEmpty()) {
								List<WorkflowConfig> sortedWorkflowConfigs = workflowConfigs.stream()
										.filter(workflowConfig -> workflowConfig.getIsDeleted() == false)
										.sorted(Comparator.comparingInt(WorkflowConfig::getWorkflowConfigSeq))
										.collect(Collectors.toList());
								List<WorkflowConfigDTO> workflowConfigDTOs = new ArrayList<WorkflowConfigDTO>();
								for (WorkflowConfig workflowConfig : sortedWorkflowConfigs) {
									WorkflowConfigDTO workflowConfigDTO = new WorkflowConfigDTO();
									workflowConfigDTO.setDesc(workflowConfig.getWorkflowDesc());
									workflowConfigDTO.setWorkflowId(workflowConfig.getGcpWorkflowId());
									workflowConfigDTO.setStatus(workflowConfig.getStatus());
									workflowConfigDTO.setDeleted(workflowConfig.getIsDeleted());
									workflowConfigDTO.setId(workflowConfig.getWid());
									workflowConfigDTO.setSeqId(workflowConfig.getWorkflowConfigSeq());
									List<WorkflowStep> workflowSteps = workflowConfig.getWorkflowSteps();
									if (workflowSteps != null && !workflowSteps.isEmpty()) {
										List<WorkflowStep> sortedWorkflowSteps = workflowSteps.stream()
												.filter(workflowStep -> workflowStep.getIsDeleted() == false)
												.sorted(Comparator.comparingInt(WorkflowStep::getWorkflowStepsSeq))
												.collect(Collectors.toList());
										List<WorkflowStepsDTO> workflowStepsDTOs = new ArrayList<WorkflowStepsDTO>();
										for (WorkflowStep workflowStep : sortedWorkflowSteps) {
											WorkflowStepsDTO workflowStepsDTO = new WorkflowStepsDTO();
											workflowStepsDTO.setDesc(workflowStep.getStepDesc());
											workflowStepsDTO.setName(workflowStep.getFunctionName());
											workflowStepsDTO.setId(workflowStep.getId());
											workflowStepsDTO.setSeqId(workflowStep.getWorkflowStepsSeq());
											workflowStepsDTO.setStatus(workflowStep.getStatus());
											workflowStepsDTO.setDeleted(workflowStep.getIsDeleted());
											workflowStepsDTOs.add(workflowStepsDTO);
											logger.info("Workflow Step Added Successfully :- "
													+ workflowStep.getFunctionName());
										}
										if (workflowStepsDTOs != null && !workflowStepsDTOs.isEmpty()) {
											workflowConfigDTO.setSteps(workflowStepsDTOs);
										} else {
											workflowConfigDTO.setSteps(new ArrayList<>());
										}
									}
									workflowConfigDTOs.add(workflowConfigDTO);
									logger.info("Workflow Config Added Successfully :- "
											+ workflowConfig.getGcpWorkflowId());
								}
								if (workflowConfigDTOs != null && !workflowConfigDTOs.isEmpty()) {
									processConfigDTO.setWorkflows(workflowConfigDTOs);
								} else {

									processConfigDTO.setWorkflows(new ArrayList<>());
								}
								processConfigDTO.setWorkflows(workflowConfigDTOs);
							}
							processConfigDTOs.add(processConfigDTO);
							logger.info("Process Config Added Successfully :- " + processConfig.getProcessName());
						}
						if (processConfigDTOs != null && !processConfigDTOs.isEmpty()) {
							responsePayload.setData(processConfigDTOs);
						} else {
							responsePayload.setData(new ArrayList<>());
						}
					}
					responseMap.put("data", responsePayload);
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Get WorkFlow Service Success");
				logger.info(String.format("Time taken on get WorkFlow Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get WorkFlow Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Get WorkFlow Service failed");
				logger.info(String.format("Contract Not Found :- %s", payload.getContractCode()));
				logger.info(String.format("Time taken on get WorkFlow Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get WorkFlow Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get WorkFlow Service.", e);
			logger.info(String.format("Time taken on get WorkFlow Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get WorkFlow Service.");
			return responseMap;
		}
	}

	@Override
	public Map<Object, Object> addUpdateWorkFlows(WorkflowResponsePayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info(String.format("Start of Add/Update WorkFlow Service : %s", payload.getContractCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Contract contractExist = contractRepository.findByCode(payload.getContractCode());
			if (contractExist != null) {
				logger.info("Contract Found Successfully...");
				String userId = Long.toString(payload.getUserId());
				ContractProcessConfig contractProcessConfig = null;
				int processCount = payload.getData() == null ? 0
						: payload.getData().stream().filter(processConfigDTO -> processConfigDTO.isDeleted() == false)
								.collect(Collectors.toList()).size();
				if (payload.getId() > 0) {
					logger.info("Updating Existing Contract Process Config...");
					contractProcessConfig = contractProcessConfigRepository.findById(payload.getId()).orElse(null);
					if (contractProcessConfig != null) {
						if (contractProcessConfig.getStatus() == null || contractProcessConfig.getStatus().isEmpty()
								|| !contractProcessConfig.getStatus().equals(payload.getStatus())
								|| contractProcessConfig.getProcessCount() != processCount) {
							contractProcessConfig.setStatus(payload.getStatus());
							contractProcessConfig.setProcessCount(processCount);
							contractProcessConfig.setUpdatedBy(userId);
							contractProcessConfig
									.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
							contractProcessConfig = contractProcessConfigRepository.save(contractProcessConfig);
							logger.info("Contract Process Config Updated Successfully...");
						}
					} else {
						responseMap.put("code", PortalConstant.OK);
						responseMap.put("message", CommonMessageLog.CONTRACTPROCESSCONFIGNOTFOUND);
						responseMap.put("status", CommonMessageLog.FAILED);
						stopWatch.stop();
						logger.info("Add/Update WorkFlow Service failed");
						logger.info(
								String.format("Contract Process Config Not Found :- %s", payload.getContractCode()));
						logger.info(String.format("Time taken on Add/Update WorkFlow Service :- %s",
								stopWatch.getTotalTimeSeconds()));
						logger.info(String.format("End of Add/Update WorkFlow Service %s",
								CommonMessageLog.CONTRACTPROCESSCONFIGNOTFOUND));
						return responseMap;
					}
				} else {
					logger.info("Adding New Contract Process Config...");
					contractProcessConfig = new ContractProcessConfig();
					contractProcessConfig.setContract(contractExist);
					contractProcessConfig.setProcessCount(processCount);
					contractProcessConfig.setCreatedBy(userId);
					contractProcessConfig.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					contractProcessConfig.setUpdatedBy(userId);
					contractProcessConfig.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
					contractProcessConfig.setStatus(payload.getStatus());
					contractProcessConfig = contractProcessConfigRepository.save(contractProcessConfig);
					logger.info("Contract Process Config Added Successfully...");
				}

				if (contractProcessConfig != null) {
					if (payload.getData() != null && !payload.getData().isEmpty()) {
						List<ProcessConfigDTO> ProcessConfigDTOs = payload.getData();
						for (ProcessConfigDTO processConfigDTO : ProcessConfigDTOs) {
							int workflowConfigCount = processConfigDTO.getWorkflows() == null ? 0
									: processConfigDTO.getWorkflows().stream()
											.filter(workflowConfigDTO -> workflowConfigDTO.isDeleted() == false)
											.collect(Collectors.toList()).size();
							if (processConfigDTO.getId() > 0) {
								logger.info("Updating Existing Process Config...");
								ProcessConfig processConfig = processConfigRepository.findById(processConfigDTO.getId())
										.orElse(null);
								if (processConfig != null) {
									if (processConfig.getProcessConfigSeq() != processConfigDTO.getSeqId()
											|| processConfig.getProcessName() == null
											|| processConfig.getProcessName().isEmpty()
											|| !processConfig.getProcessName().equals(processConfigDTO.getName())
											|| processConfig.getWorkflowCount() != workflowConfigCount
											|| processConfig.getStatus() == null || processConfig.getStatus().isEmpty()
											|| !processConfig.getStatus().equals(processConfigDTO.getStatus())
											|| processConfig.getIsDeleted() == null
											|| processConfig.getIsDeleted() != processConfigDTO.isDeleted()) {
										processConfig.setProcessConfigSeq(processConfigDTO.getSeqId());
										processConfig.setProcessName(processConfigDTO.getName());
										processConfig.setStatus(processConfigDTO.getStatus());
										processConfig.setWorkflowCount(workflowConfigCount);
										processConfig.setIsDeleted(processConfigDTO.isDeleted());
										processConfig.setUpdatedBy(userId);
										processConfig.setUpdatedDate(
												Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
										processConfig = processConfigRepository.save(processConfig);
										logger.info("Process Config Updated Successfully : "
												+ processConfig.getProcessName());
									}

									if (processConfigDTO.getWorkflows() != null
											&& !processConfigDTO.getWorkflows().isEmpty()) {
										List<WorkflowConfigDTO> workflowConfigDTOs = processConfigDTO.getWorkflows();
										for (WorkflowConfigDTO workflowConfigDTO : workflowConfigDTOs) {
											if (workflowConfigDTO.getId() > 0) {
												logger.info("Updating Existing Workflow Config...");
												WorkflowConfig workflowConfig = workflowConfigRepository
														.findById(workflowConfigDTO.getId()).orElse(null);
												if (workflowConfig != null) {
													if (workflowConfig.getWorkflowConfigSeq() != workflowConfigDTO
															.getSeqId() || workflowConfig.getGcpWorkflowId() == null
															|| workflowConfig.getGcpWorkflowId().isEmpty()
															|| !workflowConfig.getGcpWorkflowId()
																	.equals(workflowConfigDTO.getWorkflowId())
															|| workflowConfig.getWorkflowDesc() == null
															|| workflowConfig.getWorkflowDesc().isEmpty()
															|| !workflowConfig.getWorkflowDesc()
																	.equals(workflowConfigDTO.getDesc())
															|| workflowConfig.getStatus() == null
															|| workflowConfig.getStatus().isEmpty()
															|| !workflowConfig.getStatus()
																	.equals(workflowConfigDTO.getStatus())
															|| workflowConfig.getIsDeleted() == null || workflowConfig
																	.getIsDeleted() != workflowConfigDTO.isDeleted()) {
														workflowConfig
																.setWorkflowConfigSeq(workflowConfigDTO.getSeqId());
														workflowConfig
																.setGcpWorkflowId(workflowConfigDTO.getWorkflowId());
														workflowConfig.setWorkflowDesc(workflowConfigDTO.getDesc());
														workflowConfig.setStatus(workflowConfigDTO.getStatus());
														workflowConfig.setIsDeleted(workflowConfigDTO.isDeleted());
														workflowConfig.setUpdatedBy(userId);
														workflowConfig.setUpdatedDate(Timestamp
																.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
														workflowConfig = workflowConfigRepository.save(workflowConfig);
														logger.info("Workflow Config Updated Successfully : "
																+ workflowConfigDTO.getWorkflowId());
													}

													List<WorkflowStepsDTO> workflowStepsDTOs = workflowConfigDTO
															.getSteps();
													for (WorkflowStepsDTO workflowStepsDTO : workflowStepsDTOs) {
														if (workflowStepsDTO.getId() > 0) {
															logger.info("Updating Existing Workflow Step...");
															WorkflowStep workflowStep = workflowStepRepository
																	.findById(workflowStepsDTO.getId()).orElse(null);
															if (workflowStep != null) {
																if (workflowStep
																		.getWorkflowStepsSeq() != workflowStepsDTO
																				.getSeqId()
																		|| workflowStep.getFunctionName() == null
																		|| workflowStep.getFunctionName().isEmpty()
																		|| !workflowStep.getFunctionName()
																				.equals(workflowStepsDTO.getName())
																		|| workflowStep.getStepDesc() == null
																		|| workflowStep.getStepDesc().isEmpty()
																		|| !workflowStep.getStepDesc()
																				.equals(workflowStepsDTO.getDesc())
																		|| workflowStep.getStatus() == null
																		|| workflowStep.getStatus().isEmpty()
																		|| !workflowStep.getStatus()
																				.equals(workflowStepsDTO.getStatus())
																		|| workflowStep.getIsDeleted() == null
																		|| workflowStep
																				.getIsDeleted() != workflowStepsDTO
																						.isDeleted()) {
																	workflowStep.setWorkflowStepsSeq(
																			workflowStepsDTO.getSeqId());
																	workflowStep.setFunctionName(
																			workflowStepsDTO.getName());
																	workflowStep
																			.setStepDesc(workflowStepsDTO.getDesc());
																	workflowStep
																			.setStatus(workflowStepsDTO.getStatus());
																	workflowStep
																			.setIsDeleted(workflowStepsDTO.isDeleted());
																	workflowStep.setUpdatedBy(userId);
																	workflowStep
																			.setUpdatedDate(Timestamp.valueOf(DateUtils
																					.dateInYYYYMMDDHHMMSS(new Date())));
																	workflowStep = workflowStepRepository
																			.save(workflowStep);
																	logger.info("Workflow Step Updated Successfully : "
																			+ workflowStepsDTO.getName());
																}
															} else {
																responseMap.put("code", PortalConstant.OK);
																responseMap.put("message",
																		CommonMessageLog.WORKFLOWSTEPNOTFOUND);
																responseMap.put("status", CommonMessageLog.FAILED);
																stopWatch.stop();
																logger.info("Add/Update WorkFlow Service failed");
																logger.info(
																		String.format("Workflow Step Not Found :- %s",
																				payload.getContractCode()));
																logger.info(String.format(
																		"Time taken on Add/Update WorkFlow Service :- %s",
																		stopWatch.getTotalTimeSeconds()));
																logger.info(String.format(
																		"End of Add/Update WorkFlow Service %s",
																		CommonMessageLog.WORKFLOWSTEPNOTFOUND));
																return responseMap;
															}
														} else {
															logger.info("Adding New Workflow Step...");
															WorkflowStep workflowStep = new WorkflowStep();
															workflowStep.setCreatedBy(userId);
															workflowStep.setCreatedDate(Timestamp.valueOf(
																	DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
															workflowStep.setUpdatedBy(userId);
															workflowStep.setUpdatedDate(Timestamp.valueOf(
																	DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
															workflowStep
																	.setWorkflowStepsSeq(workflowStepsDTO.getSeqId());
															workflowStep.setStepDesc(workflowStepsDTO.getDesc());
															workflowStep.setFunctionName(workflowStepsDTO.getName());
															workflowStep.setStatus(workflowStepsDTO.getStatus());
															workflowStep.setIsDeleted(workflowStepsDTO.isDeleted());
															workflowStep.setWorkflowConfig(workflowConfig);
															workflowStep = workflowStepRepository.save(workflowStep);
															logger.info("Workflow Step Added Successfully : "
																	+ workflowStepsDTO.getName());
														}
													}
												} else {
													responseMap.put("code", PortalConstant.OK);
													responseMap.put("message", CommonMessageLog.WORKFLOWCONFIGNOTFOUND);
													responseMap.put("status", CommonMessageLog.FAILED);
													stopWatch.stop();
													logger.info("Add/Update WorkFlow Service failed");
													logger.info(String.format("Workflow Config Not Found :- %s",
															payload.getContractCode()));
													logger.info(String.format(
															"Time taken on Add/Update WorkFlow Service :- %s",
															stopWatch.getTotalTimeSeconds()));
													logger.info(String.format("End of Add/Update WorkFlow Service %s",
															CommonMessageLog.WORKFLOWCONFIGNOTFOUND));
													return responseMap;
												}
											} else {
												logger.info("Adding New Workflow Config...");
												WorkflowConfig workflowConfig = new WorkflowConfig();
												workflowConfig.setCreatedBy(userId);
												workflowConfig.setCreatedDate(
														Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
												workflowConfig.setUpdatedBy(userId);
												workflowConfig.setUpdatedDate(
														Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
												workflowConfig.setWorkflowConfigSeq(workflowConfigDTO.getSeqId());
												workflowConfig.setWorkflowDesc(workflowConfigDTO.getDesc());
												workflowConfig.setStatus(workflowConfigDTO.getStatus());
												workflowConfig.setIsDeleted(workflowConfigDTO.isDeleted());
												workflowConfig.setGcpWorkflowId(workflowConfigDTO.getWorkflowId());
												workflowConfig.setProcessConfig(processConfig);
												workflowConfig = workflowConfigRepository.save(workflowConfig);
												logger.info("Workflow Config Added Successfully : "
														+ workflowConfigDTO.getWorkflowId());

												if (workflowConfigDTO.getSteps() != null
														&& !workflowConfigDTO.getSteps().isEmpty()) {
													List<WorkflowStepsDTO> workflowStepsDTOs = workflowConfigDTO
															.getSteps();
													for (WorkflowStepsDTO workflowStepsDTO : workflowStepsDTOs) {
														logger.info("Adding New Workflow Step...");
														WorkflowStep workflowStep = new WorkflowStep();
														workflowStep.setCreatedBy(userId);
														workflowStep.setCreatedDate(Timestamp
																.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
														workflowStep.setUpdatedBy(userId);
														workflowStep.setUpdatedDate(Timestamp
																.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
														workflowStep.setWorkflowStepsSeq(workflowStepsDTO.getSeqId());
														workflowStep.setStepDesc(workflowStepsDTO.getDesc());
														workflowStep.setFunctionName(workflowStepsDTO.getName());
														workflowStep.setStatus(workflowStepsDTO.getStatus());
														workflowStep.setIsDeleted(workflowStepsDTO.isDeleted());
														workflowStep.setWorkflowConfig(workflowConfig);
														workflowStep = workflowStepRepository.save(workflowStep);
														logger.info("Workflow Step Added Successfully : "
																+ workflowStepsDTO.getName());
													}
												}
											}
										}
									}
								} else {
									responseMap.put("code", PortalConstant.OK);
									responseMap.put("message", CommonMessageLog.PROCESSCONFIGNOTFOUND);
									responseMap.put("status", CommonMessageLog.FAILED);
									stopWatch.stop();
									logger.info("Add/Update WorkFlow Service failed");
									logger.info(
											String.format("Process Config Not Found :- %s", payload.getContractCode()));
									logger.info(String.format("Time taken on Add/Update WorkFlow Service :- %s",
											stopWatch.getTotalTimeSeconds()));
									logger.info(String.format("End of Add/Update WorkFlow Service %s",
											CommonMessageLog.PROCESSCONFIGNOTFOUND));
									return responseMap;
								}
							} else {
								logger.info("Adding New Process Config...");
								ProcessConfig processConfig = new ProcessConfig();
								processConfig.setCreatedBy(userId);
								processConfig
										.setCreatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								processConfig.setUpdatedBy(userId);
								processConfig
										.setUpdatedDate(Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
								processConfig.setProcessConfigSeq(processConfigDTO.getSeqId());
								processConfig.setProcessName(processConfigDTO.getName());
								processConfig.setStatus(processConfigDTO.getStatus());
								processConfig.setIsDeleted(processConfigDTO.isDeleted());
								processConfig.setWorkflowCount(workflowConfigCount);
								processConfig.setContractProcessConfig(contractProcessConfig);
								processConfig = processConfigRepository.save(processConfig);
								logger.info("Process Config Added Successfully : " + processConfigDTO.getName());

								if (processConfigDTO.getWorkflows() != null
										&& !processConfigDTO.getWorkflows().isEmpty()) {
									List<WorkflowConfigDTO> workflowConfigDTOs = processConfigDTO.getWorkflows();
									for (WorkflowConfigDTO workflowConfigDTO : workflowConfigDTOs) {
										logger.info("Adding New Workflow Config...");
										WorkflowConfig workflowConfig = new WorkflowConfig();
										workflowConfig.setCreatedBy(userId);
										workflowConfig.setCreatedDate(
												Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
										workflowConfig.setUpdatedBy(userId);
										workflowConfig.setUpdatedDate(
												Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
										workflowConfig.setWorkflowConfigSeq(workflowConfigDTO.getSeqId());
										workflowConfig.setWorkflowDesc(workflowConfigDTO.getDesc());
										workflowConfig.setStatus(workflowConfigDTO.getStatus());
										workflowConfig.setIsDeleted(workflowConfigDTO.isDeleted());
										workflowConfig.setGcpWorkflowId(workflowConfigDTO.getWorkflowId());
										workflowConfig.setProcessConfig(processConfig);
										workflowConfig = workflowConfigRepository.save(workflowConfig);
										logger.info("Workflow Config Added Successfully : "
												+ workflowConfigDTO.getWorkflowId());

										if (workflowConfigDTO.getSteps() != null
												&& !workflowConfigDTO.getSteps().isEmpty()) {
											List<WorkflowStepsDTO> workflowStepsDTOs = workflowConfigDTO.getSteps();
											for (WorkflowStepsDTO workflowStepsDTO : workflowStepsDTOs) {
												logger.info("Adding New Workflow Step...");
												WorkflowStep workflowStep = new WorkflowStep();
												workflowStep.setCreatedBy(userId);
												workflowStep.setCreatedDate(
														Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
												workflowStep.setUpdatedBy(userId);
												workflowStep.setUpdatedDate(
														Timestamp.valueOf(DateUtils.dateInYYYYMMDDHHMMSS(new Date())));
												workflowStep.setWorkflowStepsSeq(workflowStepsDTO.getSeqId());
												workflowStep.setStepDesc(workflowStepsDTO.getDesc());
												workflowStep.setFunctionName(workflowStepsDTO.getName());
												workflowStep.setStatus(workflowStepsDTO.getStatus());
												workflowStep.setIsDeleted(workflowStepsDTO.isDeleted());
												workflowStep.setWorkflowConfig(workflowConfig);
												workflowStep = workflowStepRepository.save(workflowStep);
												logger.info("Workflow Step Added Successfully : "
														+ workflowStepsDTO.getName());
											}
										}
									}
								}
							}
						}
					}
				}

				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				stopWatch.stop();
				logger.info("Add/Update WorkFlow Service Success");
				logger.info(String.format("Time taken on Add/Update WorkFlow Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add/Update WorkFlow Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				stopWatch.stop();
				logger.info("Add/Update WorkFlow Service failed");
				logger.info(String.format("Contract Not Found :- %s", payload.getContractCode()));
				logger.info(String.format("Time taken on Add/Update WorkFlow Service :- %s",
						stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of Add/Update WorkFlow Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of Add/Update WorkFlow Service.", e);
			logger.info(
					String.format("Time taken on Add/Update WorkFlow Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of Add/Update WorkFlow Service.");
			return responseMap;
		}
	}

	@Override
	public PageableResponseDTO getWorkFlowsReport(WorkflowReportRequest payload, final Pageable pageRequest,
			String sortBy, String sortOrder) {
		Contract contractExist = contractRepository.findByCode(payload.getContractCode());
		if (contractExist == null) {
			return new PageableResponseDTO("Contract not found", StatusCode.ENTITY_NOT_FOUND, Collections.EMPTY_LIST,
					new PageResponse());
		}
		Specification<WorkflowRun> spec = Specification
				.where(WorkflowRunSpecification.hasContractCode(payload.getContractCode()));
		if (payload.getStatus() != null && !payload.getStatus().isEmpty()) {
			spec = spec.and(WorkflowRunSpecification.hasStatus(payload.getStatus()));
		}
		if (payload.getFunctionName() != null && !payload.getFunctionName().isEmpty()) {
			spec = spec.and(WorkflowRunSpecification.hasFunctionName(payload.getFunctionName()));
		}
		if (payload.getEmployeeId() != null && !payload.getEmployeeId().isEmpty()) {
			spec = spec.and(WorkflowRunSpecification.hasEmployeeCode(payload.getEmployeeId()));
		}
		if (payload.getWorkflowName() != null && !payload.getWorkflowName().isEmpty()) {
			spec = spec.and(WorkflowRunSpecification.hasWorkflowName(payload.getWorkflowName()));
		}
		if (payload.getStartDate() != null && !payload.getStartDate().isEmpty() && payload.getEndDate() != null
				&& !payload.getEndDate().isEmpty()) {
			spec = spec.and(WorkflowRunSpecification.hasDateRange(Timestamp.valueOf(payload.getStartDate()),
					Timestamp.valueOf(payload.getEndDate())));
		}
		if (payload.getLoanApprovalStatus() != null && !payload.getLoanApprovalStatus().isEmpty()) {
			spec = spec.and(WorkflowRunSpecification.hasLoanApprovalStatus(payload.getLoanApprovalStatus()));
		}
		Sort sort = buildSort(sortBy, sortOrder);
		PageResponse pagination = new PageResponse();
		List<WorkFlowRunDTO> globalErrorLogsDTOs = new ArrayList<>();
		if (pageRequest == null) {
			List<WorkflowRun> page = workflowRunRepository.findAll(spec, sort);
			globalErrorLogsDTOs = page.stream().map(log -> convertToDto(log)).toList();
		} else {
			Pageable finalPageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
			Page<WorkflowRun> page = workflowRunRepository.findAll(spec, finalPageRequest);
			globalErrorLogsDTOs = page.getContent().stream().map(log -> convertToDto(log)).toList();
			pagination = new PageResponse(page.getTotalElements(), page.getPageable().getPageNumber(),
					page.getNumberOfElements(), page.getSize());
		}
		logger.info("Total Workflow run fetched:{}", globalErrorLogsDTOs.size());
		logger.info("Get  Workflow run  Service Success");
		return new PageableResponseDTO("Record fetched", StatusCode.OK, globalErrorLogsDTOs, pagination);
	}

	private Sort buildSort(String sortBy, String sortOrder) {
		// Determine sort direction: ascending or descending
		Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		switch (sortBy) {
		case "createdDate":
			return Sort.by(direction, "createdDate");
		case "status":
			return Sort.by(direction, "status");
		case "functionName":
			return Sort.by(direction, "functionName");
		case "employeeName":
			// For sorting by a nested field like employeeInfo.name, use the property path
			return Sort.by(direction, "employeeInfo.name");
		case "contractCode":
			return Sort.by(direction, "contract.code");
		default:
			// Default sort by createdDate if no valid field is specified
			return Sort.by(direction, "createdDate");
		}
	}

	private WorkFlowRunDTO convertToDto(WorkflowRun log) {
		WorkFlowRunDTO dto = modelMapper.map(log, WorkFlowRunDTO.class);
		dto.setTimestamp(log.getCreatedDate());
		dto.setEmployeeId(log.getEmployeeInfo().getCode());
		dto.setEmployeeName(log.getEmployeeInfo().getEmployeeName());
		dto.setWorkflowName(log.getWorkflowStep().getWorkflowConfig().getGcpWorkflowId());
		return dto;
	}

	@Override
	public Map<Object, Object> getFunctions(ContractDetailRequestPayload payload) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		logger.info(String.format("Start of get Functions Service : %s", payload.getContractCode()));
		Map<Object, Object> responseMap = new LinkedHashMap<Object, Object>();
		try {
			Contract contract = contractRepository.findByCode(payload.getContractCode());
			if (contract != null) {
				logger.info("Contract Found Successfully...");
				Set<String> functions = workflowStepRepository.getFunctionsByContractCode(payload.getContractCode());
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.SUCCESS);
				responseMap.put("status", CommonMessageLog.SUCCESS);
				responseMap.put("data", functions);

				stopWatch.stop();
				logger.info("Get Functions Service Success");
				logger.info(
						String.format("Time taken on get Functions Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Functions Service %s", CommonMessageLog.SUCCESS));
				return responseMap;
			} else {
				responseMap.put("code", PortalConstant.OK);
				responseMap.put("message", CommonMessageLog.CONTRACTNOTFOUND);
				responseMap.put("status", CommonMessageLog.FAILED);
				responseMap.put("data", null);
				stopWatch.stop();
				logger.info("Get Functions Service failed");
				logger.info(String.format("Contract Not Found :- %s", payload.getContractCode()));
				logger.info(
						String.format("Time taken on get Functions Service :- %s", stopWatch.getTotalTimeSeconds()));
				logger.info(String.format("End of get Functions Service %s", CommonMessageLog.CONTRACTNOTFOUND));
				return responseMap;
			}
		} catch (Exception e) {
			responseMap.put("code", PortalConstant.INTERNALSERVERERROR);
			responseMap.put("status", CommonMessageLog.FAILED);
			responseMap.put("message", e.getMessage());
			stopWatch.stop();
			logger.error("Error:- Failed in catch  block of get WorkFlow Service.", e);
			logger.info(String.format("Time taken on get WorkFlow Service :- %s", stopWatch.getTotalTimeSeconds()));
			logger.info("End of get WorkFlow Service.");
			return responseMap;
		}
	}

}
