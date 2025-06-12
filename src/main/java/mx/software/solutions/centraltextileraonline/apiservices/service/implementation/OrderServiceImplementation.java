package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderUpdatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderSamplerEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderShippingEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderStatusEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.OrderStatusException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClientAddressHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClientHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.DateHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SendEmailHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothSamplerRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothVariantRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;
import mx.software.solutions.centraltextileraonline.apiservices.service.OrderService;
import mx.software.solutions.centraltextileraonline.apiservices.service.PaymentService;

@Slf4j
@Service
public class OrderServiceImplementation implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ClothVariantRepository clothVariantRepository;

	@Autowired
	private ClothSamplerRepository clothSamplerRepository;

	@Autowired
	private OrderHelper orderHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private ClientHelper clientHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private ClientAddressHelper clientAddressHelper;

	@Autowired
	private DateHelper dateHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private SendEmailHelper sendEmailHelper;

	@Value("${central-textilera.billing.iva}")
	private BigDecimal iva;

	@Override
	public OrderResponse createOrder(final OrderCreateRequest orderCreateRequest, final UUID client) throws DataBaseException, NotFoundException {
		OrderServiceImplementation.log.info("Starting created order for the cliente id {}", client);
		final var listCalculatePriceResponse = this.paymentService.calculatePriceProduct(orderCreateRequest.getCloths(), orderCreateRequest.getSamplers());
		final var clientEntity = this.clientHelper.getClientEntity(client);
		final var sellPrice = BigDecimal.valueOf(listCalculatePriceResponse.stream().mapToDouble(calculatePriceResponse -> calculatePriceResponse.getTotalSellPrice().doubleValue()).sum());
		var total = sellPrice;

		final List<OrderClothEntity> listOrderClothEntities = new ArrayList<>();
		final List<OrderSamplerEntity> listOrderSamplerEntities = new ArrayList<>();

		listCalculatePriceResponse.forEach(calculatePriceResponse -> {
			final var optionalClothVariantEntity = this.clothVariantRepository.findById(calculatePriceResponse.getProduct());
			if (optionalClothVariantEntity.isPresent()) {
				final var clothVariantEntity = optionalClothVariantEntity.get();
				clothVariantEntity.setAmount(clothVariantEntity.getAmount().subtract(calculatePriceResponse.getAmount()));

				final var orderClothEntity = new OrderClothEntity();
				orderClothEntity.setClothEntity(clothVariantEntity.getClothEntity());
				orderClothEntity.setClothName(clothVariantEntity.getClothEntity().getName());
				orderClothEntity.setColorName(clothVariantEntity.getColorEntity().getName());
				orderClothEntity.setAmount(calculatePriceResponse.getAmount());
				orderClothEntity.setSellPrice(calculatePriceResponse.getTotalSellPrice());
				orderClothEntity.setTotalSellPrice(calculatePriceResponse.getTotalSellPriceNormal());
				orderClothEntity.setDiscount(calculatePriceResponse.getDiscount());
				listOrderClothEntities.add(orderClothEntity);
			}

			final var optionalClothSamplerEntity = this.clothSamplerRepository.findById(calculatePriceResponse.getProduct());
			if (optionalClothSamplerEntity.isPresent()) {
				final var clothSamplerEntity = optionalClothSamplerEntity.get();
				clothSamplerEntity.setAmount(clothSamplerEntity.getAmount().subtract(calculatePriceResponse.getAmount()));

				final var orderSamplerEntity = new OrderSamplerEntity();
				orderSamplerEntity.setClothEntity(clothSamplerEntity.getClothEntity());
				orderSamplerEntity.setSamplerName("Muestrario " + clothSamplerEntity.getClothEntity().getName());
				orderSamplerEntity.setAmount(calculatePriceResponse.getAmount());
				orderSamplerEntity.setSellPrice(calculatePriceResponse.getSellPrice());
				orderSamplerEntity.setTotalSellPrice(calculatePriceResponse.getTotalSellPriceNormal());
				orderSamplerEntity.setDiscount(calculatePriceResponse.getDiscount());
				listOrderSamplerEntities.add(orderSamplerEntity);
			}
		});

		final List<OrderStatusEntity> orderStatusEntities = new ArrayList<>();
		final var orderStatusEntity = new OrderStatusEntity();
		orderStatusEntity.setStatus(OrderStatus.REVISION);
		orderStatusEntity.setDate(new Date());
		orderStatusEntities.add(orderStatusEntity);

		final var orderShippingEntity = new OrderShippingEntity();

		var shippingPrice = BigDecimal.ZERO;
		if (DeliveryMethod.SHIPPING.equals(orderCreateRequest.getDeliveryMethod())) {
			shippingPrice = orderCreateRequest.getShipping().getPrice();
			final var shippingClientAddressEntity = this.clientAddressHelper.getClientAddressEntity(orderCreateRequest.getShipping().getClientAddress());
			orderShippingEntity.setStreetName(shippingClientAddressEntity.getStreetName());
			orderShippingEntity.setNumExt(shippingClientAddressEntity.getNumExt());
			orderShippingEntity.setNumInt(shippingClientAddressEntity.getNumInt());
			orderShippingEntity.setZipCode(shippingClientAddressEntity.getZipCode());
			orderShippingEntity.setSuburb(shippingClientAddressEntity.getSuburb());
			orderShippingEntity.setMunicipality(shippingClientAddressEntity.getMunicipality());
			orderShippingEntity.setState(shippingClientAddressEntity.getState());
			orderShippingEntity.setCity(shippingClientAddressEntity.getCity());
			orderShippingEntity.setCountry(shippingClientAddressEntity.getCountry());
			orderShippingEntity.setReferences(shippingClientAddressEntity.getReferences());
			orderShippingEntity.setProvider(orderCreateRequest.getShipping().getProvider());
			orderShippingEntity.setServiceCode(orderCreateRequest.getShipping().getServiceCode());
			orderShippingEntity.setServiceName(orderCreateRequest.getShipping().getServiceName());
			orderShippingEntity.setPrice(orderCreateRequest.getShipping().getPrice());
			orderShippingEntity.setDate(orderCreateRequest.getShipping().getDate());
			orderShippingEntity.setShippingMethod(orderCreateRequest.getShipping().getShippingMethod());
			orderShippingEntity.setRateId(orderCreateRequest.getShipping().getRateId());
			total = sellPrice.add(shippingPrice);
		}

		final var optionalLastOrderEntity = this.orderRepository.findFirstByOrderByNumberDesc();
		var number = 1;
		if (optionalLastOrderEntity.isPresent())
			number = optionalLastOrderEntity.get().getNumber() + 1;

		final var orderEntity = new OrderEntity();
		orderEntity.setClientEntity(clientEntity);
		orderEntity.setNumber(number);
		orderEntity.setTotal(total);
		orderEntity.setOrderClothEntities(listOrderClothEntities);
		orderEntity.setOrderSamplerEntities(listOrderSamplerEntities);
		orderEntity.setDeliveryMethod(orderCreateRequest.getDeliveryMethod());
		orderEntity.setPaymentMethod(orderCreateRequest.getPaymentMethod());
		orderEntity.setPaymentId(orderCreateRequest.getPaymentId());
		orderEntity.setOrderStatusEntities(orderStatusEntities);

		if (DeliveryMethod.SHIPPING.equals(orderCreateRequest.getDeliveryMethod())) {
			orderEntity.setOrderShippingEntity(orderShippingEntity);
			orderShippingEntity.setOrderEntity(orderEntity);
		}

		OrderEntity newOrderEntity = null;
		try {
			newOrderEntity = this.orderRepository.save(orderEntity);
			OrderServiceImplementation.log.info("Finished create order for the cliente id {}", client);
		} catch (final Exception exception) {
			OrderServiceImplementation.log.info("The order could not create for the cliente id {}", client);
			throw new DataBaseException(Controller.PAYMENT_ORDER, DataBaseActionType.CREATE);
		}

		this.sendEmailHelper.sendEmailOrderCreated(newOrderEntity.getId());

		return this.orderHelper.convertOrder(newOrderEntity);
	}

	@Override
	public OrderResponse updateOrder(final OrderUpdateRequest orderUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, OrderStatusException {
		OrderServiceImplementation.log.info("Starting update the order with id {}.", orderUpdateRequest.getOrder());
		final var adminEntity = this.adminHelper.getAdminEntity(admin);
		final var orderEntity = this.orderHelper.getOrderEntity(orderUpdateRequest.getOrder());
		final var orderSta = orderEntity.getOrderStatusEntities();

		final var lastOrderStatusEntity = orderSta.get(orderSta.size()-1);
		final var orderStatusEntities = orderEntity.getOrderStatusEntities();

		final var orderStatusEntity = new OrderStatusEntity();
		orderStatusEntity.setAdminEntity(adminEntity);
		orderStatusEntity.setDate(new Date());

		switch (lastOrderStatusEntity.getStatus()) {
		case REVISION:
			orderStatusEntity.setStatus(OrderStatus.PREPARATION);
			break;
		case PREPARATION:
			if (DeliveryMethod.SHIPPING.equals(orderEntity.getDeliveryMethod()))
				orderStatusEntity.setStatus(OrderStatus.SHIPPING);
			else if (DeliveryMethod.PICK_UP.equals(orderEntity.getDeliveryMethod()))
				orderStatusEntity.setStatus(OrderStatus.PICK_UP);
			break;
		case SHIPPING:
		case PICK_UP:
			orderStatusEntity.setStatus(OrderStatus.DELIVERED);
			break;
		case DELIVERED:
			throw new OrderStatusException();
		}
		if (this.sessionHelper.isAdminRoot()) {
			var orderStatus = orderUpdateRequest.getOrderStatus();
			if (OrderStatus.SHIPPING.equals(orderStatus) || OrderStatus.PICK_UP.equals(orderStatus)) {
				if (DeliveryMethod.SHIPPING.equals(orderEntity.getDeliveryMethod()))
					orderStatus = OrderStatus.SHIPPING;
				else if (DeliveryMethod.PICK_UP.equals(orderEntity.getDeliveryMethod()))
					orderStatus = OrderStatus.PICK_UP;
			}
			orderStatusEntity.setStatus(orderStatus);
		}
		orderStatusEntities.add(orderStatusEntity);
		try {
			OrderServiceImplementation.log.info("The order status with id {} change to {}.", orderUpdateRequest.getOrder(), orderStatusEntity.getStatus());
			final var newOrderEntity = this.orderRepository.save(orderEntity);

			final var sendEmailOrderUpdatedRequest = new SendEmailOrderUpdatedRequest();

			final var orderResponse = this.orderHelper.convertOrder(orderEntity);

			sendEmailOrderUpdatedRequest.setStatusOrder(orderStatusEntity.getStatus().getLabel());
			sendEmailOrderUpdatedRequest.setEmail(orderResponse.getClient().getEmail());
			sendEmailOrderUpdatedRequest.setNumberOrder(String.valueOf(orderResponse.getNumber()));
			sendEmailOrderUpdatedRequest.setId(orderResponse.getId());
			sendEmailOrderUpdatedRequest.setClientName(orderResponse.getClient().getName() + " " + orderResponse.getClient().getFirstLastname());
			if(orderResponse.getOrderShipping() != null)
				sendEmailOrderUpdatedRequest.setTrackingCode(orderResponse.getOrderShipping().getTrackingNumber());
			this.emailService.sendEmailOrderUpdated(sendEmailOrderUpdatedRequest);

			OrderServiceImplementation.log.info("Finished update the order with id {}. change to {}", orderUpdateRequest.getOrder(), orderStatusEntity.getStatus());
			return this.orderHelper.convertOrder(newOrderEntity);
		} catch (final Exception exception) {
			OrderServiceImplementation.log.info("The order could not update with the id {}", orderUpdateRequest.getOrder(), exception);
			throw new DataBaseException(Controller.PAYMENT_ORDER, DataBaseActionType.CREATE);
		}
	}

	@Override
	public GetResponse<OrderResponse> getAllOrder(final OrderFilterRequest orderFilterRequest, final DateFilterRequest dateFilter, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		OrderServiceImplementation.log.info("Starting searched of all order.");
		Page<OrderEntity> pageOrderEntity = null;
		this.dateHelper.setDateFilter(dateFilter);
		try {
			Integer number = null;
			String clientName = null;
			if (orderFilterRequest.getSearch() != null) {
				try {
					number = Integer.valueOf(orderFilterRequest.getSearch());
				} catch (final Exception e) {
					clientName = orderFilterRequest.getSearch();
				}
			}
			String orderStatus = null;
			if (orderFilterRequest.getOrderStatus() != null)
				orderStatus = orderFilterRequest.getOrderStatus().name();
			String deliveryMethod = null;
			if (orderFilterRequest.getDeliveryMethod() != null)
				deliveryMethod = orderFilterRequest.getDeliveryMethod().name();
			String paymentMethod = null;
			if (orderFilterRequest.getPaymentMethod() != null)
				paymentMethod = orderFilterRequest.getPaymentMethod().name();

			final var dateStart = dateFilter.getDateStart();
			final var dateEnd = dateFilter.getDateEnd();
			final var direction = paginationRequest.getTypeSort().name();
			final var pageable = this.paginationHelper.getPageable(paginationRequest);

			pageOrderEntity = this.orderRepository.findAll(null, number, clientName, orderStatus, deliveryMethod, paymentMethod, dateStart, dateEnd, direction, pageable);

		} catch (final Exception exception) {
			OrderServiceImplementation.log.error("The order could not been read.", exception);
			throw new DataBaseException(Controller.PAYMENT_ORDER, DataBaseActionType.READ);
		}
		final var listOrderResponse = pageOrderEntity.get().map(this.orderHelper::convertOrder).collect(Collectors.toList());

		listOrderResponse.sort(Comparator.comparing(OrderResponse::getNumber).reversed());

		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageOrderEntity);
		if (listOrderResponse.isEmpty()) {
			OrderServiceImplementation.log.error("The orders not found.");
			throw new NotFoundException(Controller.PAYMENT_ORDER, "all");
		}
		OrderServiceImplementation.log.info("Finished search the order.");
		return new GetResponse<>(listOrderResponse, paginationResponse);
	}

	@Override
	public GetResponse<OrderResponse> getAllOrderByClient(final UUID client, final OrderFilterRequest orderFilterRequest, final DateFilterRequest dateFilter, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		OrderServiceImplementation.log.info("Starting searched of all order with client {}.", client);
		Page<OrderEntity> pageOrderEntity = null;
		this.dateHelper.setDateFilter(dateFilter);
		try {
			Integer number = null;
			String clientName = null;
			if (orderFilterRequest.getSearch() != null) {
				try {
					number = Integer.getInteger(orderFilterRequest.getSearch());
				} catch (final Exception e) {
					clientName = orderFilterRequest.getSearch();
				}
			}
			String orderStatus = null;
			if (orderFilterRequest.getOrderStatus() != null)
				orderStatus = orderFilterRequest.getOrderStatus().name();
			String deliveryMethod = null;
			if (orderFilterRequest.getDeliveryMethod() != null)
				deliveryMethod = orderFilterRequest.getDeliveryMethod().name();
			String paymentMethod = null;
			if (orderFilterRequest.getPaymentMethod() != null)
				paymentMethod = orderFilterRequest.getPaymentMethod().name();
			final var dateStart = dateFilter.getDateStart();
			final var dateEnd = dateFilter.getDateEnd();
			final var direction = paginationRequest.getTypeSort().name();
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageOrderEntity = this.orderRepository.findAll(client, number, clientName, orderStatus, deliveryMethod, paymentMethod, dateStart, dateEnd, direction, pageable);
		} catch (final Exception exception) {
			OrderServiceImplementation.log.error("The order with the client {} could not been read.", client, exception);
			throw new DataBaseException(Controller.PAYMENT_ORDER, DataBaseActionType.READ, client.toString());
		}
		final var listOrderResponse = pageOrderEntity.get().map(this.orderHelper::convertOrder).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageOrderEntity);
		if (listOrderResponse.isEmpty()) {
			OrderServiceImplementation.log.error("The orders not found.");
			throw new NotFoundException(Controller.PAYMENT_ORDER, "all");
		}
		OrderServiceImplementation.log.info("Finished search the order with the client {}.", client);
		return new GetResponse<>(listOrderResponse, paginationResponse);
	}

	@Override
	public OrderResponse getOrderById(final UUID order) throws DataBaseException, NotFoundException {
		OrderServiceImplementation.log.info("Starting searched order with id {}.", order);
		Optional<OrderEntity> optionalOrderEntity;
		try {
			optionalOrderEntity = this.orderRepository.findById(order);
		} catch (final Exception exception) {
			OrderServiceImplementation.log.error("The order with the id {} could not been read.", order, exception);
			throw new DataBaseException(Controller.PAYMENT_ORDER, DataBaseActionType.READ, order.toString());
		}
		if (optionalOrderEntity.isEmpty()) {
			OrderServiceImplementation.log.error("The order with the id {} not found.", order);
			throw new NotFoundException(Controller.PAYMENT_ORDER, "id");
		}
		OrderServiceImplementation.log.info("Finished search the order with id {}.", order);
		return this.orderHelper.convertOrder(optionalOrderEntity.get());
	}

}
