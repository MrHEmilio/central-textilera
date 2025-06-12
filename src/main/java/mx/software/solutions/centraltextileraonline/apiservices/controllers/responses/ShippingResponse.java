package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;

@Data
public class ShippingResponse {
	private ShippingMethod shippingMethod;
	private String rateId;
	private String provider;
	private String serviceCode;
	private String serviceName;
	private BigDecimal price;
	private Date date;
	private String image;

}
