package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;

@Getter
@AllArgsConstructor
public class OrderShippingResponse {

	private final UUID id;
	private final String streetName;
	private final String numExt;
	private final String numInt;
	private final String zipCode;
	private final String suburb;
	private final String municipality;
	private final String state;
	private final String city;
	private final String country;
	private final String references;
	private final String provider;
	private final String serviceCode;
	private final String serviceName;
	private final BigDecimal price;
	private final Date date;
	private final ShippingMethod shippingMethod;
	private final String trackingNumber;
	private final String trackingUrlProvider;

}
