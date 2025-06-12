package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClothUpdateRequest {

	@NotNull(message = "cloth.id.not.null")
	private UUID id;

	@NotNull(message = "cloth.name.not.null")
	@NotBlank(message = "cloth.name.not.blank")
	private String name;

	private String image;

	@NotNull(message = "cloth.main.description.not.null")
	@NotBlank(message = "cloth.main.description.not.blank")
	private String mainDescription;

	@NotNull(message = "cloth.sampler.not.null")
	private ClothSamplerRequest sampler;

	@NotNull(message = "cloth.measure.not.null")
	private ClothMeasureRequest measure;

	@NotNull(message = "cloth.billing.not.null")
	private ClothBillingRequest billing;

	private List<String> descriptions;

	@NotNull(message = "cloth.fiber.not.null")
	private UUID fiber;

	@NotNull(message = "cloth.sale.not.null")
	private UUID sale;

	@NotNull(message = "cloth.variants.not.null")
	@NotEmpty(message = "cloth.variants.not.empty")
	private List<ClothVariantRequest> variants;

	private List<UUID> collections;

	private List<UUID> uses;

	@NotNull(message = "cloth.prices.not.null")
	@NotEmpty(message = "cloth.prices.not.empty")
	private List<ClothPriceRequest> prices;

}
