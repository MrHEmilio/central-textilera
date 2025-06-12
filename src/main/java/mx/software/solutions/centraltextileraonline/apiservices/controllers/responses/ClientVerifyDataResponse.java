package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.List;

import lombok.Data;

@Data
public class ClientVerifyDataResponse {

	private boolean ok;
	private List<String> dataWrong;

}
