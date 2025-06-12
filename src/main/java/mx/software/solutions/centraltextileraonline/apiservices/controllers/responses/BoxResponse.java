package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoxResponse {

	@NonNull
	private final UUID id;
	@NonNull
	private final String name;
	@NonNull
	private final BigDecimal width;
	@NonNull
	private final BigDecimal height;
	@NonNull
	private final BigDecimal depth;
	@NonNull
	private final String colorCode;
	@NonNull
	private final Boolean active;

}
