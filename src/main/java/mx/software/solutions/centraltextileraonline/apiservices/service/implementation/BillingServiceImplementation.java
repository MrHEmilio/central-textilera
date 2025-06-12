package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BillingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailInvoiceRequest;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientAddressEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderBillingEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ApiRestTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientAddressRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderRepository;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.CreateCfdiRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.ItemRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.ReceiverRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.TaxesRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.CreateCfdiResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetCfdiUseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetFiscalRegimensResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetProductCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.GetUnitCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.SendEmailCfdiResponse;
import mx.software.solutions.centraltextileraonline.apiservices.service.BillingService;

@Slf4j
@Service
public class BillingServiceImplementation implements BillingService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ClientAddressRepository clientAddressRepository;

	@Autowired
	private OrderHelper orderHelper;

	@Value("${central-textilera.address.zip-code}")
	private String storeZipCode;

	@Value("${central-textilera.billing.iva}")
	private BigDecimal iva;

	@Value("${url.api.facturama.create-cfdi}")
	private String urlApiFacturamaCfdiCreate;

	@Value("${url.api.facturama.get-cfdi-use}")
	private String urlApiFacturamaGetCfdiUse;

	@Value("${url.api.facturama.get-fiscal-regimens}")
	private String urlApiFacturamaGetFiscalRegimens;

	@Value("${url.api.facturama.get-units-code}")
	private String urlApiFacturamaGetUnitsCode;

	@Value("${url.api.facturama.get-products-code}")
	private String urlApiFacturamaGetProductsCode;

	@Value("${url.api.facturama.send-email-cfdi}")
	private String urlApiFacturamaSendEmailCfdi;

	@Value("${token.facturama.username}")
	private String tokenFacturamaUsername;

	@Value("${token.facturama.password}")
	private String tokenFacturamaPassword;

	@Override
	public List<GetCfdiUseResponse> getCfdiUse(final String rfc) throws RestTemplateException {
		final var restTemplate = new RestTemplate();
		final var httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(this.tokenFacturamaUsername, this.tokenFacturamaPassword);
		final var httpEntity = new HttpEntity<>(httpHeaders);
		final var uriComponents = UriComponentsBuilder.fromHttpUrl(this.urlApiFacturamaGetCfdiUse).queryParam("rfc", rfc.toUpperCase()).encode().build();
		GetCfdiUseResponse[] arrayCfdiUseResponse = null;
		try {
			BillingServiceImplementation.log.info("Starting consume service {}.", uriComponents.toUriString());
			final var responseEntity = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, httpEntity, GetCfdiUseResponse[].class);
			arrayCfdiUseResponse = responseEntity.getBody();
			BillingServiceImplementation.log.info("Finished consume service {}.", uriComponents.toUriString());
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("Error to consume service {}.", uriComponents.toUriString(), exception);
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "get.cfdi.use", exception.getMessage());
		}
		// return Arrays.asList(arrayCfdiUseResponse).stream().sorted().collect(Collectors.toList());

		return Arrays.asList(arrayCfdiUseResponse);
	}

	@Override
	public List<GetFiscalRegimensResponse> getFiscalRegimens() throws RestTemplateException {
		final var restTemplate = new RestTemplate();
		final var httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(this.tokenFacturamaUsername, this.tokenFacturamaPassword);
		final var httpEntity = new HttpEntity<>(httpHeaders);
		GetFiscalRegimensResponse[] arrayFiscalRegimensResponse = null;
		try {
			BillingServiceImplementation.log.info("Starting consume service {}.", this.urlApiFacturamaGetFiscalRegimens);
			final var responseEntity = restTemplate.exchange(this.urlApiFacturamaGetFiscalRegimens, HttpMethod.GET, httpEntity, GetFiscalRegimensResponse[].class);
			arrayFiscalRegimensResponse = responseEntity.getBody();
			BillingServiceImplementation.log.info("Finished consume service {}.", this.urlApiFacturamaGetFiscalRegimens);
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("Error to consume service {}.", this.urlApiFacturamaGetFiscalRegimens, exception);
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "get.fiscal.regimens", exception.getMessage());
		}
		return Arrays.asList(arrayFiscalRegimensResponse);
	}

	@Override
	public List<GetUnitCodeResponse> getUnitCode(final String search) throws RestTemplateException {
		final var restTemplate = new RestTemplate();
		final var httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(this.tokenFacturamaUsername, this.tokenFacturamaPassword);
		final var httpEntity = new HttpEntity<>(httpHeaders);
		final var uriComponents = UriComponentsBuilder.fromHttpUrl(this.urlApiFacturamaGetUnitsCode).queryParam("keyword", search).encode().build();
		GetUnitCodeResponse[] arrayUnitCodeResponse = null;
		try {
			BillingServiceImplementation.log.info("Starting consume service {}.", uriComponents.toUriString());
			final var responseEntity = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, httpEntity, GetUnitCodeResponse[].class);
			arrayUnitCodeResponse = responseEntity.getBody();
			BillingServiceImplementation.log.info("Finished consume service {}.", uriComponents.toUriString());
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("Error to consume service {}.", uriComponents.toUriString(), exception);
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "get.unit.code", exception.getMessage());
		}

		return Arrays.asList(arrayUnitCodeResponse).stream()
				.sorted((str1, str2)->{
					int posUno = str1.toString().toLowerCase().indexOf(search.toLowerCase());
					int posDos = str2.toString().toLowerCase().indexOf(search.toLowerCase());

					posUno = (posUno == -1) ? Integer.MAX_VALUE : posUno;
					posDos = (posDos == -1) ? Integer.MAX_VALUE : posDos;

					return Integer.compare(posUno, posDos);

				}).collect(Collectors.toList());
	}

	@Override
	public List<GetProductCodeResponse> getProductsCode(final String search) throws RestTemplateException {
		final var restTemplate = new RestTemplate();
		final var httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(this.tokenFacturamaUsername, this.tokenFacturamaPassword);
		final var httpEntity = new HttpEntity<>(httpHeaders);
		final var uriComponents = UriComponentsBuilder.fromHttpUrl(this.urlApiFacturamaGetProductsCode).queryParam("keyword", search).encode().build();
		GetProductCodeResponse[] arrayProductCodeResponse = null;
		try {
			BillingServiceImplementation.log.info("Starting consume service {}.", uriComponents.toUriString());
			final var responseEntity = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, httpEntity, GetProductCodeResponse[].class);
			arrayProductCodeResponse = responseEntity.getBody();
			BillingServiceImplementation.log.info("Finished consume service {}.", uriComponents.toUriString());
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("Error to consume service {}.", uriComponents.toUriString(), exception);
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "get.cfdi.use", exception.getMessage());
		}

		return Arrays.asList(arrayProductCodeResponse).stream()
				.sorted((str1, str2)->{
					int posUno = str1.toString().toLowerCase().indexOf(search.toLowerCase());
					int posDos = str2.toString().toLowerCase().indexOf(search.toLowerCase());

					posUno = (posUno == -1) ? Integer.MAX_VALUE : posUno;
					posDos = (posDos == -1) ? Integer.MAX_VALUE : posDos;

					return Integer.compare(posUno, posDos);

				}).collect(Collectors.toList());
	}

	@Override
	public void createBilling(final BillingCreateRequest billingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException {
		final var orderEntity = this.orderHelper.getOrderEntity(billingCreateRequest.getOrder());
		this.validateBillingNotExist(orderEntity.getOrderBillingEntity(), orderEntity.getClientEntity().getId());
		this.validateBillingDataClientExist(orderEntity.getClientEntity());

		final var clientEntity = orderEntity.getClientEntity();
		final var billingClientAddressEntity = this.clientAddressRepository.findByClientEntityAndBillingAddressTrue(clientEntity).orElse(new ClientAddressEntity());
		final var ivaDto = new IvaDto();

		final var receiverRequest = new ReceiverRequest();
		receiverRequest.setRfc(clientEntity.getRfc().toUpperCase());
		receiverRequest.setName(clientEntity.getCompanyName().toUpperCase());
		receiverRequest.setFiscalRegime(clientEntity.getFiscalRegimen());
		receiverRequest.setCfdiUse(billingCreateRequest.getCfdiUse());
		receiverRequest.setTaxZipCode(billingClientAddressEntity.getZipCode());

		final List<ItemRequest> listItemsRequest = new ArrayList<>();
		orderEntity.getOrderClothEntities().forEach(orderClothEntity -> {
			final var ivaSubtotal = orderClothEntity.getSellPrice().multiply(this.iva);
			ivaDto.setIvaTotal(ivaDto.getIvaTotal().add(ivaSubtotal));

			final var taxesRequest = new TaxesRequest();
			taxesRequest.setTotal(ivaSubtotal);
			taxesRequest.setName("IVA");
			taxesRequest.setBase(orderClothEntity.getSellPrice());
			taxesRequest.setRate(this.iva);

			final var clothEntity = orderClothEntity.getClothEntity();
			final var clothBillingEntity = clothEntity.getClothBillingEntity();
			final var itemRequest = new ItemRequest();
			itemRequest.setProductCode(clothBillingEntity.getProductCode());
			itemRequest.setIdentificationNumber(clothEntity.getId().toString());
			itemRequest.setDescription(clothEntity.getName() + " " + orderClothEntity.getColorName());
			itemRequest.setUnit(this.getUnitText(clothEntity.getSaleEntity().getName()));
			itemRequest.setUnitCode(clothBillingEntity.getUnitCode());
			itemRequest.setUnitPrice(orderClothEntity.getSellPrice().subtract(ivaSubtotal).divide(orderClothEntity.getAmount()));
			itemRequest.setQuantity(orderClothEntity.getAmount());
			itemRequest.setSubtotal(orderClothEntity.getSellPrice().subtract(ivaSubtotal));
			itemRequest.setTaxObject("02");
			itemRequest.setTaxes(Arrays.asList(taxesRequest));
			itemRequest.setTotal(orderClothEntity.getSellPrice());
			listItemsRequest.add(itemRequest);
		});

		orderEntity.getOrderSamplerEntities().forEach(orderSamplerEntity -> {
			final var ivaSubtotal = orderSamplerEntity.getSellPrice().multiply(this.iva);
			ivaDto.setIvaTotal(ivaDto.getIvaTotal().add(ivaSubtotal));

			final var taxesRequest = new TaxesRequest();
			taxesRequest.setTotal(ivaSubtotal);
			taxesRequest.setName("IVA");
			taxesRequest.setBase(orderSamplerEntity.getSellPrice());
			taxesRequest.setRate(this.iva);

			final var clothEntity = orderSamplerEntity.getClothEntity();
			final var clothSamplerBillingEntity = clothEntity.getClothSamplerEntity().getClothSamplerBillingEntity();
			final var itemRequest = new ItemRequest();
			itemRequest.setProductCode(clothSamplerBillingEntity.getProductCode());
			itemRequest.setIdentificationNumber(clothEntity.getClothSamplerEntity().getId().toString());
			itemRequest.setDescription("Muestrario " + clothEntity.getName());
			itemRequest.setUnit("Pieza");
			itemRequest.setUnitCode(clothSamplerBillingEntity.getUnitCode());
			itemRequest.setUnitPrice(orderSamplerEntity.getSellPrice().subtract(ivaSubtotal).divide(orderSamplerEntity.getAmount()));
			itemRequest.setQuantity(orderSamplerEntity.getAmount());
			itemRequest.setSubtotal(orderSamplerEntity.getSellPrice().subtract(ivaSubtotal));
			itemRequest.setTaxObject("02");
			itemRequest.setTaxes(Arrays.asList(taxesRequest));
			itemRequest.setTotal(orderSamplerEntity.getSellPrice());
			listItemsRequest.add(itemRequest);
		});

		if (orderEntity.getOrderShippingEntity() != null) {
			final var orderShippingEntity = orderEntity.getOrderShippingEntity();
			final var ivaSubtotal = orderShippingEntity.getPrice().multiply(this.iva);
			ivaDto.setIvaTotal(ivaDto.getIvaTotal().add(ivaSubtotal));

			final var taxesRequest = new TaxesRequest();
			taxesRequest.setTotal(ivaSubtotal);
			taxesRequest.setName("IVA");
			taxesRequest.setBase(orderShippingEntity.getPrice());
			taxesRequest.setRate(this.iva);

			final var itemRequest = new ItemRequest();
			itemRequest.setProductCode("10101504"); // HARDCODE
			itemRequest.setIdentificationNumber(orderShippingEntity.getProvider());
			itemRequest.setDescription(orderShippingEntity.getServiceName());
			itemRequest.setUnit(this.getUnitText(orderShippingEntity.getServiceCode()));
			itemRequest.setUnitCode("28"); // HARDCODE
			itemRequest.setUnitPrice(orderShippingEntity.getPrice().subtract(ivaSubtotal));
			itemRequest.setQuantity(BigDecimal.ONE);
			itemRequest.setSubtotal(orderShippingEntity.getPrice().subtract(ivaSubtotal));
			itemRequest.setTaxObject("02");
			itemRequest.setTaxes(Arrays.asList(taxesRequest));
			itemRequest.setTotal(orderShippingEntity.getPrice());
			listItemsRequest.add(itemRequest);
		}

		final var dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		final var createCfdiRequest = new CreateCfdiRequest();
		createCfdiRequest.setNameId(1);
		createCfdiRequest.setDate(dateFormat.format(new Date()));
		createCfdiRequest.setCurrency("MXN");
		createCfdiRequest.setExpeditionPlace(this.storeZipCode);
		createCfdiRequest.setFolio(String.valueOf(orderEntity.getNumber()));
		createCfdiRequest.setCfdiType("I");
		createCfdiRequest.setPaymentForm("03");
		createCfdiRequest.setPaymentMethod("PUE");
		createCfdiRequest.setReceiver(receiverRequest);
		createCfdiRequest.setItems(listItemsRequest);

		final var restTemplate = new RestTemplate();
		final var httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(this.tokenFacturamaUsername, this.tokenFacturamaPassword);
		final var httpEntity = new HttpEntity<>(createCfdiRequest, httpHeaders);
		CreateCfdiResponse createCfdiResponse = null;
		try {
			BillingServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiFacturamaCfdiCreate, createCfdiRequest);
			createCfdiResponse = restTemplate.postForObject(this.urlApiFacturamaCfdiCreate, httpEntity, CreateCfdiResponse.class);
			BillingServiceImplementation.log.info("Finished consume service {} with this response {}.", this.urlApiFacturamaCfdiCreate, createCfdiResponse);
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("Error to consume service {} with this request {}.", this.urlApiFacturamaCfdiCreate, createCfdiRequest, exception);
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "create.cfdi", exception.getMessage());
		}
		final var id = createCfdiResponse.getId();
		final var streetName = billingClientAddressEntity.getStreetName();
		final var numExt = billingClientAddressEntity.getNumExt();
		final var numInt = billingClientAddressEntity.getNumInt();
		final var zipCode = billingClientAddressEntity.getZipCode();
		final var suburb = billingClientAddressEntity.getSuburb();
		final var municipality = billingClientAddressEntity.getMunicipality();
		final var state = billingClientAddressEntity.getState();
		final var city = billingClientAddressEntity.getCity();
		final var country = billingClientAddressEntity.getCountry();
		final var rfc = clientEntity.getRfc().toUpperCase();
		final var companyName = clientEntity.getCompanyName().toUpperCase();
		final var fiscalRegimen = clientEntity.getFiscalRegimen();

		final var orderBillingEntity = new OrderBillingEntity();
		orderBillingEntity.setBillingId(id);
		orderBillingEntity.setStreetName(streetName);
		orderBillingEntity.setNumExt(numExt);
		orderBillingEntity.setNumInt(numInt);
		orderBillingEntity.setZipCode(zipCode);
		orderBillingEntity.setSuburb(suburb);
		orderBillingEntity.setMunicipality(municipality);
		orderBillingEntity.setState(state);
		orderBillingEntity.setCity(city);
		orderBillingEntity.setCountry(country);
		orderBillingEntity.setRfc(rfc.toUpperCase());
		orderBillingEntity.setCompanyName(companyName.toUpperCase());
		orderBillingEntity.setFiscalRegimen(fiscalRegimen);
		orderBillingEntity.setIva(ivaDto.getIvaTotal());

		orderEntity.setOrderBillingEntity(orderBillingEntity);
		orderBillingEntity.setOrderEntity(orderEntity);

		try {
			this.orderRepository.save(orderEntity);
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("The order could not create CFDI for the cliente id {}", clientEntity.getId());
			throw new DataBaseException(Controller.PAYMENT_BILLING, DataBaseActionType.CREATE);
		}

		final var sendEmailInvoiceRequest = new SendEmailInvoiceRequest();
		sendEmailInvoiceRequest.setOrder(orderEntity.getId());
		this.sendEmailInvoice(sendEmailInvoiceRequest);
	}

	@Override
	public void sendEmailInvoice(final SendEmailInvoiceRequest sendEmailInvoiceRequest) throws NotFoundException, DataBaseException, RestTemplateException {
		final var orderEntity = this.orderHelper.getOrderEntity(sendEmailInvoiceRequest.getOrder());
		this.validateBillingExist(orderEntity.getOrderBillingEntity(), orderEntity.getClientEntity().getId());
		final var cfdiId = orderEntity.getOrderBillingEntity().getBillingId();
		final var email = orderEntity.getClientEntity().getCredentialEntity().getEmail();
		final var restTemplate = new RestTemplate();
		final var httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(this.tokenFacturamaUsername, this.tokenFacturamaPassword);
		final var httpEntity = new HttpEntity<>(httpHeaders);
		final var uriComponents = UriComponentsBuilder.fromHttpUrl(this.urlApiFacturamaSendEmailCfdi).queryParam("cfdiType", "issued").queryParam("cfdiId", cfdiId).queryParam("email", email).encode().build();
		SendEmailCfdiResponse sendEmailCfdiResponse = null;
		try {
			BillingServiceImplementation.log.info("Starting consume service {}.", uriComponents.toUriString());
			final var responseEntity = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, httpEntity, SendEmailCfdiResponse.class);
			sendEmailCfdiResponse = responseEntity.getBody();
			BillingServiceImplementation.log.info("Finished consume service {}.", uriComponents.toUriString());
		} catch (final Exception exception) {
			BillingServiceImplementation.log.info("Error to consume service {}.", uriComponents.toUriString(), exception);
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "send.email.cfdi", exception.getMessage());
		}

		if (!sendEmailCfdiResponse.isSuccess()) {
			BillingServiceImplementation.log.info("Service {} response with false.", uriComponents.toUriString());
			throw new RestTemplateException(ApiRestTemplate.FACTURAMA, "send.email.cfdi", "");
		}
	}

	private void validateBillingDataClientExist(final ClientEntity clientEntity) throws NotFoundException {
		BillingServiceImplementation.log.info("Starting validated the data client for billing with the id {}.", clientEntity.getId());
		if (clientEntity.getRfc() == null) {
			BillingServiceImplementation.log.info("Not found RFC for the client billing data with the id {}.", clientEntity.getId());
			throw new NotFoundException(Controller.PAYMENT_BILLING, "rfc");
		}
		if (clientEntity.getCompanyName() == null) {
			BillingServiceImplementation.log.info("Not found Company Name for the client billing data with the id {}.", clientEntity.getId());
			throw new NotFoundException(Controller.PAYMENT_BILLING, "company.name");
		}
		final var billingClientAddressEntity = this.clientAddressRepository.findByClientEntityAndBillingAddressTrue(clientEntity);
		if (billingClientAddressEntity.isEmpty()) {
			BillingServiceImplementation.log.info("Not found Billing Address for the client billing data with the id {}.", clientEntity.getId());
			throw new NotFoundException(Controller.PAYMENT_BILLING, "billing.address");
		}
		BillingServiceImplementation.log.info("Finished validate the data client for billing with the id {}.", clientEntity.getId());
	}

	private void validateBillingNotExist(final OrderBillingEntity orderBillingEntity, final UUID client) throws ExistException {
		BillingServiceImplementation.log.info("Starting validated the client with the id {} if not exists billing.", client);
		if (orderBillingEntity != null) {
			BillingServiceImplementation.log.info("The client with the id {} has billing.", client);
			throw new ExistException(Controller.PAYMENT_BILLING, "cfdi", client.toString());
		}
	}

	private void validateBillingExist(final OrderBillingEntity orderBillingEntity, final UUID client) throws NotFoundException {
		BillingServiceImplementation.log.info("Starting validated the client with the id {} if exists billing.", client);
		if (orderBillingEntity == null) {
			BillingServiceImplementation.log.info("The client with the id {} not has billing.", client);
			throw new NotFoundException(Controller.PAYMENT_BILLING, "cfdi", client.toString());
		}
	}

	private String getUnitText(final String name) {
		if (name.length() >= 21)
			return name.substring(0, 20);
		return name;
	}

}

@Data
class IvaDto {
	private BigDecimal ivaTotal = BigDecimal.ZERO;
}