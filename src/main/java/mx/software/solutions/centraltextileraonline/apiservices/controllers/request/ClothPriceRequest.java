package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class ClothPriceRequest {

	private UUID id;

	@NotNull(message = "cloth.price.first.amount.range.not.null")
	@Positive(message = "cloth.price.first.amount.range.positive")
	private int firstAmountRange;

	private Integer lastAmountRange;

	@NotNull(message = "cloth.price.order.not.null")
	@Positive(message = "cloth.price.order.positive")
	private int order;

	@NotNull(message = "cloth.price.not.null")
	@Positive(message = "cloth.price.positive")
	private BigDecimal price;

	private boolean active;

}
