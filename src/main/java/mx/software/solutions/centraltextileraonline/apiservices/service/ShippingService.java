package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceShippingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingTrackingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;

public interface ShippingService {

	List<ShippingResponse> calculatePriceShipping(CalculatePriceShippingRequest calculatePriceShippingRequest, List<ShippingResponse> listShippingResponse) throws RestTemplateException, ParseException;
	void createShipping(ShippingTrackingCreateRequest shippingTrackingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException, JsonProcessingException;
}
