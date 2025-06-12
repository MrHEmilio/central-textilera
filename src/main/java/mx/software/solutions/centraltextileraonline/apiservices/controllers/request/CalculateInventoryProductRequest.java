package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CalculateInventoryProductRequest {

	@NotNull(message = "order.calculate.inventory.cloths.not.null")
	private List<PaymentClothVariantRequest> cloths;

	@NotNull(message = "order.calculate.inventory.samplers.not.null")
	private List<PaymentClothSamplerRequest> samplers;

}