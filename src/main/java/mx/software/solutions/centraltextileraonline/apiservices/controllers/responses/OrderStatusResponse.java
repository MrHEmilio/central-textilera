package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderStatusResponse {

	private UUID id;
	private String status;
	private Date date;

}
