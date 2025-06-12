package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculatePriceResponse {

	private final UUID product;
	private final BigDecimal amount;
	private final BigDecimal sellPrice;
	private final BigDecimal totalSellPrice;
	private final BigDecimal priceNormal;
	private final BigDecimal totalSellPriceNormal;
	private final BigDecimal discount;

}
