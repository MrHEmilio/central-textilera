package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;

@Data
public class ShippingCreateWithoutAccountRequest {

	@Valid
	private ClientAddressCreateWithoutAccountRequest clientAddress;

	@NotNull(message = "shipping.shipping.method.not.null")
	private ShippingMethod shippingMethod;

	@NotNull(message = "shipping.rate.id.not.null")
	private String rateId;

	@NotNull(message = "shipping.provider.not.null")
	@NotBlank(message = "shipping.provider.not.blank")
	private String provider;

	@NotNull(message = "shipping.service.code.not.null")
	@NotBlank(message = "shipping.service.code.not.blank")
	private String serviceCode;

	@NotNull(message = "shipping.service.name.not.null")
	@NotBlank(message = "shipping.service.name.not.blank")
	private String serviceName;

	@NotNull(message = "shipping.price.not.null")
	@Positive(message = "shipping.price.positive")
	private BigDecimal price;

	@NotNull(message = "shipping.date.not.null")
	private Date date;

}
