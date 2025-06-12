package mx.software.solutions.centraltextileraonline.apiservices.service;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountryCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface CountryCodeService {

	GetResponse<CountryCodeResponse> getAllCountryCode(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
