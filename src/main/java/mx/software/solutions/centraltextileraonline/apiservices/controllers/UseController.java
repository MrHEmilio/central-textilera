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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.UseDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.UseService;

@RestController
@RequestMapping("/use")
public class UseController implements UseDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private UseService useService;

	@Override
	@PostMapping
	public CrudResponse<UseResponse> createUse(@Valid @RequestBody final UseCreateRequest useCreateRequest) throws DataBaseException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var useResponse = this.useService.createUse(useCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_USE, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(useResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<UseResponse> updateUse(@Valid @RequestBody final UseUpdateRequest useUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var useResponse = this.useService.updateUse(useUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_USE, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(useResponse, messageResponse);
	}

	@Override
	@PutMapping("/{use}")
	public CrudResponse<UseResponse> reactivateUse(@PathVariable final UUID use) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var useResponse = this.useService.reactivateUse(use, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_USE, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(useResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{use}")
	public CrudResponse<UseResponse> deleteUse(@PathVariable final UUID use) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var useResponse = this.useService.deleteUse(use, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_USE, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(useResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<UseResponse> getAllUse(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.useService.getAllUse(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{use}/history")
	public GetResponse<UseHistoryResponse> getUseHistory(@PathVariable final UUID use, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.useService.getUseHistory(use, paginationRequest);
	}

}
