package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothBillingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothDescriptionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothMeasureResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothPriceResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothSamplerBillingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothSamplerResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothVariantResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothBillingEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothMeasureEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothPriceEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothSamplerBillingEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothSamplerEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothVariantEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ClothResponseStructure;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothRepository;

@Slf4j
@Component
public class ClothHelper {

	@Autowired
	private ClothRepository clothRepository;

	@Autowired
	private ImagesHelper imagesHelper;

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
	private AdminHelper adminHelper;

	@Value("${url.images.cloths}")
	private String urlImagesCloths;

	@Value("${central-textilera.cloth-description.width}")
	private String clothDescriptionWidth;

	@Value("${central-textilera.cloth-description.height}")
	private String clothDescriptionHeight;

	@Value("${central-textilera.cloth-description.yield-per-kilo}")
	private String clothDescriptionYieldPerKilo;

	@Value("${central-textilera.cloth-description.fiber}")
	private String clothDescriptionFiber;

	@Value("${central-textilera.cloth-description.sale}")
	private String clothDescriptionSale;

	@Value("${central-textilera.cloth-description.average-per-roll}")
	private String clothDescriptionAveragePerKilo;

	public ClothEntity getClothEntity(final UUID cloth) throws DataBaseException, NotFoundException {
		Optional<ClothEntity> optionalClothEntity = Optional.empty();
		try {
			ClothHelper.log.info("Starting searched the cloth with the id {}.", cloth);
			optionalClothEntity = this.clothRepository.findById(cloth);
		} catch (final Exception exception) {
			ClothHelper.log.error("The cloth with the id {} could not read.", cloth, exception);
			throw new DataBaseException(Controller.CATALOG_CLOTH, DataBaseActionType.READ, cloth.toString());
		}

		if (optionalClothEntity.isEmpty()) {
			ClothHelper.log.error("The use not found with the id {}.", cloth);
			throw new NotFoundException(Controller.CATALOG_CLOTH, "id", cloth.toString());
		}
		ClothHelper.log.info("Finished search the cloth with the id {}.", cloth);
		return optionalClothEntity.get();
	}

