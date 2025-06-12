package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;

@Getter
@RequiredArgsConstructor
public class BannerHistoryResponse {

	@NonNull
	private final UUID id;
	@NonNull
	private final BannerResponse banner;
	@NonNull
	private final AdminResponse admin;
	@NonNull
	private final DataBaseActionType actionType;
	@NonNull
	private final Date date;
	@NonNull
	private final String object;

}
