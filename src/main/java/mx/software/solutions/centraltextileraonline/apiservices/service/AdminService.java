package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.AdminResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.EmailExistWithoutPasswordException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface AdminService {

	AdminResponse createAdmin(AdminCreateRequest adminCreateRequest) throws DataBaseException, ExistException, EmailExistWithoutPasswordException;

	AdminResponse updateAdmin(AdminUpdateRequest adminUpdateRequest, UUID admin) throws DataBaseException, NotFoundException;

	AdminResponse deleteAdmin(UUID admin) throws DataBaseException, NotFoundException;

	GetResponse<AdminResponse> getAllAdmin(FilterRequest filterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	CredentialInfoResponse<AdminResponse> getInfoAdmin(UUID admin) throws DataBaseException, NotFoundException;
}
