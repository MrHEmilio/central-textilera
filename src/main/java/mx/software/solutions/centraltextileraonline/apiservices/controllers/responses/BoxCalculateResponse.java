package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoxCalculateResponse {

	private final String clothName;
	private final String colorName;
	private final List<BoxCalculateAmountResponse> boxes;

}
