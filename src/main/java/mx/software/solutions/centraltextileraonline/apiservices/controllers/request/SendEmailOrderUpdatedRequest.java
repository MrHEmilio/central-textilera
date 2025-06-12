package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import lombok.Data;

@Data
public class SendEmailOrderUpdatedRequest {

	private UUID id;

	private String email;

	private String clientName;

	private String numberOrder;

	private String statusOrder;

	private String trackingCode;

	private String trackingUrl;

}
