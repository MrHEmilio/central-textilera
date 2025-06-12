package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ClothFilterRequest {

	private String search;
	private String searchUrl;
	private List<UUID> fibers;
	private List<UUID> sales;
	private List<UUID> collections;
	private List<UUID> uses;
	private Boolean active;

}
