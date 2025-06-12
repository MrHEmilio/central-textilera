package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface ColorService {

	ColorResponse createColor(ColorCreateRequest colorCreateRequest, UUID admin) throws DataBaseException, ExistException;

	ColorResponse updateColor(ColorUpdateRequest colorUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	ColorResponse reactivateColor(UUID color, UUID admin) throws DataBaseException, NotFoundException;

	ColorResponse deleteColor(UUID color, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<ColorResponse> getAllColor(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<ColorHistoryResponse> getColorHistory(UUID color, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
