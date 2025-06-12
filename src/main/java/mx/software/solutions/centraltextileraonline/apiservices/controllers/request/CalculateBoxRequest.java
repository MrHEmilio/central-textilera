package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CalculateBoxRequest {

	@NotNull(message = "box.calculate.box.order.not.null")
	private UUID order;

}