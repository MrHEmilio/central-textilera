package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothSamplerResponse {

	private final UUID id;
	private final String description;
	private final BigDecimal price;
	private final BigDecimal amount;
	private final ClothSamplerBillingResponse billing;

}
