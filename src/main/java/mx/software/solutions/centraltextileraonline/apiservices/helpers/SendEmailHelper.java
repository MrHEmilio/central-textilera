package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderCreatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;

@Slf4j
@Component
public class SendEmailHelper {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderHelper orderHelper;

    @Value("${central-textilera.billing.iva}")
    private BigDecimal iva;

    @Autowired
    private ColorRepository colorRepo;

    public void sendEmailOrderCreated(final UUID order) throws DataBaseException, NotFoundException {
        final var orderEntity = this.orderHelper.getOrderEntity(order);
        final var clientEntity = orderEntity.getClientEntity();
        var shippingPrice = BigDecimal.ZERO;
        final var shippingName = new StringBuilder();
        final var sellPrice = BigDecimal.valueOf(orderEntity.getOrderClothEntities()
                .stream()
                .mapToDouble(orderClothEntity -> orderClothEntity
                        .getSellPrice()
                        .doubleValue())
                .sum());
        var total = sellPrice;
        final var productsName = new StringBuilder();
        final var productsDetails = new StringBuilder();
        final var productsAmount = new StringBuilder();
        final var priceUnit = new StringBuilder();

        if (DeliveryMethod.SHIPPING.equals(orderEntity.getDeliveryMethod())) {
            total = sellPrice.add(shippingPrice);
            BigDecimal menosIva = orderEntity.getOrderShippingEntity().getPrice().multiply(this.iva).setScale(2, RoundingMode.HALF_UP);
            shippingPrice = orderEntity.getOrderShippingEntity().getPrice().subtract(menosIva).setScale(2, RoundingMode.HALF_UP);

            final var orderShippingEntity = orderEntity.getOrderShippingEntity();
            shippingName.append(orderShippingEntity.getStreetName());
            shippingName.append(' ');
            shippingName.append(orderShippingEntity.getNumExt());
            shippingName.append(", ");
            shippingName.append(orderShippingEntity.getZipCode());
            shippingName.append(' ');
            shippingName.append(orderShippingEntity.getSuburb());
            shippingName.append(", ");
            shippingName.append(orderShippingEntity.getMunicipality());
        }

        orderEntity.getOrderClothEntities().forEach(orderClothEntity -> {
            BigDecimal priceU;
            BigDecimal menosIva;
            productsName.append("<br />");
            productsName.append(orderClothEntity.getClothName());
            productsDetails.append("<br />");
            productsDetails.append(orderClothEntity.getColorName());

            productsAmount.append("<br />");
            productsAmount.append(orderClothEntity.getAmount());
            productsAmount.append(' ');
            productsAmount.append(orderClothEntity.getClothEntity().getSaleEntity().getAbbreviation());

            priceUnit.append("<br />");
            priceU = orderClothEntity.getSellPrice().divide(orderClothEntity.getAmount(), 2, RoundingMode.HALF_UP);
            menosIva = priceU.multiply(this.iva).setScale(2, RoundingMode.HALF_UP);
            priceUnit.append("$").append(priceU.subtract(menosIva).setScale(2, RoundingMode.HALF_UP));
        });

        orderEntity.getOrderSamplerEntities().forEach(orderSamplerEntity -> {
            productsName.append("<br />");
            productsName.append(orderSamplerEntity.getSamplerName());
            productsDetails.append("<br />");
            productsDetails.append("N/A");
            productsAmount.append("<br />");
            priceUnit.append("<br />");
            priceUnit.append(orderSamplerEntity.getSellPrice());
            priceUnit.append(' ');

        });

        SendEmailHelper.log.info("Starting send email to order created to {}.", clientEntity.getCredentialEntity().getEmail());

        final var sendEmailOrderCreatedRequest = new SendEmailOrderCreatedRequest();
        sendEmailOrderCreatedRequest.setId(orderEntity.getId());
        sendEmailOrderCreatedRequest.setEmail(clientEntity.getCredentialEntity().getEmail());
        sendEmailOrderCreatedRequest.setClientName(clientEntity.getName() + " " + clientEntity.getFirstLastname());
        sendEmailOrderCreatedRequest.setNumberOrder(String.valueOf(orderEntity.getNumber()));
        sendEmailOrderCreatedRequest.setStatusOrder(OrderStatus.REVISION.getLabel());
        sendEmailOrderCreatedRequest.setDeliveryMethod(orderEntity.getDeliveryMethod());
        sendEmailOrderCreatedRequest.setPaymentMethod(orderEntity.getPaymentMethod());
        sendEmailOrderCreatedRequest.setSubtotal(sellPrice);
        sendEmailOrderCreatedRequest.setShippingTotal(shippingPrice);
        sendEmailOrderCreatedRequest.setIva(total.multiply(this.iva).setScale(2, RoundingMode.HALF_UP));
        sendEmailOrderCreatedRequest.setTotal(orderEntity.getTotal());
        sendEmailOrderCreatedRequest.setShippingName(shippingName.toString());
        sendEmailOrderCreatedRequest.setProductsName(productsName.toString());
        sendEmailOrderCreatedRequest.setProductsDetail(productsDetails.toString());
        sendEmailOrderCreatedRequest.setProductsAmount(productsAmount.toString());
        sendEmailOrderCreatedRequest.setPriceUnit(priceUnit.toString());
        if (orderEntity.getOrderShippingEntity() != null)
            sendEmailOrderCreatedRequest.setTrackingCode(orderEntity.getOrderShippingEntity().getTrackingNumber());

        this.emailService.sendEmailOrderCreated(sendEmailOrderCreatedRequest);
        SendEmailHelper.log.info("Finished send email to order created to {}", clientEntity.getCredentialEntity().getEmail());
    }

}
