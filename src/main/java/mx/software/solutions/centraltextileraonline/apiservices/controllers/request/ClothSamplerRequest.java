package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class ClothSamplerRequest {

	private UUID id;

	@NotNull(message = "cloth.sampler.description.not.null")
	@NotBlank(message = "cloth.sampler.description.not.blank")
	private String description;

	@NotNull(message = "cloth.sampler.price.not.null")
	@Positive(message = "cloth.sampler.price.positive")
	private BigDecimal price;

	@NotNull(message = "cloth.sampler.amount.not.null")
	@Positive(message = "cloth.sampler.amount.positive")
	private BigDecimal amount;

	@Valid
	@NotNull(message = "cloth.billing.not.null")
	private ClothSamplerBillingRequest billing;

}
