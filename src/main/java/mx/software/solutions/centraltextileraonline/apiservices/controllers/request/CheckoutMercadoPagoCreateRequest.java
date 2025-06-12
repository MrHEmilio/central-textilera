package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CheckoutMercadoPagoCreateRequest {

	@Valid
	private ClientCreateWithoutAccountRequest client;

	@Valid
	private ClientAddressCreateWithoutAccountRequest clientAddress;

	private BigDecimal shippingPrice;

	@NotNull(message = "checkout.mercado.pago.cloths.not.null")
	private List<PaymentClothVariantRequest> cloths;

	@NotNull(message = "checkout.mercado.pago.samplers.not.null")
	private List<PaymentClothSamplerRequest> samplers;

}