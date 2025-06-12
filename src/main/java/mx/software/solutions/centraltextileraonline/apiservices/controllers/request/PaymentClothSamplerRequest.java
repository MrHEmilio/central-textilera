package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class PaymentClothSamplerRequest {

	@NotNull(message = "cloth.id.not.null")
	private UUID sampler;

	@NotNull(message = "order.calculate.price.amount.not.null")
	@Positive(message = "order.calculate.price.amount.positive")
	private BigDecimal amount;

}
