package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.UUID;

import javax.validation.Valid;

import mx.software.solutions.centraltextileraonline.apiservices.service.implementation.OrderJsonComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.OrderDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateWithoutAccountRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.OrderStatusException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientAddressService;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientService;
import mx.software.solutions.centraltextileraonline.apiservices.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController implements OrderDocumentation {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private ClientAddressService clientAddressService;

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	OrderJsonComponent jsonComponent;

	@Value("${central-textilera.generic-password}")
	private String genericPassword;

	@Override
	@PostMapping
	public CrudResponse<OrderResponse> createOrder(@Valid @RequestBody final OrderCreateRequest orderCreateRequest) throws DataBaseException, NotFoundException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		if(orderCreateRequest.getPaymentId().trim().contains("-")){
			this.jsonComponent.receiveOrder(orderCreateRequest, sessionDto.getIdUser());
			return new CrudResponse<>(null, "SE CREÓ EL PEDIDO, STATUS: -PAGO PENDIENTE DE APROBACIÓN-");
		}
		final var orderResponse = this.orderService.createOrder(orderCreateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PAYMENT_ORDER, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(orderResponse, messageResponse);
	}

	@Override
	@PostMapping("/without/account")
	public CrudResponse<OrderResponse> createOrderWithoutAccount(@Valid @RequestBody final OrderCreateWithoutAccountRequest orderCreateWithoutAccountRequest) throws DataBaseException, NotFoundException, ExistException {
		ClientResponse clientResponse = null;
		try {
			clientResponse = this.clientService.getClientByEmail(orderCreateWithoutAccountRequest.getClient().getEmail());
		} catch (final NotFoundException notFoundException) {
			final var clientCreateRequest = new ClientCreateRequest();
			clientCreateRequest.setName(orderCreateWithoutAccountRequest.getClient().getName());
			clientCreateRequest.setFirstLastname(orderCreateWithoutAccountRequest.getClient().getFirstLastname());
			clientCreateRequest.setSecondLastname(orderCreateWithoutAccountRequest.getClient().getSecondLastname());
			clientCreateRequest.setCountryCode(orderCreateWithoutAccountRequest.getClient().getCountryCode());
			clientCreateRequest.setPhone(orderCreateWithoutAccountRequest.getClient().getPhone());
			clientCreateRequest.setEmail(orderCreateWithoutAccountRequest.getClient().getEmail());
			clientCreateRequest.setPassword(this.genericPassword);
			if (orderCreateWithoutAccountRequest.isOrderBilling()) {
				clientCreateRequest.setRfc(orderCreateWithoutAccountRequest.getClient().getRfc());
				clientCreateRequest.setCompanyName(orderCreateWithoutAccountRequest.getClient().getCompanyName());
				clientCreateRequest.setFiscalRegimen(orderCreateWithoutAccountRequest.getClient().getFiscalRegimen());
			}
			clientResponse = this.clientService.createClient(clientCreateRequest);
		}
		ShippingCreateRequest shippingCreateRequest = null;
		UUID billingAddress = null;
		if (DeliveryMethod.SHIPPING.equals(orderCreateWithoutAccountRequest.getDeliveryMethod())) {
			final var clientAddressCreateWithoutAccount = orderCreateWithoutAccountRequest.getShipping().getClientAddress();
			final var clientAddressCreateRequest = new ClientAddressCreateRequest();
			clientAddressCreateRequest.setName(orderCreateWithoutAccountRequest.getClient().getName() + "_" + UUID.randomUUID());
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
			clientAddressCreateRequest.setBillingAddress(false);
			final var clientAddressResponse = this.clientAddressService.createClientAddress(clientAddressCreateRequest, clientResponse.getId());

			final var shippingCreateWithoutAccount = orderCreateWithoutAccountRequest.getShipping();
			shippingCreateRequest = new ShippingCreateRequest();
			shippingCreateRequest.setClientAddress(clientAddressResponse.getId());
			shippingCreateRequest.setProvider(shippingCreateWithoutAccount.getProvider());
			shippingCreateRequest.setServiceCode(shippingCreateWithoutAccount.getServiceCode());
			shippingCreateRequest.setServiceName(shippingCreateWithoutAccount.getServiceName());
			shippingCreateRequest.setPrice(shippingCreateWithoutAccount.getPrice());
			shippingCreateRequest.setDate(shippingCreateWithoutAccount.getDate());
			shippingCreateRequest.setShippingMethod(shippingCreateWithoutAccount.getShippingMethod());
			shippingCreateRequest.setRateId(shippingCreateWithoutAccount.getRateId());

			if (orderCreateWithoutAccountRequest.getBillingAddress() != null && orderCreateWithoutAccountRequest.isAddressSame())
				billingAddress = clientAddressResponse.getId();
		}

		if (orderCreateWithoutAccountRequest.getBillingAddress() != null && !orderCreateWithoutAccountRequest.isAddressSame()) {
			final var clientAddressCreateWithoutAccount = orderCreateWithoutAccountRequest.getBillingAddress();
			final var clientAddressCreateRequest = new ClientAddressCreateRequest();
			clientAddressCreateRequest.setName(orderCreateWithoutAccountRequest.getClient().getName() + "_" + UUID.randomUUID());
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
			final var clientAddressResponse = this.clientAddressService.createClientAddress(clientAddressCreateRequest, clientResponse.getId());
			billingAddress = clientAddressResponse.getId();
		}

		final var orderCreateRequest = new OrderCreateRequest();
		orderCreateRequest.setCloths(orderCreateWithoutAccountRequest.getCloths());
		orderCreateRequest.setSamplers(orderCreateWithoutAccountRequest.getSamplers());
		orderCreateRequest.setShipping(shippingCreateRequest);
		orderCreateRequest.setDeliveryMethod(orderCreateWithoutAccountRequest.getDeliveryMethod());
		orderCreateRequest.setPaymentMethod(orderCreateWithoutAccountRequest.getPaymentMethod());
		orderCreateRequest.setPaymentId(orderCreateWithoutAccountRequest.getPaymentId());
		orderCreateRequest.setBillingAddress(billingAddress);

		if(orderCreateRequest.getPaymentId().trim().contains("-")){
			System.out.println("Entró la orden al almacenamiento JSON! ");
			this.jsonComponent.receiveOrder(orderCreateRequest, clientResponse.getId());
			return new CrudResponse<>(new OrderResponse(), "Pedido en revisión");
		}

		final var orderResponse = this.orderService.createOrder(orderCreateRequest, clientResponse.getId());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PAYMENT_ORDER, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
		return new CrudResponse<>(orderResponse, messageResponse);
	}

	@Override
	@PutMapping
	public CrudResponse<OrderResponse> updateOrder(@RequestBody final OrderUpdateRequest orderUpdateRequest) throws DataBaseException, NotFoundException, OrderStatusException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		final var orderResponse = this.orderService.updateOrder(orderUpdateRequest, sessionDto.getIdUser());
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PAYMENT_ORDER, MessageLangType.RESPONSE, DataBaseActionType.UPDATE);
		return new CrudResponse<>(orderResponse, messageResponse);
	}

	@Override
	@GetMapping
	public GetResponse<OrderResponse> getAllOrder(@Valid final OrderFilterRequest orderFilterRequest, @Valid final DateFilterRequest dateFilter, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		if (this.sessionHelper.isAdmin())
			return this.orderService.getAllOrder(orderFilterRequest, dateFilter, paginationRequest);
		return this.orderService.getAllOrderByClient(this.sessionHelper.getSessionDto().getIdUser(), orderFilterRequest, dateFilter, paginationRequest);
	}

	@Override
	@GetMapping("/client/{client}")
	public GetResponse<OrderResponse> getAllOrderByClient(@PathVariable final UUID client, @Valid final OrderFilterRequest orderFilterRequest, @Valid final DateFilterRequest dateFilter, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		return this.orderService.getAllOrderByClient(client, orderFilterRequest, dateFilter, paginationRequest);
	}

	@Override
	@GetMapping("{order}")
	public OrderResponse getOrderById(@PathVariable final UUID order) throws DataBaseException, NotFoundException {
		return this.orderService.getOrderById(order);
	}



}
