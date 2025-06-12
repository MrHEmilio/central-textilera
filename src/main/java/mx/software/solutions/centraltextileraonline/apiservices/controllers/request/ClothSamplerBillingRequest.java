package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClothSamplerBillingRequest {

	private UUID id;

	@NotNull(message = "cloth.sampler.billing.product.code.not.null")
	@NotBlank(message = "cloth.sampler.billing.product.code.not.blank")
	private String productCode;

	@NotNull(message = "cloth.sampler.billing.product.label.not.null")
	@NotBlank(message = "cloth.sampler.billing.product.label.not.blank")
	private String productLabel;

	@NotNull(message = "cloth.sampler.billing.unit.code.not.null")
	@NotBlank(message = "cloth.sampler.billing.unit.code.not.blank")
	private String unitCode;

	@NotNull(message = "cloth.sampler.billing.unit.label.not.null")
	@NotBlank(message = "cloth.sampler.billing.unit.label.not.blank")
	private String unitLabel;

}
