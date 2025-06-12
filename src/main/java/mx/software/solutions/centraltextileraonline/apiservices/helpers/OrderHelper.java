package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.*;
import java.util.stream.Collectors;

import mx.software.solutions.centraltextileraonline.apiservices.enumerations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderBillingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderProductResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderStatusResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderEntity;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderRepository;

@Slf4j
@Component
public class OrderHelper {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientHelper clientHelper;

    @Autowired
    private ClothHelper clothHelper;

    public OrderEntity getOrderEntity(final UUID order) throws DataBaseException, NotFoundException {
        Optional<OrderEntity> optionalOrderEntity = Optional.empty();
        OrderHelper.log.info("Starting searched the order with id {}.", order);
        try {
            optionalOrderEntity = this.orderRepository.findById(order);
        } catch (final Exception exception) {
            OrderHelper.log.error("The order with the id {} could not read.", order, exception);
            throw new DataBaseException(Controller.PAYMENT_ORDER, DataBaseActionType.READ, order.toString());
        }
        if (optionalOrderEntity.isEmpty()) {
            OrderHelper.log.info("The order with id {} not found.", order);
            throw new NotFoundException(Controller.PAYMENT_ORDER, "id");
        }
        final var orderEntity = optionalOrderEntity.get();
        OrderHelper.log.info("Finished search the order with the id {}.", order);
        return orderEntity;
    }

    public OrderResponse convertOrder(final OrderEntity orderEntity) {
        final var id = orderEntity.getId();
        final var clientResponse = this.clientHelper.convertClient(orderEntity.getClientEntity());
        final var number = orderEntity.getNumber();
        final var total = orderEntity.getTotal();
        final List<OrderProductResponse> listOrderProductResponse = new ArrayList<>();
        final var listClothResponseStructure = List.of(ClothResponseStructure.SALE);
        orderEntity.getOrderClothEntities().forEach(orderClothEntity -> {
            final var clothEntity = orderClothEntity.getClothEntity();
            final var clothResponse = this.clothHelper.convertCloth(clothEntity, listClothResponseStructure);
            final var orderProductResponse = new OrderProductResponse();
            orderProductResponse.setId(orderClothEntity.getId());
            orderProductResponse.setImage(clothResponse.getImage());
            orderProductResponse.setName(clothResponse.getName());
            orderProductResponse.setColor(orderClothEntity.getColorName());
            orderProductResponse.setSale(clothResponse.getSale().getName());
            orderProductResponse.setAmount(orderClothEntity.getAmount());
            orderProductResponse.setSellPrice(orderClothEntity.getSellPrice());
            orderProductResponse.setTotalSellPrice(orderClothEntity.getTotalSellPrice());
            orderProductResponse.setDiscount(orderClothEntity.getDiscount());
            listOrderProductResponse.add(orderProductResponse);
        });

        orderEntity.getOrderSamplerEntities().forEach(orderSamplerEntity -> {
            final var orderProductResponse = new OrderProductResponse();
            orderProductResponse.setId(orderSamplerEntity.getId());
            orderProductResponse.setImage(null);
            orderProductResponse.setName("Muestrario " + orderSamplerEntity.getSamplerName());
            orderProductResponse.setColor(null);
            orderProductResponse.setSale(null);
            orderProductResponse.setAmount(orderSamplerEntity.getAmount());
            orderProductResponse.setSellPrice(orderSamplerEntity.getSellPrice());
            orderProductResponse.setTotalSellPrice(orderSamplerEntity.getTotalSellPrice());
            orderProductResponse.setDiscount(orderSamplerEntity.getDiscount());
            listOrderProductResponse.add(orderProductResponse);
        });

        OrderShippingResponse orderShippingResponse = null;
        if (DeliveryMethod.SHIPPING.equals(orderEntity.getDeliveryMethod())) {
            final var orderShippingEntity = orderEntity.getOrderShippingEntity();
            final var idOrderShipping = orderShippingEntity.getId();
            final var streetName = orderShippingEntity.getStreetName();
            final var numExt = orderShippingEntity.getNumExt();
            final var numInt = orderShippingEntity.getNumInt();
            final var zipCode = orderShippingEntity.getZipCode();
            final var suburb = orderShippingEntity.getSuburb();
            final var municipality = orderShippingEntity.getMunicipality();
            final var state = orderShippingEntity.getState();
            final var city = orderShippingEntity.getCity();
            final var country = orderShippingEntity.getCountry();
            final var references = orderShippingEntity.getReferences();
            final var provider = orderShippingEntity.getProvider();
            final var serviceCode = orderShippingEntity.getServiceCode();
            final var serviceName = orderShippingEntity.getServiceName();
            final var price = orderShippingEntity.getPrice();
            final var date = orderShippingEntity.getDate();
            final var shippingMethod = orderShippingEntity.getShippingMethod();
            final var trackingNumber = orderShippingEntity.getTrackingNumber();
            final var trackingUrlProvider = orderShippingEntity.getTrackingUrlProvider();
            orderShippingResponse = new OrderShippingResponse(idOrderShipping, streetName, numExt, numInt, zipCode, suburb, municipality, state, city, country, references, provider, serviceCode, serviceName, price, date, shippingMethod, trackingNumber, trackingUrlProvider);
        }

        final var deliveryMethod = orderEntity.getDeliveryMethod().getLabel();
        final var paymentMethod = orderEntity.getPaymentMethod();

        OrderBillingResponse orderBillingResponse = null;
        if (orderEntity.getOrderBillingEntity() != null) {
            final var orderBillingEntity = orderEntity.getOrderBillingEntity();
            final var idOrderBilling = orderEntity.getOrderBillingEntity().getId();
            final var streetName = orderBillingEntity.getStreetName();
            final var numExt = orderBillingEntity.getNumExt();
            final var numInt = orderBillingEntity.getNumInt();
            final var zipCode = orderBillingEntity.getZipCode();
            final var suburb = orderBillingEntity.getSuburb();
            final var municipality = orderBillingEntity.getMunicipality();
            final var state = orderBillingEntity.getState();
            final var city = orderBillingEntity.getCity();
            final var country = orderBillingEntity.getCountry();
            final var rfc = orderBillingEntity.getRfc();
            final var companyName = orderBillingEntity.getRfc();
            final var fiscalRegimen = orderBillingEntity.getFiscalRegimen();
            orderBillingResponse = new OrderBillingResponse(idOrderBilling, streetName, numExt, numInt, zipCode, suburb, municipality, state, city, country, rfc, companyName, fiscalRegimen);
        }

        final List<OrderStatusResponse> listStatusHistory = new ArrayList<>();
        orderEntity.getOrderStatusEntities().forEach(orderStatusEntity -> {
            final var orderStatusResponse = new OrderStatusResponse();
            orderStatusResponse.setId(orderStatusEntity.getId());
            orderStatusResponse.setStatus(orderStatusEntity.getStatus().getLabel());
            orderStatusResponse.setDate(orderStatusEntity.getDate());
            listStatusHistory.add(orderStatusResponse);
        });

        return new OrderResponse(id, clientResponse, number, total, listOrderProductResponse, deliveryMethod, paymentMethod, listStatusHistory, orderShippingResponse, orderBillingResponse);
    }
}