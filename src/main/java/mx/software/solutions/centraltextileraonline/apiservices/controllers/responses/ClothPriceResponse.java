package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothPriceResponse {

	private final UUID id;
	private final int firstAmountRange;
	private final Integer lastAmountRange;
	private final BigDecimal price;

}
