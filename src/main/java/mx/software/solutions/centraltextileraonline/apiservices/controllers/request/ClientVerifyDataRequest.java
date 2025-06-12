package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import lombok.Data;

@Data
public class ClientVerifyDataRequest {

	private UUID countryCode;
	private String phone;

	private String rfc;

}
