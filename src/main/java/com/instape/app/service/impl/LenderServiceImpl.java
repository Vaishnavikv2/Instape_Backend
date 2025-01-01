package com.instape.app.service.impl;

import org.springframework.stereotype.Service;

import com.instape.app.service.LenderService;

@Service
public class LenderServiceImpl implements LenderService {
	/*
	 * private final Logger logger = LogManager.getLogger(LenderServiceImpl.class);
	 * 
	 * @Autowired private LenderRepository lenderRepository;
	 * 
	 * @Autowired private ModelMapper mapper;
	 * 
	 * @Autowired private ProductCatalogRepository productCatalogRepository;
	 * 
	 * @Autowired private LenderProductMappingRepository
	 * lenderProductMappingRepository;
	 * 
	 * @Override
	 * 
	 * @Transactional public LenderDTO create(Long loggedInUser, LenderDTO
	 * lenderDto) { logger.info("Creating Lender"); if
	 * (lenderRepository.existsByCode(lenderDto.getCode())) {
	 * logger.error("Lender already exists with same code, Code:{}",
	 * lenderDto.getCode()); throw new
	 * InstapeException("Lender already exists with same code",
	 * StatusCode.ALREADY_EXIST); } Lender lender = mapper.map(lenderDto,
	 * Lender.class); lender.setCreatedBy(loggedInUser.toString());
	 * lender.setUpdatedBy(loggedInUser.toString());
	 * lender.setCreatedDate(DateUtils.getCurrentTimestamp());
	 * lender.setUpdatedDate(DateUtils.getCurrentTimestamp());
	 * lender.setStatus(PortalConstant.ACTIVE); lender.setDeleted(false); lender =
	 * lenderRepository.save(lender);
	 * logger.info("Lender Creation Successfull, Lender:{}", lender.getName());
	 * 
	 * List<Long> products = lenderDto.getProducts(); if (products != null &&
	 * !products.isEmpty()) { List<LenderProductMapping> lenderProductMappings = new
	 * ArrayList<LenderProductMapping>(); for (Long productId : products) {
	 * ProductCatalog productCatalog =
	 * productCatalogRepository.findById(productId).orElse(null); if (productCatalog
	 * != null) { lenderProductMappings.add(createLenderProductMapping(loggedInUser,
	 * lender, productCatalog)); } }
	 * lenderProductMappingRepository.saveAll(lenderProductMappings);
	 * logger.info("Lender Product Catalog Mapped Successfully"); } return
	 * mapper.map(lender, LenderDTO.class); }
	 * 
	 * @Override
	 * 
	 * @Transactional public LenderDTO update(Long loggedInUser, LenderDTO
	 * lenderDto) { logger.info("updating lender"); Lender lender =
	 * lenderRepository.getLenderById(lenderDto.getId()); if (lender == null) {
	 * throw new InstapeException("Lender does not exist",
	 * StatusCode.ENTITY_NOT_FOUND); } lender.setName(lenderDto.getName());
	 * lender.setLogoPath(lenderDto.getLogoPath());
	 * lender.setLenderAddress(lenderDto.getLenderAddress());
	 * lender.setCinNumber(lenderDto.getCinNumber());
	 * lender.setLenderEmail(lenderDto.getLenderEmail());
	 * lender.setLenderPhone(lenderDto.getLenderPhone());
	 * lender.setWebsite(lenderDto.getWebsite());
	 * lender.setGstNumber(lenderDto.getGstNumber());
	 * lender.setStatus(lenderDto.getStatus());
	 * lender.setUpdatedBy(loggedInUser.toString());
	 * lender.setUpdatedDate(DateUtils.getCurrentTimestamp()); lender =
	 * lenderRepository.save(lender);
	 * logger.info("Lender updated successfully, Lender:{}", lender.getName());
	 * //updating the lender products updateLenderProducts(loggedInUser, lenderDto,
	 * lender); return mapper.map(lender, LenderDTO.class); }
	 * 
	 * private void updateLenderProducts(Long loggedInUser, LenderDTO lenderDto,
	 * Lender lender) { List<Long> updateProductIds = lenderDto.getProducts();
	 * 
	 * List<LenderProductMapping> lenderProductMappings =
	 * lender.getLenderProductMapping(); List<Long> existingProductCatalogIds =
	 * lenderProductMappings.stream().filter(lpcm -> lpcm.isDeleted() == false)
	 * .map(lpcm -> lpcm.getProductCatalog().getId()).collect(Collectors.toList());
	 * 
	 * if (lenderProductMappings != null && !lenderProductMappings.isEmpty()) {
	 * lenderProductMappings = lenderProductMappings.stream().filter(lpcm ->
	 * lpcm.isDeleted() == false) .collect(Collectors.toList()); for
	 * (LenderProductMapping lenderProductMapping : lenderProductMappings) { if
	 * (!updateProductIds.contains(lenderProductMapping.getProductCatalog().getId())
	 * ) { lenderProductMapping.setDeleted(true);
	 * lenderProductMapping.setUpdatedBy(loggedInUser);
	 * lenderProductMapping.setUpdatedDate(DateUtils.getCurrentTimestamp()); } } }
	 * 
	 * for (Long productId : updateProductIds) { if
	 * (!existingProductCatalogIds.contains(productId)) { ProductCatalog
	 * productCatalog = productCatalogRepository.findById(productId).orElse(null);
	 * if (productCatalog != null) {
	 * lenderProductMappings.add(createLenderProductMapping(loggedInUser, lender,
	 * productCatalog)); } } }
	 * 
	 * lenderProductMappingRepository.saveAll(lenderProductMappings);
	 * logger.info("Lender Product Catalog Mapped Successfully"); }
	 * 
	 * private LenderProductMapping createLenderProductMapping(Long loggedInUser,
	 * Lender lender, ProductCatalog productCatalog) { LenderProductMapping
	 * lenderProductMapping = new LenderProductMapping();
	 * lenderProductMapping.setCreatedBy(loggedInUser);
	 * lenderProductMapping.setUpdatedBy(loggedInUser);
	 * lenderProductMapping.setCreatedDate(DateUtils.getCurrentTimestamp());
	 * lenderProductMapping.setUpdatedDate(DateUtils.getCurrentTimestamp());
	 * lenderProductMapping.setDeleted(false);
	 * lenderProductMapping.setLender(lender);
	 * lenderProductMapping.setProductCatalog(productCatalog);
	 * lenderProductMapping.setStatus(PortalConstant.ACTIVE); return
	 * lenderProductMapping; }
	 * 
	 * @Override public PageableResponseDTO getAll(final Pageable pageable) {
	 * logger.info("getting lender list"); Page<Lender> page =
	 * lenderRepository.getAllLenders(pageable); List<LenderDTO> lenderDtoList = new
	 * ArrayList<LenderDTO>(); for (Lender lender : page.getContent()) { LenderDTO
	 * lenderDto = mapper.map(lender, LenderDTO.class); List<ProductDetailDTO>
	 * productDetailDTOs = new ArrayList<ProductDetailDTO>();
	 * List<LenderProductMapping> lenderProductMappings =
	 * lender.getLenderProductMapping(); lenderProductMappings =
	 * lenderProductMappings.stream().filter(lpcm -> lpcm.isDeleted() == false)
	 * .collect(Collectors.toList()); for (LenderProductMapping lenderProductMapping
	 * : lenderProductMappings) { ProductDetailDTO productDetailDTO = new
	 * ProductDetailDTO();
	 * productDetailDTO.setId(lenderProductMapping.getProductCatalog().getId());
	 * productDetailDTO.setName(lenderProductMapping.getProductCatalog().getName());
	 * productDetailDTO.setCode(lenderProductMapping.getProductCatalog().getCode());
	 * productDetailDTOs.add(productDetailDTO); }
	 * lenderDto.setProductDetails(productDetailDTOs); lenderDtoList.add(lenderDto);
	 * } PageResponse pagination = new PageResponse(page.getTotalElements(),
	 * page.getPageable().getPageNumber(), page.getNumberOfElements(),
	 * page.getSize()); logger.info("Total lender fetched:{}",
	 * lenderDtoList.size()); return new PageableResponseDTO(lenderDtoList,
	 * pagination); }
	 * 
	 * @Override public LenderDTO getById(Long lenderId) {
	 * logger.info("getting lender by id"); Lender lender =
	 * lenderRepository.getLenderById(lenderId); if (lender == null) { throw new
	 * InstapeException("Lender does not exist", StatusCode.ENTITY_NOT_FOUND); }
	 * logger.info("Lender fetched successfully, Lender:{}", lender.getName());
	 * LenderDTO lenderDto = mapper.map(lender, LenderDTO.class);
	 * List<ProductDetailDTO> productDetailDTOs = new ArrayList<ProductDetailDTO>();
	 * List<LenderProductMapping> lenderProductMappings =
	 * lender.getLenderProductMapping(); lenderProductMappings =
	 * lenderProductMappings.stream().filter(lpcm -> lpcm.isDeleted() == false)
	 * .collect(Collectors.toList()); for (LenderProductMapping lenderProductMapping
	 * : lenderProductMappings) { ProductDetailDTO productDetailDTO = new
	 * ProductDetailDTO();
	 * productDetailDTO.setId(lenderProductMapping.getProductCatalog().getId());
	 * productDetailDTO.setName(lenderProductMapping.getProductCatalog().getName());
	 * productDetailDTO.setCode(lenderProductMapping.getProductCatalog().getCode());
	 * productDetailDTOs.add(productDetailDTO); }
	 * lenderDto.setProductDetails(productDetailDTOs); return lenderDto; }
	 * 
	 * @Override
	 * 
	 * @Transactional public void delete(Long loggedInUser, Long lenderId) {
	 * logger.info("deleting resources"); Lender lender =
	 * lenderRepository.getLenderById(lenderId); if (lender == null) { throw new
	 * InstapeException("Lender does not exist", StatusCode.ENTITY_NOT_FOUND); }
	 * lender.setDeleted(true); lender.setUpdatedBy(loggedInUser.toString());
	 * lender.setUpdatedDate(DateUtils.getCurrentTimestamp());
	 * lenderRepository.save(lender);
	 * 
	 * List<LenderProductMapping> lenderProductMappings =
	 * lender.getLenderProductMapping(); lenderProductMappings =
	 * lenderProductMappings.stream().filter(lpcm -> lpcm.isDeleted() == false)
	 * .collect(Collectors.toList()); for (LenderProductMapping lenderProductMapping
	 * : lenderProductMappings) { lenderProductMapping.setDeleted(true);
	 * lenderProductMapping.setUpdatedBy(loggedInUser);
	 * lenderProductMapping.setUpdatedDate(DateUtils.getCurrentTimestamp()); }
	 * lenderProductMappingRepository.saveAll(lenderProductMappings);
	 * 
	 * logger.info("Lender deleted successfully, Lender:{}", lender.getName()); }
	 * 
	 * @Override public boolean isExistWithSameCode(String lenderCode) {
	 * logger.info("checking lender exisitance"); return
	 * lenderRepository.existsByCode(lenderCode); }
	 */
	
	
}