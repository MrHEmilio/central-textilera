package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.AdminDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.AdminUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.AdminResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CredentialInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.EmailExistWithoutPasswordException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController implements AdminDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private AdminService adminService;

	@Override
	@PostMapping
	public CrudResponse<AdminResponse> createAdmin(@Valid @RequestBody final AdminCreateRequest adminCreateRequest) throws DataBaseException, ExistException, EmailExistWithoutPasswordException {
		final var adminResponse = this.adminService.createAdmin(adminCreateRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_ADMIN, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(adminResponse, messageResponse);
	}

	@Override
	@PutMapping("/{admin}")
	public CrudResponse<AdminResponse> updateAdmin(@Valid @RequestBody final AdminUpdateRequest adminUpdateRequest, @PathVariable final UUID admin) throws DataBaseException, NotFoundException {
		final var adminResponse = this.adminService.updateAdmin(adminUpdateRequest, admin);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_ADMIN, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(adminResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<AdminResponse> updateAdmin(@Valid @RequestBody final AdminUpdateRequest adminUpdateRequest) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var adminResponse = this.adminService.updateAdmin(adminUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_ADMIN, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(adminResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{admin}")
	public CrudResponse<AdminResponse> deleteAdmin(@PathVariable final UUID admin) throws DataBaseException, NotFoundException {
		final var adminResponse = this.adminService.deleteAdmin(admin);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_ADMIN, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(adminResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<AdminResponse> getAllAdmin(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.adminService.getAllAdmin(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/info")
	public CredentialInfoResponse<AdminResponse> getInfoAdmin() throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		return this.adminService.getInfoAdmin(sessionDto.getIdUser());
	}

	@Override
	@GetMapping("/info/{admin}")
	public CredentialInfoResponse<AdminResponse> getInfoAdmin(@PathVariable final UUID admin) throws DataBaseException, NotFoundException {
		return this.adminService.getInfoAdmin(admin);
	}
}
