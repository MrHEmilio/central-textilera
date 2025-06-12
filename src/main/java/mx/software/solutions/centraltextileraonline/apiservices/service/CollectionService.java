package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface CollectionService {

	CollectionResponse createCollection(CollectionCreateRequest collectionCreateRequest, UUID admin) throws DataBaseException, ExistException, ImageInvalidException;

	CollectionResponse updateCollection(CollectionUpdateRequest collectionUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException;

	CollectionResponse reactivateCollection(UUID collection, UUID admin) throws DataBaseException, NotFoundException;

	CollectionResponse deleteCollection(UUID collection, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<CollectionResponse> getAllCollection(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<CollectionHistoryResponse> getCollectionHistory(UUID collection, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
