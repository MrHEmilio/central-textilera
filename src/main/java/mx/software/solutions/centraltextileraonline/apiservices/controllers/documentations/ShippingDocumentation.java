package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceShippingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingTrackingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingTrackingCreateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;

@Tag(name = "Shipping", description = "Endpoints to manage the shipping realized in Central Textilera Ecommerce.")
public interface ShippingDocumentation {

	@Operation(summary = "Calculate Price Shipping", description = "Calculate price of shipping of cart of Central Textilera Ecommerce.")
	List<ShippingResponse> calculatePriceShipping(CalculatePriceShippingRequest calculatePriceShipping) throws DataBaseException, NotFoundException, RestTemplateException, ParseException;

	@Operation(summary = "Create Shipping", description = "Create shipping of cart of Central Textilera Ecommerce.")
	ShippingTrackingCreateResponse createShipping(ShippingTrackingCreateRequest shippingTrackingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException, JsonProcessingException;

}
