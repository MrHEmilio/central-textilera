package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CalculatePriceShippingRequest {

	@NotNull(message = "price.shipping.latitude.not.null")
	@NotBlank(message = "price.shipping.latitude.not.blank")
	private String latitude;

	@NotNull(message = "price.shipping.longitude.not.null")
	@NotBlank(message = "price.shipping.longitude.not.blank")
	private String longitude;

	@NotNull(message = "order.calculate.price.not.null")
	private List<PaymentClothVariantRequest> cloths;

	@NotNull(message = "order.calculate.price.samplers.not.null")
	private List<PaymentClothSamplerRequest> samplers;

	private ClientAddressCreateWithoutAccountRequest address;

	private String clientName;

	private String phone;

	private String email;

}