package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Color", description = "Endpoints to manage the color show in Central Textilera Ecommerce.")
public interface ColorDocumentation {

	@Operation(summary = "Create color", description = "Create color of Central Textilera Ecommerce.")
	CrudResponse<ColorResponse> createColor(ColorCreateRequest colorCreateRequest) throws DataBaseException, ExistException;

	@Operation(summary = "Update color", description = "Update color of Central Textilera Ecommerce.")
	CrudResponse<ColorResponse> updateColor(ColorUpdateRequest colorUpdateRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Reactivate color", description = "Reactivate color of Central Textilera Ecommerce.")
	CrudResponse<ColorResponse> reactivateColor(@PathVariable final UUID color) throws DataBaseException, NotFoundException;

	@Operation(summary = "Delete color", description = "Delete color of Central Textilera Ecommerce.")
	CrudResponse<ColorResponse> deleteColor(UUID color) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all colors", description = "Get all colors of Central Textilera Ecommerce.")
	GetResponse<ColorResponse> getAllColor(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get color history", description = "Get color history of Central Textilera Ecommerce.")
	GetResponse<ColorHistoryResponse> getColorHistory(@PathVariable final UUID color, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
