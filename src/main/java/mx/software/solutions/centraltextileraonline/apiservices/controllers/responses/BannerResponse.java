package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BannerResponse {

	@NonNull
	private final UUID id;
	@NonNull
	private final String image;
	@NonNull
	private final String description;

	private final int waitTime;
	@NonNull
	private final Boolean active;

}
