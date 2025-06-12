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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.ClothDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothResponseStructureRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClothUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClothResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClothService;

@RestController
@RequestMapping("/cloth")
public class ClothController implements ClothDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private ClothService clothService;

	@Override
	@PostMapping
	public CrudResponse<ClothResponse> createCloth(@Valid @RequestBody final ClothCreateRequest clothCreateRequest) throws DataBaseException, ExistException, ImageInvalidException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clothResponse = this.clothService.createCloth(clothCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_CLOTH, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(clothResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<ClothResponse> updateCloth(@Valid @RequestBody final ClothUpdateRequest clothUpdateRequest) throws DataBaseException, NotFoundException, ExistException, ImageInvalidException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clothResponse = this.clothService.updateCloth(clothUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_CLOTH, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(clothResponse, messageResponse);
	}

	@Override
	@PutMapping("/{cloth}")
	public CrudResponse<ClothResponse> reactivateCloth(@PathVariable final UUID cloth) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clothResponse = this.clothService.reactivateCloth(cloth, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_CLOTH, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(clothResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{cloth}")
	public CrudResponse<ClothResponse> deleteCloth(@PathVariable final UUID cloth) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var clothResponse = this.clothService.deleteCloth(cloth, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_CLOTH, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(clothResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<ClothResponse> getAllCloth(@Valid final ClothFilterRequest clothFilterRequest, final ClothResponseStructureRequest clothResponseStruture, @Valid final PaginationRequest paginationRequest, final boolean random) throws DataBaseException, NotFoundException {
		return this.clothService.getAllCloth(clothFilterRequest, clothResponseStruture.getResponseStructure(), paginationRequest, random);
	}

	@Override
	@GetMapping("/{cloth}/history")
	public GetResponse<ClothHistoryResponse> getClothHistory(@PathVariable final UUID cloth, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.clothService.getClothHistory(cloth, paginationRequest);
	}

}
