package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;

@Data
public class OrderCreateRequest {

	@NotNull(message = "order.cloths.not.null")
	private List<PaymentClothVariantRequest> cloths;

	@NotNull(message = "order.samplers.not.null")
	private List<PaymentClothSamplerRequest> samplers;

	@Valid
	private ShippingCreateRequest shipping;

	@NotNull(message = "order.delivery.method.not.null")
	private DeliveryMethod deliveryMethod;

	@NotNull(message = "order.payment.method.not.null")
	private PaymentMethod paymentMethod;

	@NotNull(message = "order.payment.id.not.null")
	private String paymentId;

	private UUID billingAddress;

}
