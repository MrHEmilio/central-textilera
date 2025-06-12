package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothSamplerBillingResponse {

	private final UUID id;
	private final String productCode;
	private final String productLabel;
	private final String unitCode;
	private final String unitLabel;

}
