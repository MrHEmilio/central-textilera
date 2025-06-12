package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothResponseStructureRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Cloth", description = "Endpoints to manage the cloths show in Central Textilera Ecommerce.")
public interface ClothDocumentation {

	@Operation(summary = "Create cloth", description = "Create cloth of Central Textilera Ecommerce.")
	CrudResponse<ClothResponse> createCloth(ClothCreateRequest clothCreateRequest) throws DataBaseException, ExistException, ImageInvalidException;

	@Operation(summary = "Update cloth", description = "Update cloth of Central Textilera Ecommerce.")
	CrudResponse<ClothResponse> updateCloth(ClothUpdateRequest clothUpdateRequest) throws DataBaseException, NotFoundException, ExistException, ImageInvalidException;

	@Operation(summary = "Reactivate cloth", description = "Reactivate cloth of Central Textilera Ecommerce.")
	CrudResponse<ClothResponse> reactivateCloth(@PathVariable final UUID cloth) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete cloth", description = "Delete cloth of Central Textilera Ecommerce.")
	CrudResponse<ClothResponse> deleteCloth(UUID cloth) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all cloth", description = "Get all cloth of Central Textilera Ecommerce.")
	GetResponse<ClothResponse> getAllCloth(ClothFilterRequest filterClothRequest, ClothResponseStructureRequest clothResponseStruture, PaginationRequest paginationRequest, boolean random) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get cloth history", description = "Get cloth history of Central Textilera Ecommerce.")
	GetResponse<ClothHistoryResponse> getClothHistory(UUID cloth, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
