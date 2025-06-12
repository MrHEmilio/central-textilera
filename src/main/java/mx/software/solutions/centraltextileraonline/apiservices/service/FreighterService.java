package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface FreighterService {

	FreighterResponse createFreighter(FreighterCreateRequest freighterCreateRequest, UUID admin) throws DataBaseException, ExistException, ImageInvalidException;

	FreighterResponse updateFreighter(FreighterUpdateRequest freighterUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	FreighterResponse reactivateFreighter(UUID freighter, UUID admin) throws DataBaseException, NotFoundException;

	FreighterResponse deleteFreighter(UUID freighter, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<FreighterResponse> getAllFreighter(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<FreighterHistoryResponse> getFreighterHistory(UUID freighter, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
