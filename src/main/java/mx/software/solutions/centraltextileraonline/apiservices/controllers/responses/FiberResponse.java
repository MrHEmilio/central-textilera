package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FiberResponse {

	@NonNull
	private final UUID id;
	@NonNull
	private final String name;
	@NonNull
	private final Boolean active;

}
