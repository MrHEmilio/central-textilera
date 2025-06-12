package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DirectionRequest {

	@JsonProperty("full_name")
	private String fullName;
	private String company;
	@JsonProperty("direction_1")
	private String direction1;
	@JsonProperty("direction_2")
	private String direction2;
	private String latitude;
	private String longitude;
	@JsonProperty("postal_code")
	private String postalCode;
	private String district;
	private String city;
	@JsonProperty("country_code")
	private String countryCode;
	private String phone;
	private String email;
}
