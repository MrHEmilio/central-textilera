package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class ClothMeasureRequest {

	private UUID id;

	@NotNull(message = "cloth.measure.dimension.not.null")
	@Positive(message = "cloth.measure.dimension.positive")
	private BigDecimal dimension;

	@NotNull(message = "cloth.measure.average.per.roll.not.null")
	@Positive(message = "cloth.measure.average.per.roll.positive")
	private BigDecimal averagePerRoll;

	@NotNull(message = "cloth.measure.width.not.null")
	@Positive(message = "cloth.measure.width.positive")
	private BigDecimal width;

	@NotNull(message = "cloth.measure.weight.not.null")
	@Positive(message = "cloth.measure.weight.positive")
	private BigDecimal weight;

	private BigDecimal yieldPerKilo;

}
