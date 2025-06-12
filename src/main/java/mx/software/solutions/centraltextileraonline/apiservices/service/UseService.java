package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface UseService {

	UseResponse createUse(UseCreateRequest useCreateRequest, UUID admin) throws DataBaseException, ExistException;

	UseResponse updateUse(UseUpdateRequest useUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	UseResponse reactivateUse(UUID use, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	UseResponse deleteUse(UUID use, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<UseResponse> getAllUse(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<UseHistoryResponse> getUseHistory(UUID use, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
