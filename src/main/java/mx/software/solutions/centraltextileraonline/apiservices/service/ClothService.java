package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.List;
import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ClothResponseStructure;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface ClothService {

	ClothResponse createCloth(ClothCreateRequest clothCreateRequest, UUID admin) throws DataBaseException, ExistException, ImageInvalidException;

	ClothResponse updateCloth(ClothUpdateRequest clothUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, ExistException, ImageInvalidException;

	ClothResponse reactivateCloth(UUID cloth, UUID admin) throws DataBaseException, NotFoundException;

	ClothResponse deleteCloth(UUID cloth, UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<ClothResponse> getAllCloth(ClothFilterRequest clothFilterRequest, List<ClothResponseStructure> listClothResponseStructure, PaginationRequest paginationRequest, boolean random) throws DataBaseException, NotFoundException;

	GetResponse<ClothHistoryResponse> getClothHistory(UUID cloth, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

}
