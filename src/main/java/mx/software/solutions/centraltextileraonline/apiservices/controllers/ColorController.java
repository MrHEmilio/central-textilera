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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.ColorDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.ColorService;

@RestController
@RequestMapping("/color")
public class ColorController implements ColorDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private ColorService colorService;

	@Override
	@PostMapping
	public CrudResponse<ColorResponse> createColor(@Valid @RequestBody final ColorCreateRequest colorCreateRequest) throws DataBaseException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var colorResponse = this.colorService.createColor(colorCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLOR, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(colorResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<ColorResponse> updateColor(@Valid @RequestBody final ColorUpdateRequest colorUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var colorResponse = this.colorService.updateColor(colorUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLOR, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(colorResponse, messageResponse);
	}

	@Override
	@PutMapping("/{color}")
	public CrudResponse<ColorResponse> reactivateColor(@PathVariable final UUID color) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var colorResponse = this.colorService.reactivateColor(color, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLOR, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(colorResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{color}")
	public CrudResponse<ColorResponse> deleteColor(@PathVariable final UUID color) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var colorResponse = this.colorService.deleteColor(color, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_COLOR, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(colorResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<ColorResponse> getAllColor(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.colorService.getAllColor(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{color}/history")
	public GetResponse<ColorHistoryResponse> getColorHistory(@PathVariable final UUID color, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.colorService.getColorHistory(color, paginationRequest);
	}

}
