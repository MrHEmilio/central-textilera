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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.FiberDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.FiberService;

@RestController
@RequestMapping("/fiber")
public class FiberController implements FiberDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private FiberService fiberService;

	@Override
	@PostMapping
	public CrudResponse<FiberResponse> createFiber(@Valid @RequestBody final FiberCreateRequest fiberCreateRequest) throws DataBaseException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var fiberResponse = this.fiberService.createFiber(fiberCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FIBER, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(fiberResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<FiberResponse> updateFiber(@Valid @RequestBody final FiberUpdateRequest fiberUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var fiberResponse = this.fiberService.updateFiber(fiberUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FIBER, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(fiberResponse, messageResponse);
	}

	@Override
	@PutMapping("/{fiber}")
	public CrudResponse<FiberResponse> reactivateFiber(@PathVariable final UUID fiber) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var fiberResponse = this.fiberService.reactivateFiber(fiber, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FIBER, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(fiberResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{fiber}")
	public CrudResponse<FiberResponse> deleteFiber(@PathVariable final UUID fiber) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var fiberResponse = this.fiberService.deleteFiber(fiber, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_FIBER, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(fiberResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<FiberResponse> getAllFiber(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.fiberService.getAllFiber(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{fiber}/history")
	public GetResponse<FiberHistoryResponse> getFiberHistory(@PathVariable final UUID fiber, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.fiberService.getFiberHistory(fiber, paginationRequest);
	}

}
