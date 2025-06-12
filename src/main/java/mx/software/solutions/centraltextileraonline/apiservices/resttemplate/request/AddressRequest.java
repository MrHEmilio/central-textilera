package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import lombok.Data;

@Data
public class AddressRequest {

	private String province;
	private String city;
	private String name;
	private String zip;
	private String country;
	private String address1;
	private String company;
	private String address2;
	private String phone;
	private String email;
	private String reference;
	private String contents;

}
