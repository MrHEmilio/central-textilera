package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BillingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BillingCreateWithoutAccountRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailInvoiceRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BillingCreateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SendEmailInvoiceResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetCfdiUseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetFiscalRegimensResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetProductCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetUnitCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.service.BillingService;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientAddressService;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientService;
import mx.software.solutions.centraltextileraonline.apiservices.service.OrderService;

@RestController
@RequestMapping("/billing")
public class BillingController {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private BillingService billingService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private ClientAddressService clientAddressService;

	@Autowired
	private OrderService orderService;

	@GetMapping("/cfdi/use")
	List<GetCfdiUseResponse> getCfdiUse(@RequestParam final String rfc) throws RestTemplateException {
		return this.billingService.getCfdiUse(rfc);
	}

	@GetMapping("/fiscal/regimen")
	List<GetFiscalRegimensResponse> getFiscalRegimens() throws RestTemplateException {
		return this.billingService.getFiscalRegimens();
	}

	@GetMapping("/unit/code")
	List<GetUnitCodeResponse> getUnitCode(@RequestParam(defaultValue = "") final String search) throws RestTemplateException {
		return this.billingService.getUnitCode(search);
	}

	@GetMapping("/product/code")
	List<GetProductCodeResponse> getProductsCode(@RequestParam final String search) throws RestTemplateException {
		return this.billingService.getProductsCode(search);
	}

	@PostMapping
	public BillingCreateResponse createBilling(@Valid @RequestBody final BillingCreateRequest billingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException {
		this.billingService.createBilling(billingCreateRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PAYMENT_BILLING, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new BillingCreateResponse(messageResponse);
	}

	@PostMapping("/without/account")
	public BillingCreateResponse createBillingWithoutAccount(@Valid @RequestBody final BillingCreateWithoutAccountRequest billingCreateWithoutAccountRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException {
		final var orderResponse = this.orderService.getOrderById(billingCreateWithoutAccountRequest.getOrder());
		final var clientResponse = orderResponse.getClient();
		final var clientUpdateRequest = new ClientUpdateRequest();
		clientUpdateRequest.setName(clientResponse.getName());
		clientUpdateRequest.setFirstLastname(clientResponse.getFirstLastname());
		clientUpdateRequest.setSecondLastname(clientResponse.getSecondLastname());
		clientUpdateRequest.setCountryCode(clientResponse.getCountryCode().getId());
		clientUpdateRequest.setPhone(clientResponse.getPhone());
		clientUpdateRequest.setRfc(billingCreateWithoutAccountRequest.getRfc());
		clientUpdateRequest.setCompanyName(billingCreateWithoutAccountRequest.getCompanyName());
		clientUpdateRequest.setFiscalRegimen(billingCreateWithoutAccountRequest.getFiscalRegimen());
		this.clientService.updateClient(clientUpdateRequest, clientResponse.getId());

		final var clientAddressCreateWithoutAccount = billingCreateWithoutAccountRequest.getBillingAddress();
		final var clientAddressCreateRequest = new ClientAddressCreateRequest();
		clientAddressCreateRequest.setName(clientResponse.getName() + "_" + UUID.randomUUID());
		clientAddressCreateRequest.setStreetName(clientAddressCreateWithoutAccount.getStreetName());
		clientAddressCreateRequest.setNumExt(clientAddressCreateWithoutAccount.getNumExt());
		clientAddressCreateRequest.setNumInt(clientAddressCreateWithoutAccount.getNumInt());
		clientAddressCreateRequest.setSuburb(clientAddressCreateWithoutAccount.getSuburb());
		clientAddressCreateRequest.setZipCode(clientAddressCreateWithoutAccount.getZipCode());
		clientAddressCreateRequest.setMunicipality(clientAddressCreateWithoutAccount.getMunicipality());
		clientAddressCreateRequest.setCity(clientAddressCreateWithoutAccount.getCity());
		clientAddressCreateRequest.setState(clientAddressCreateWithoutAccount.getState());
		clientAddressCreateRequest.setCountry(clientAddressCreateWithoutAccount.getCountry());
		clientAddressCreateRequest.setReferences(clientAddressCreateWithoutAccount.getReferences());
		clientAddressCreateRequest.setLatitude(clientAddressCreateWithoutAccount.getLatitude());
		clientAddressCreateRequest.setLongitude(clientAddressCreateWithoutAccount.getLongitude());
		clientAddressCreateRequest.setPredetermined(false);
		clientAddressCreateRequest.setBillingAddress(true);
		this.clientAddressService.createClientAddress(clientAddressCreateRequest, clientResponse.getId());

		final var billingCreateRequest = new BillingCreateRequest();
		billingCreateRequest.setOrder(billingCreateWithoutAccountRequest.getOrder());
		billingCreateRequest.setCfdiUse(billingCreateWithoutAccountRequest.getCfdiUse());
		this.billingService.createBilling(billingCreateRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PAYMENT_BILLING, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new BillingCreateResponse(messageResponse);
	}

	@PostMapping("/send/email")
	public SendEmailInvoiceResponse sendEmailInvoice(@Valid @RequestBody final SendEmailInvoiceRequest sendEmailInvoiceRequest) throws NotFoundException, DataBaseException, RestTemplateException {
		this.billingService.sendEmailInvoice(sendEmailInvoiceRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PAYMENT_BILLING, MessageLangType.RESPONSE, DataBaseActionType.READ);
		return new SendEmailInvoiceResponse(messageResponse);
	}

}
