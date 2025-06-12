package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface FiberService {

	FiberResponse createFiber(FiberCreateRequest fiberCreateRequest, UUID admin) throws DataBaseException, ExistException;

	FiberResponse updateFiber(FiberUpdateRequest fiberUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	FiberResponse reactivateFiber(UUID fiber, UUID admin) throws DataBaseException, NotFoundException;

	FiberResponse deleteFiber(UUID fiber, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<FiberResponse> getAllFiber(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<FiberHistoryResponse> getFiberHistory(UUID fiber, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
