package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShippingTrackingCreateResponse {

	@NonNull
	private final String message;

}