	public ClothResponse convertCloth(final ClothEntity clothEntity, List<ClothResponseStructure> listClothResponseStructure) {
		if (listClothResponseStructure == null)
			listClothResponseStructure = new ArrayList<>();

		if (listClothResponseStructure.contains(ClothResponseStructure.ALL))
			listClothResponseStructure = Arrays.asList(ClothResponseStructure.values());

		final var image = this.imagesHelper.getUrlImage(this.urlImagesCloths, clothEntity.getId());
		ClothSamplerResponse clothSamplerResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.SAMPLER))
			clothSamplerResponse = this.convertClothSampler(clothEntity.getClothSamplerEntity());
		ClothMeasureResponse clothMeasureResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.MEASURE))
			clothMeasureResponse = this.convertClothMeasure(clothEntity.getClothMeasureEntity());
		ClothBillingResponse clothBillingResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.BILLING))
			clothBillingResponse = this.convertClothBilling(clothEntity.getClothBillingEntity());
		List<ClothDescriptionResponse> listClothDescriptionResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.DESCRIPTION))
			listClothDescriptionResponse = this.convertClothDescription(clothEntity);
		FiberResponse fiberResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.FIBER))
			fiberResponse = this.fiberHelper.convertFiber(clothEntity.getFiberEntity());
		SaleResponse saleResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.SALE))
			saleResponse = this.saleHelper.convertSale(clothEntity.getSaleEntity());
		List<ClothVariantResponse> listClothVariantResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.VARIANTS))
			listClothVariantResponse = clothEntity.getClothVariantEntities().stream().map(this::convertClothVariant).collect(Collectors.toList());
		List<CollectionResponse> listCollectionsResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.COLLECTIONS))
			listCollectionsResponse = clothEntity.getCollectionEntities().stream().map(this.collectionHelper::convertCollection).collect(Collectors.toList());
		List<UseResponse> listUsesResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.USES))
			listUsesResponse = clothEntity.getUseEntities().stream().map(this.useHelper::convertUse).collect(Collectors.toList());
		List<ClothPriceResponse> listClothPricesResponse = null;
		if (listClothResponseStructure.contains(ClothResponseStructure.PRICES))
			listClothPricesResponse = clothEntity.getClothPriceEntities().stream().map(this::convertClothPrice).collect(Collectors.toList());

		final var clothResponse = new ClothResponse();
		clothResponse.setId(clothEntity.getId());
		clothResponse.setImage(image);
		clothResponse.setName(clothEntity.getName());
		clothResponse.setNameUrl(clothEntity.getNameUrl());
		clothResponse.setSampler(clothSamplerResponse);
		clothResponse.setMeasure(clothMeasureResponse);
		clothResponse.setBilling(clothBillingResponse);
		clothResponse.setMainDescription(clothEntity.getMainDescription());
		clothResponse.setDescriptions(listClothDescriptionResponse);
		clothResponse.setFiber(fiberResponse);
		clothResponse.setSale(saleResponse);
		clothResponse.setVariants(listClothVariantResponse);
		clothResponse.setCollections(listCollectionsResponse);
		clothResponse.setUses(listUsesResponse);
		clothResponse.setPrices(listClothPricesResponse);
		clothResponse.setActive(clothEntity.isActive());
		return clothResponse;
	}

	public ClothSamplerResponse convertClothSampler(final ClothSamplerEntity clothSamplerEntity) {
		final var id = clothSamplerEntity.getId();
		final var description = clothSamplerEntity.getDescription();
		final var price = clothSamplerEntity.getPrice();
		final var amount = clothSamplerEntity.getAmount();
		final var clothSamplerBilling = this.convertClothSamplerBilling(clothSamplerEntity.getClothSamplerBillingEntity());
		return new ClothSamplerResponse(id, description, price, amount, clothSamplerBilling);
	}

	private ClothSamplerBillingResponse convertClothSamplerBilling(final ClothSamplerBillingEntity clothSamplerBillingEntity) {
		final var id = clothSamplerBillingEntity.getId();
		final var productCode = clothSamplerBillingEntity.getProductCode();
		final var productLabel = clothSamplerBillingEntity.getProductLabel();
		final var unitCode = clothSamplerBillingEntity.getUnitCode();
		final var unitLabel = clothSamplerBillingEntity.getUnitLabel();
		return new ClothSamplerBillingResponse(id, productCode, productLabel, unitCode, unitLabel);
	}

	private ClothMeasureResponse convertClothMeasure(final ClothMeasureEntity clothMeasureEntity) {
		final var id = clothMeasureEntity.getId();
		final var dimension = clothMeasureEntity.getDimension();
		final var averagePerRoll = clothMeasureEntity.getAveragePerRoll();
		final var width = clothMeasureEntity.getWidth();
		final var weight = clothMeasureEntity.getWeight();
		final var yieldPerKilo = clothMeasureEntity.getYieldPerKilo();
		return new ClothMeasureResponse(id, dimension, averagePerRoll, width, weight, yieldPerKilo);
	}

	private ClothBillingResponse convertClothBilling(final ClothBillingEntity clothBillingEntity) {
		final var id = clothBillingEntity.getId();
		final var productCode = clothBillingEntity.getProductCode();
		final var productLabel = clothBillingEntity.getProductLabel();
		final var unitCode = clothBillingEntity.getUnitCode();
		final var unitLabel = clothBillingEntity.getUnitLabel();
		return new ClothBillingResponse(id, productCode, productLabel, unitCode, unitLabel);
	}

	private List<ClothDescriptionResponse> convertClothDescription(final ClothEntity clothEntity) {
		final var clothDescriptionResponse = clothEntity.getClothDescriptionEntities().stream().map(clothDescriptionEntity -> new ClothDescriptionResponse(clothDescriptionEntity.getName(), false)).collect(Collectors.toList());

		final var clothDescriptionWidth = String.format(this.clothDescriptionWidth, clothEntity.getClothMeasureEntity().getWidth().divide(new BigDecimal(100)));
		final var clothDescriptionHeight = String.format(this.clothDescriptionHeight, clothEntity.getClothMeasureEntity().getWeight());
		final var clothDescriptionYieldPerKilo = String.format(this.clothDescriptionYieldPerKilo, clothEntity.getClothMeasureEntity().getYieldPerKilo());
		final var clothDescriptionFiber = String.format(this.clothDescriptionFiber, clothEntity.getFiberEntity().getName());
		final var clothDescriptionSale = String.format(this.clothDescriptionSale, clothEntity.getSaleEntity().getName());
		final var clothDescriptionAveragePerKilo = String.format(this.clothDescriptionAveragePerKilo, clothEntity.getClothMeasureEntity().getAveragePerRoll(), clothEntity.getSaleEntity().getAbbreviation());

		clothDescriptionResponse.add(new ClothDescriptionResponse(clothDescriptionWidth, true));
		clothDescriptionResponse.add(new ClothDescriptionResponse(clothDescriptionHeight, true));
		if (clothEntity.getClothMeasureEntity().getYieldPerKilo() != null)
			clothDescriptionResponse.add(new ClothDescriptionResponse(clothDescriptionYieldPerKilo, true));
		clothDescriptionResponse.add(new ClothDescriptionResponse(clothDescriptionFiber, true));
		clothDescriptionResponse.add(new ClothDescriptionResponse(clothDescriptionSale, true));
		clothDescriptionResponse.add(new ClothDescriptionResponse(clothDescriptionAveragePerKilo, true));

		return clothDescriptionResponse;
	}

	private ClothVariantResponse convertClothVariant(final ClothVariantEntity clothVariantEntity) {
		final var id = clothVariantEntity.getId();
		final var colorResponse = this.colorHelper.convertColor(clothVariantEntity.getColorEntity());
		final var amount = clothVariantEntity.getAmount();
		return new ClothVariantResponse(id, colorResponse, amount);
	}

	private ClothPriceResponse convertClothPrice(final ClothPriceEntity clothPriceEntity) {
		final var id = clothPriceEntity.getId();
		final var firstAmountRange = clothPriceEntity.getFirstAmountRange();
		final var lastAmountRange = clothPriceEntity.getLastAmountRange();
		final var price = clothPriceEntity.getPrice();
		return new ClothPriceResponse(id, firstAmountRange, lastAmountRange, price);
	}

	public ClothHistoryResponse convertClothHistory(final ClothHistoryEntity clothHistoryEntity) {
		final var listClothResponseStructure = Arrays.asList(ClothResponseStructure.values());
		final var id = clothHistoryEntity.getId();
		final var clothResponse = this.convertCloth(clothHistoryEntity.getClothEntity(), listClothResponseStructure);
		final var adminResponse = this.adminHelper.convertAdmin(clothHistoryEntity.getAdminEntity());
		final var actionType = clothHistoryEntity.getActionType();
		final var date = clothHistoryEntity.getDate();
		final var object = clothHistoryEntity.getObject();
		return new ClothHistoryResponse(id, clothResponse, adminResponse, actionType, date, object);
	}

}
