package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.List;
import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculateBoxRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxCalculateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface BoxService {

	BoxResponse createBox(BoxCreateRequest boxCreateRequest, UUID admin) throws DataBaseException, ExistException;

	BoxResponse updateBox(BoxUpdateRequest boxUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	BoxResponse reactivateBox(UUID box, UUID admin) throws DataBaseException, NotFoundException;

	BoxResponse deleteBox(UUID box, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<BoxResponse> getAllBox(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<BoxHistoryResponse> getBoxHistory(UUID box, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	List<BoxCalculateResponse> calculateBox(final CalculateBoxRequest calculateBoxRequest) throws DataBaseException, NotFoundException;

}
