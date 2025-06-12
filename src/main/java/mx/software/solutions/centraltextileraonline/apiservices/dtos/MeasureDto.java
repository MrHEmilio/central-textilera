package mx.software.solutions.centraltextileraonline.apiservices.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MeasureDto {

	private BigDecimal height = BigDecimal.ZERO;
	private BigDecimal length = BigDecimal.ZERO;
	private BigDecimal weight = BigDecimal.ZERO;
	private BigDecimal width = BigDecimal.ZERO;

}
