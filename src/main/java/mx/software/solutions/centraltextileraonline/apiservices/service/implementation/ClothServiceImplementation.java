package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothBillingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothMeasureRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothPriceRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothSamplerBillingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothSamplerRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothVariantRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothBillingEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothDescriptionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothMeasureEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothPriceEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothSamplerBillingEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothSamplerEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothVariantEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ClothResponseStructure;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClothHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.CollectionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ColorHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.FiberHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ImagesHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SaleHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.UseHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClothService;

@Slf4j
@Service
public class ClothServiceImplementation implements ClothService {

	@Autowired
	private ClothRepository clothRepository;

	@Autowired
	private ClothHistoryRepository clothHistoryRepository;

	@Autowired
	private ClothHelper clothHelper;

	@Autowired
	private CollectionHelper collectionHelper;

	@Autowired
	private ColorHelper colorHelper;

	@Autowired
	private FiberHelper fiberHelper;

	@Autowired
	private SaleHelper saleHelper;

	@Autowired
	private UseHelper useHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private ImagesHelper imagesHelper;

	@Value("${path.images.cloths}")
	private String pathImagesCloths;

	@Value("${central-textilera.name-url-pattner}")
	private String patternNameUrl;

	@Override
	public ClothResponse createCloth(final ClothCreateRequest clothCreateRequest, final UUID admin) throws DataBaseException, ExistException, ImageInvalidException {
		ClothServiceImplementation.log.info("Starting created the cloth {}.", clothCreateRequest.getName());
		final var nameUrl = this.convertNameUrl(clothCreateRequest.getName());
		this.validateClothNotExist(clothCreateRequest.getName(), nameUrl);
		this.imagesHelper.verifyImage(Controller.CATALOG_CLOTH, clothCreateRequest.getImage());
		try {
			final var clothSamplerEntity = this.convertClothSampler(clothCreateRequest.getSampler());
			final var clothMeasureEntity = this.convertClothMeasure(clothCreateRequest.getMeasure());
			final var clothBillingEntity = this.convertClothBilling(clothCreateRequest.getBilling());
			final List<ClothDescriptionEntity> listClothDescriptionEntities = new ArrayList<>();
			if (clothCreateRequest.getDescriptions() != null)
				clothCreateRequest.getDescriptions().stream().map(this::convertClothDescription).collect(Collectors.toList());
			final var fiberEntity = this.fiberHelper.getFiberEntity(clothCreateRequest.getFiber());
			final var saleEntity = this.saleHelper.getSaleEntity(clothCreateRequest.getSale());
			final var listClothVariantEntities = clothCreateRequest.getVariants().stream().map(clothVariantRequest -> {
				clothVariantRequest.setActive(true);
				return clothVariantRequest;
			}).map(this::convertClothVariant).collect(Collectors.toList());
			final var listCollectionEntities = clothCreateRequest.getCollections().stream().map(this::convertCollection).collect(Collectors.toList());
			final var listUseEntities = clothCreateRequest.getUses().stream().map(this::convertUse).collect(Collectors.toList());
			final var listClothPriceEntities = clothCreateRequest.getPrices().stream().map(this::convertClothPrice).collect(Collectors.toList());

			final var clothEntity = new ClothEntity();
			clothEntity.setName(clothCreateRequest.getName());
			clothEntity.setNameUrl(nameUrl);
			clothEntity.setMainDescription(clothCreateRequest.getMainDescription());
			clothEntity.setClothSamplerEntity(clothSamplerEntity);
			clothEntity.setClothMeasureEntity(clothMeasureEntity);
			clothEntity.setClothBillingEntity(clothBillingEntity);
			clothEntity.setClothDescriptionEntities(listClothDescriptionEntities);
			clothEntity.setFiberEntity(fiberEntity);
			clothEntity.setSaleEntity(saleEntity);
			clothEntity.setClothVariantEntities(listClothVariantEntities);
			clothEntity.setCollectionEntities(listCollectionEntities);
			clothEntity.setUseEntities(listUseEntities);
			clothEntity.setClothPriceEntities(listClothPriceEntities);
			clothEntity.setActive(true);

			clothSamplerEntity.setClothEntity(clothEntity);
			clothMeasureEntity.setClothEntity(clothEntity);
			clothBillingEntity.setClothEntity(clothEntity);

			final var newClothEntity = this.clothRepository.save(clothEntity);
			ClothServiceImplementation.log.info("Finished create the cloth {}.", clothCreateRequest.getName());

			ClothServiceImplementation.log.info("Starting saved the cloth image.");
			this.imagesHelper.saveImage(clothCreateRequest.getImage(), this.pathImagesCloths, newClothEntity.getId());
			ClothServiceImplementation.log.info("Finished save the cloth image.");

			ClothServiceImplementation.log.info("Starting created the create history for {}.", clothCreateRequest.getName());
			this.createClothHistory(newClothEntity, admin, DataBaseActionType.CREATE);
			ClothServiceImplementation.log.info("Finished create the create history for {}.", clothCreateRequest.getName());

			final var listClothResponseStructure = Arrays.asList(ClothResponseStructure.values());
			return this.clothHelper.convertCloth(newClothEntity, listClothResponseStructure);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The cloth {} could not been create.", clothCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.CREATE, clothCreateRequest.getName());
		}
	}

