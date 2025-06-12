package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Collection", description = "Endpoints to manage the collection show in Central Textilera Ecommerce.")
public interface CollectionDocumentation {

	@Operation(summary = "Create collection", description = "Create collection of Central Textilera Ecommerce.")
	CrudResponse<CollectionResponse> createCollection(CollectionCreateRequest collectionCreateRequest) throws DataBaseException, ExistException, ImageInvalidException;

	@Operation(summary = "Update collection", description = "Update collection of Central Textilera Ecommerce.")
	CrudResponse<CollectionResponse> updateCollection(CollectionUpdateRequest collectionUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate collection", description = "Reactivate collection of Central Textilera Ecommerce.")
	CrudResponse<CollectionResponse> reactivateCollection(@PathVariable final UUID collection) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete collection", description = "Delete collection of Central Textilera Ecommerce.")
	CrudResponse<CollectionResponse> deleteCollection(UUID collection) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all collections", description = "Get all collections of Central Textilera Ecommerce.")
	GetResponse<CollectionResponse> getAllCollection(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get collection history", description = "Get collection history of Central Textilera Ecommerce.")
	GetResponse<CollectionHistoryResponse> getCollectionHistory(UUID collection, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
