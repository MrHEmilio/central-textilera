package mx.software.solutions.centraltextileraonline.apiservices.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PriceDto {

	private BigDecimal sellPriceCloth = BigDecimal.ZERO;
	private BigDecimal sellPriceClothNormal = BigDecimal.ZERO;

}
