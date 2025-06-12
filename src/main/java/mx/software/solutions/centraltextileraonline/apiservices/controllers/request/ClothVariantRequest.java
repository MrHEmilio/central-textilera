package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class ClothVariantRequest {

	private UUID id;

	@NotNull(message = "cloth.variant.color.not.null")
	private UUID color;

	@NotNull(message = "cloth.variant.amount.not.null")
	@Positive(message = "cloth.variant.amount.positive")
	private BigDecimal amount;

	private boolean active;

}
