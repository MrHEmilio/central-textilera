package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ClothResponse {

	private UUID id;
	private String image;
	private String name;
	private String nameUrl;
	private String mainDescription;
	private ClothSamplerResponse sampler;
	private ClothMeasureResponse measure;
	private ClothBillingResponse billing;
	private List<ClothDescriptionResponse> descriptions;
	private FiberResponse fiber;
	private SaleResponse sale;
	private List<ClothVariantResponse> variants;
	private List<CollectionResponse> collections;
	private List<UseResponse> uses;
	private List<ClothPriceResponse> prices;
	private Boolean active;

}
