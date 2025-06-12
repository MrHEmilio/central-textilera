package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CalculateDeliveryMethodRequest {

	@NotNull(message = "order.calculate.price.cloths.not.null")
	private List<PaymentClothVariantRequest> cloths;

}