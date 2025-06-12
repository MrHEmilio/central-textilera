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

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.SaleDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.SaleService;

@RestController
@RequestMapping("/sale")
public class SaleController implements SaleDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private SaleService saleService;

	@Override
	@PostMapping
	public CrudResponse<SaleResponse> createSale(@Valid @RequestBody final SaleCreateRequest saleCreateRequest) throws DataBaseException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.saleService.createSale(saleCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_SALE, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<SaleResponse> updateSale(@Valid @RequestBody final SaleUpdateRequest saleUpdateRequest) throws DataBaseException, NotFoundException, ExistException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.saleService.updateSale(saleUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_SALE, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@PutMapping("/{sale}")
	public CrudResponse<SaleResponse> reactivateSale(@PathVariable final UUID sale) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.saleService.reactivateSale(sale, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_SALE, MessageLangType.RESPONSE, DataBaseActionType.REACTIVATE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@DeleteMapping("/{sale}")
	public CrudResponse<SaleResponse> deleteSale(@PathVariable final UUID sale) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var saleResponse = this.saleService.deleteSale(sale, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.CATALOG_SALE, MessageLangType.RESPONSE, DataBaseActionType.DELETE);
		return new CrudResponse<>(saleResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<SaleResponse> getAllSale(@Valid final FilterRequest filterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.saleService.getAllSale(filterRequest, paginationRequest);
	}

	@Override
	@GetMapping("/{sale}/history")
	public GetResponse<SaleHistoryResponse> getSaleHistory(@PathVariable final UUID sale, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.saleService.getSaleHistory(sale, paginationRequest);
	}

}
