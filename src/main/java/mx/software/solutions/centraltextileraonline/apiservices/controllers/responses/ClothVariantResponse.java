package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothVariantResponse {

	private final UUID id;
	private final ColorResponse color;
	private final BigDecimal amount;

}
