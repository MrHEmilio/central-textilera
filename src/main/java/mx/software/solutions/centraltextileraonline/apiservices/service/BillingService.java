package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.List;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BillingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailInvoiceRequest;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetCfdiUseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetFiscalRegimensResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetProductCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetUnitCodeResponse;

public interface BillingService {

	List<GetCfdiUseResponse> getCfdiUse(String rfc) throws RestTemplateException;

	List<GetFiscalRegimensResponse> getFiscalRegimens() throws RestTemplateException;

	List<GetUnitCodeResponse> getUnitCode(String search) throws RestTemplateException;

	List<GetProductCodeResponse> getProductsCode(String search) throws RestTemplateException;

	void createBilling(final BillingCreateRequest billingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException;

	void sendEmailInvoice(final SendEmailInvoiceRequest sendEmailInvoiceRequest) throws NotFoundException, DataBaseException, RestTemplateException;

}