	@Override
	public ClothResponse updateCloth(final ClothUpdateRequest clothUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException, ImageInvalidException {
		ClothServiceImplementation.log.info("Starting created the cloth {}.", clothUpdateRequest.getName());
		if (clothUpdateRequest.getImage() != null && !clothUpdateRequest.getImage().isEmpty())
			this.imagesHelper.verifyImage(Controller.CATALOG_CLOTH, clothUpdateRequest.getImage());
		try {
			final var clothSamplerEntity = this.convertClothSampler(clothUpdateRequest.getSampler());
			final var clothMeasureEntity = this.convertClothMeasure(clothUpdateRequest.getMeasure());
			final var clothBillingEntity = this.convertClothBilling(clothUpdateRequest.getBilling());
			final List<ClothDescriptionEntity> listClothDescriptionEntities = new ArrayList<>();
			if (clothUpdateRequest.getDescriptions() != null)
				clothUpdateRequest.getDescriptions().stream().map(this::convertClothDescription).collect(Collectors.toList());
			final var fiberEntity = this.fiberHelper.getFiberEntity(clothUpdateRequest.getFiber());
			final var saleEntity = this.saleHelper.getSaleEntity(clothUpdateRequest.getSale());
			final var listClothVariantEntities = clothUpdateRequest.getVariants().stream().map(this::convertClothVariant).collect(Collectors.toList());
			final var listCollectionEntities = clothUpdateRequest.getCollections().stream().map(this::convertCollection).collect(Collectors.toList());
			final var listUseEntities = clothUpdateRequest.getUses().stream().map(this::convertUse).collect(Collectors.toList());
			final var listClothPriceEntities = clothUpdateRequest.getPrices().stream().map(this::convertClothPrice).collect(Collectors.toList());

			final var clothEntity = this.clothHelper.getClothEntity(clothUpdateRequest.getId());
			this.clearCLothEntity(clothEntity);
			clothEntity.setName(clothUpdateRequest.getName());
			clothEntity.setNameUrl(this.convertNameUrl(clothUpdateRequest.getName()));
			clothEntity.setMainDescription(clothUpdateRequest.getMainDescription());
			clothEntity.setClothSamplerEntity(clothSamplerEntity);
			clothEntity.setClothMeasureEntity(clothMeasureEntity);
			clothEntity.setClothBillingEntity(clothBillingEntity);
			clothEntity.setFiberEntity(fiberEntity);
			clothEntity.setSaleEntity(saleEntity);
			clothEntity.setCollectionEntities(listCollectionEntities);
			clothEntity.setUseEntities(listUseEntities);
			clothEntity.setActive(true);

			clothSamplerEntity.setClothEntity(clothEntity);
			clothMeasureEntity.setClothEntity(clothEntity);
			clothBillingEntity.setClothEntity(clothEntity);

			clothEntity.getClothVariantEntities().clear();
			clothEntity.getClothVariantEntities().addAll(listClothVariantEntities);
			clothEntity.getClothPriceEntities().clear();
			clothEntity.getClothPriceEntities().addAll(listClothPriceEntities);

			if (clothEntity.getClothDescriptionEntities() != null) {
				clothEntity.getClothDescriptionEntities().clear();
				clothEntity.getClothDescriptionEntities().addAll(listClothDescriptionEntities);
			}

			final var newClothEntity = this.clothRepository.save(clothEntity);
			ClothServiceImplementation.log.info("Finished update the cloth {}.", clothUpdateRequest.getName());

			if (clothUpdateRequest.getImage() != null && !clothUpdateRequest.getImage().isEmpty()) {
				ClothServiceImplementation.log.info("Starting saved the cloth image.");
				this.imagesHelper.saveImage(clothUpdateRequest.getImage(), this.pathImagesCloths, newClothEntity.getId());
				ClothServiceImplementation.log.info("Finished save the cloth image.");
			}

			ClothServiceImplementation.log.info("Starting created the update history for {}.", clothUpdateRequest.getName());
			this.createClothHistory(newClothEntity, admin, DataBaseActionType.UPDATE);
			ClothServiceImplementation.log.info("Finished create the update history for {}.", clothUpdateRequest.getName());

			final var listClothResponseStructure = Arrays.asList(ClothResponseStructure.values());
			return this.clothHelper.convertCloth(newClothEntity, listClothResponseStructure);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The cloth {} could not been update.", clothUpdateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.UPDATE, clothUpdateRequest.getName());
		}
	}

	@Override
	public ClothResponse reactivateCloth(final UUID cloth, final UUID admin) throws DataBaseException, NotFoundException {
		ClothServiceImplementation.log.info("Starting reactivated the cloth {}.", cloth);
		final var clothEntity = this.clothHelper.getClothEntity(cloth);
		clothEntity.setActive(true);
		try {
			final var newClothEntity = this.clothRepository.save(clothEntity);
			ClothServiceImplementation.log.info("Finished reactivate the cloth {}.", cloth);

			ClothServiceImplementation.log.info("Starting created the reactivate history for {}.", cloth);
			this.createClothHistory(newClothEntity, admin, DataBaseActionType.REACTIVATE);
			ClothServiceImplementation.log.info("Finished create the reactivate history for {}.", cloth);

			final var listClothResponseStructure = Arrays.asList(ClothResponseStructure.values());
			return this.clothHelper.convertCloth(newClothEntity, listClothResponseStructure);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The cloth {} could not been reactivate.", cloth, exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.REACTIVATE, cloth.toString());
		}
	}

	@Override
	public ClothResponse deleteCloth(final UUID cloth, final UUID admin) throws DataBaseException, NotFoundException {
		ClothServiceImplementation.log.info("Starting deleted the cloth with the id {}.", cloth);
		final var clothEntity = this.getClothEntity(cloth);
		clothEntity.setActive(false);
		try {
			final var newClothEntity = this.clothRepository.save(clothEntity);
			ClothServiceImplementation.log.info("Finished delete the cloth with the id {}.", cloth);

			ClothServiceImplementation.log.info("Starting created the delete history for id {}.", cloth);
			this.createClothHistory(newClothEntity, admin, DataBaseActionType.DELETE);
			ClothServiceImplementation.log.info("Finished create the delete history for id {}.", cloth);

			final var listClothResponseType = Arrays.asList(ClothResponseStructure.values());
			return this.clothHelper.convertCloth(newClothEntity, listClothResponseType);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The color with the id {} could not been delete.", cloth, exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.DELETE, cloth.toString());
		}
	}

	@Override
	public GetResponse<ClothResponse> getAllCloth(final ClothFilterRequest clothFilterRequest, final List<ClothResponseStructure> listClothResponseStructure, final PaginationRequest paginationRequest, final boolean random) throws DataBaseException, NotFoundException {
		ClothServiceImplementation.log.info("Starting searched of all cloths.");
		Page<ClothEntity> pageClothEntity = null;
		try {
			final var search = clothFilterRequest.getSearch();
			final var searchUrl = clothFilterRequest.getSearchUrl();
			String fibers = null;
			if (clothFilterRequest.getFibers() != null)
				fibers = clothFilterRequest.getFibers().stream().map(fiber -> "'" + fiber.toString() + "'").collect(Collectors.joining(","));
			String sales = null;
			if (clothFilterRequest.getSales() != null)
				sales = clothFilterRequest.getSales().stream().map(sale -> "'" + sale.toString() + "'").collect(Collectors.joining(","));
			String collections = null;
			if (clothFilterRequest.getCollections() != null)
				collections = clothFilterRequest.getCollections().stream().map(collection -> "'" + collection.toString() + "'").collect(Collectors.joining(","));
			String uses = null;
			if (clothFilterRequest.getUses() != null)
				uses = clothFilterRequest.getUses().stream().map(use -> "'" + use.toString() + "'").collect(Collectors.joining(","));
			var active = clothFilterRequest.getActive();
			if (!this.sessionHelper.isAdmin())
				active = true;
			var columnSort = paginationRequest.getColumnSort();
			String direction = null;
			if (paginationRequest.getTypeSort() != null)
				direction = paginationRequest.getTypeSort().name();
			paginationRequest.setColumnSort(null);
			paginationRequest.setTypeSort(null);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			if (random)
				columnSort = "random";
			pageClothEntity = this.clothRepository.findAll(search, searchUrl, fibers, sales, collections, uses, active, columnSort, direction, pageable);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The cloths could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.READ);
		}
		final var listClothResponse = pageClothEntity.get().map(clothEntity -> this.clothHelper.convertCloth(clothEntity, listClothResponseStructure)).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageClothEntity);
		if (listClothResponse.isEmpty()) {
			ClothServiceImplementation.log.error("The cloths not found.");
			throw new NotFoundException(Controller.CATALOG_CLOTH, "all");
		}
		ClothServiceImplementation.log.info("Finished search the cloths.");
		return new GetResponse<>(listClothResponse, paginationResponse);
	}

	@Override
	public GetResponse<ClothHistoryResponse> getClothHistory(final UUID cloth, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		ClothServiceImplementation.log.info("Starting searched history of cloth with the id {}.", cloth);
		final var clothEntity = this.clothHelper.getClothEntity(cloth);
		Page<ClothHistoryEntity> pageClothHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageClothHistoryEntity = this.clothHistoryRepository.findAllByClothEntity(clothEntity, pageable);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The history cloth could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.READ);
		}
		final var listClothHistoryResponse = pageClothHistoryEntity.get().map(this.clothHelper::convertClothHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageClothHistoryEntity);
		ClothServiceImplementation.log.info("Finished search history of cloth with the id {}.", cloth);
		return new GetResponse<>(listClothHistoryResponse, paginationResponse);
	}

	private ClothEntity getClothEntity(final UUID cloth) throws DataBaseException, NotFoundException {
		Optional<ClothEntity> optionalClothEntity = Optional.empty();
		try {
			ClothServiceImplementation.log.info("Starting searched the cloth with the id {}.", cloth);
			optionalClothEntity = this.clothRepository.findById(cloth);
		} catch (final Exception exception) {
			ClothServiceImplementation.log.error("The color with the id {} could not read.", cloth, exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.READ, cloth.toString());
		}

		if (optionalClothEntity.isEmpty()) {
			ClothServiceImplementation.log.error("The cloth not found with the id {}.", cloth);
			throw new NotFoundException(Controller.CATALOG_CLOTH, "id", cloth.toString());
		}
		ClothServiceImplementation.log.info("Finished search the color with the id {}.", cloth);
		return optionalClothEntity.get();
	}

	private ClothSamplerEntity convertClothSampler(final ClothSamplerRequest clothSamplerRequest) {
		final var clothSamplerBillingEntity = this.convertClothSamplerBilling(clothSamplerRequest.getBilling());
		final var clothSamplerEntity = new ClothSamplerEntity();
		clothSamplerEntity.setId(clothSamplerRequest.getId());
		clothSamplerEntity.setDescription(clothSamplerRequest.getDescription());
		clothSamplerEntity.setPrice(clothSamplerRequest.getPrice());
		clothSamplerEntity.setAmount(clothSamplerRequest.getAmount());
		clothSamplerEntity.setClothSamplerBillingEntity(clothSamplerBillingEntity);
		clothSamplerBillingEntity.setClothSamplerEntity(clothSamplerEntity);
		return clothSamplerEntity;
	}

	private ClothSamplerBillingEntity convertClothSamplerBilling(final ClothSamplerBillingRequest clothSamplerBillingRequest) {
		final var clothSamplerBillingEntity = new ClothSamplerBillingEntity();
		clothSamplerBillingEntity.setId(clothSamplerBillingRequest.getId());
		clothSamplerBillingEntity.setProductCode(clothSamplerBillingRequest.getProductCode());
		clothSamplerBillingEntity.setProductLabel(clothSamplerBillingRequest.getProductLabel());
		clothSamplerBillingEntity.setUnitCode(clothSamplerBillingRequest.getUnitCode());
		clothSamplerBillingEntity.setUnitLabel(clothSamplerBillingRequest.getUnitLabel());
		return clothSamplerBillingEntity;
	}

	private ClothMeasureEntity convertClothMeasure(final ClothMeasureRequest clothMeasuerRequest) {
		final var clothMeasureEntity = new ClothMeasureEntity();
		clothMeasureEntity.setId(clothMeasuerRequest.getId());
		clothMeasureEntity.setDimension(clothMeasuerRequest.getDimension());
		clothMeasureEntity.setAveragePerRoll(clothMeasuerRequest.getAveragePerRoll());
		clothMeasureEntity.setWidth(clothMeasuerRequest.getWidth());
		clothMeasureEntity.setWeight(clothMeasuerRequest.getWeight());
		clothMeasureEntity.setYieldPerKilo(clothMeasuerRequest.getYieldPerKilo());
		return clothMeasureEntity;
	}

	private ClothBillingEntity convertClothBilling(final ClothBillingRequest clothBillingRequest) {
		final var clothBillingEntity = new ClothBillingEntity();
		clothBillingEntity.setId(clothBillingRequest.getId());
		clothBillingEntity.setProductCode(clothBillingRequest.getProductCode());
		clothBillingEntity.setProductLabel(clothBillingRequest.getProductLabel());
		clothBillingEntity.setUnitCode(clothBillingRequest.getUnitCode());
		clothBillingEntity.setUnitLabel(clothBillingRequest.getUnitLabel());
		return clothBillingEntity;
	}

	private ClothDescriptionEntity convertClothDescription(final String description) {
		final var clothDescriptionEntity = new ClothDescriptionEntity();
		clothDescriptionEntity.setName(description);
		clothDescriptionEntity.setActive(true);
		return clothDescriptionEntity;
	}

	private ClothVariantEntity convertClothVariant(final ClothVariantRequest clothVariantRequest) {
		var colorEntity = new ColorEntity();
		try {
			colorEntity = this.colorHelper.getColorEntity(clothVariantRequest.getColor());
			final var clothVariantEntity = new ClothVariantEntity();
			clothVariantEntity.setId(clothVariantRequest.getId());
			clothVariantEntity.setColorEntity(colorEntity);
			clothVariantEntity.setAmount(clothVariantRequest.getAmount());
			return clothVariantEntity;
		} catch (DataBaseException | NotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ClothPriceEntity convertClothPrice(final ClothPriceRequest clothPriceRequest) {
		final var clothPriceEntity = new ClothPriceEntity();
		clothPriceEntity.setId(clothPriceRequest.getId());
		clothPriceEntity.setFirstAmountRange(clothPriceRequest.getFirstAmountRange());
		clothPriceEntity.setLastAmountRange(clothPriceRequest.getLastAmountRange());
		clothPriceEntity.setOrder(clothPriceRequest.getOrder());
		clothPriceEntity.setPrice(clothPriceRequest.getPrice());
		return clothPriceEntity;
	}

	private CollectionEntity convertCollection(final UUID collection) {
		try {
			return this.collectionHelper.getCollectionEntity(collection);
		} catch (DataBaseException | NotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private UseEntity convertUse(final UUID use) {
		try {
			return this.useHelper.getUseEntity(use);
		} catch (DataBaseException | NotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void validateClothNotExist(final String name, final String nameUrl) throws ExistException {
		ClothServiceImplementation.log.info("Starting validate the cloth if exist {} {}.", name, nameUrl);
		final var optionalClothEntity = this.clothRepository.findByNameIgnoreCaseOrNameUrlIgnoreCase(name, nameUrl);
		if (optionalClothEntity.isPresent()) {
			ClothServiceImplementation.log.error("The cloth {} exist.", name);
			throw new ExistException(Controller.CATALOG_CLOTH, "name", name);
		}
	}

	private void createClothHistory(final ClothEntity clothEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var clothHistoryEntity = new ClothHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var cloneClothEntity = new ClothEntity(clothEntity);
			final var object = objectMapper.writeValueAsString(cloneClothEntity);
			clothHistoryEntity.setClothEntity(clothEntity);
			clothHistoryEntity.setAdminEntity(adminEntity);
			clothHistoryEntity.setActionType(dataBaseActionType);
			clothHistoryEntity.setDate(new Date());
			clothHistoryEntity.setObject(object);
			this.clothHistoryRepository.save(clothHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

	private String convertNameUrl(final String name) {
		var nameFormat = StringUtils.lowerCase(StringUtils.stripAccents(name));
		nameFormat = nameFormat.replace(' ', '-');
		nameFormat = nameFormat.replaceAll(this.patternNameUrl, "");
		return nameFormat.replaceAll("[-]{2}", "-");
	}

	private void clearCLothEntity(final ClothEntity clothEntity) {
		clothEntity.getClothVariantEntities().clear();
		clothEntity.getClothPriceEntities().clear();
		this.clothRepository.save(clothEntity);
	}

}
