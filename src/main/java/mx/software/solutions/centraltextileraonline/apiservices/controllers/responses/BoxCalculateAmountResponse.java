package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BoxCalculateAmountResponse {

	@Setter
	private int amount;
	private final BoxResponse boxes;

}
