package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothMeasureResponse {

	private final UUID id;
	private final BigDecimal dimension;
	private final BigDecimal averagePerRoll;
	private final BigDecimal width;
	private final BigDecimal weight;
	private final BigDecimal yieldPerKilo;

}
