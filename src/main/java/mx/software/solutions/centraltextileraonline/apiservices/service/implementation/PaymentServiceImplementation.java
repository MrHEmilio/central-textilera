package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.util.*;

import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.*;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderRepository;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.Notification;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.ResponsePayment.GetOrderMP;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.ResponsePayment.GetPaymentMP;
import mx.software.solutions.centraltextileraonline.apiservices.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.AddressRequest;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceReceiverAddressRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceShipmentsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CheckoutMercadoPagoCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaymentClothSamplerRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaymentClothVariantRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CalculatePriceResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CheckoutResponse;
import mx.software.solutions.centraltextileraonline.apiservices.dtos.PriceDto;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.CheckoutException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.InventoryException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClothHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothSamplerRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothVariantRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.PaymentService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class PaymentServiceImplementation implements PaymentService {

    @Autowired
    private ClothVariantRepository clothVariantRepository;

    @Autowired
    private ClothSamplerRepository clothSamplerRepository;

    @Autowired
    private ClothHelper clothHelper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderJsonComponent orderJsonComponent;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MessageLangHelper messageLangHelper;

    @Value("${token.mercado-pago.access-token}")
    private String accessTokenMercadoPago;

    @Value("${url.checkout.mercado-pago.success}")
    private String urlCheckoutMercadoPagoSuccess;

    @Value("${url.checkout.mercado-pago.failure}")
    private String urlCheckoutMercadoPagoFailure;

    @Value("${url.checkout.mercado-pago.pending}")
    private String urlCheckoutMercadoPagoPending;

    @Value("${url.checkout.mercado-pago.notification}")
    private String urlNotifications;

    @Value("${url.api.mercado-pago.paymentUrl}")
    private String urlPayment;

    private String urlOrders =  "https://api.mercadopago.com/merchant_orders/";
    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders httpHeaders = new HttpHeaders();
    private HttpEntity httpEntity;

    @Override
    public List<CalculatePriceResponse> calculatePriceProduct(final List<PaymentClothVariantRequest> listPaymentClothVariantRequest, final List<PaymentClothSamplerRequest> listPaymentClothSamplerRequest) {
        PaymentServiceImplementation.log.info("Starting calculated price product.");
        final Map<UUID, BigDecimal> mapClothIdAmount = new HashMap<>();
        listPaymentClothVariantRequest.forEach(paymentClothVariantRequest -> {
            final var optionalClothVariantEntity = this.clothVariantRepository.findById(paymentClothVariantRequest.getVariant());
            if (optionalClothVariantEntity.isPresent()) {
                final var clothEntity = optionalClothVariantEntity.get().getClothEntity();
                final var amount = mapClothIdAmount.get(clothEntity.getId());
                if (amount == null)
                    mapClothIdAmount.put(clothEntity.getId(), paymentClothVariantRequest.getAmount());
                else
                    mapClothIdAmount.put(clothEntity.getId(), paymentClothVariantRequest.getAmount().add(amount));
            }
        });

        final List<CalculatePriceResponse> listCalculatePriceResponse = new ArrayList<>();
        listPaymentClothVariantRequest.forEach(paymentClothVariantRequest -> {
            final var optionalClothVariantEntity = this.clothVariantRepository.findById(paymentClothVariantRequest.getVariant());
            if (optionalClothVariantEntity.isPresent()) {
                final var clothEntity = optionalClothVariantEntity.get().getClothEntity();
                final var amountTotalByCloth = mapClothIdAmount.get(clothEntity.getId());
                final var priceDto = new PriceDto();
                clothEntity.getClothPriceEntities().forEach(clothPriceEntity -> {
                    if (clothPriceEntity.getOrder() == 1)
                        priceDto.setSellPriceClothNormal(clothPriceEntity.getPrice());
                    if (amountTotalByCloth.intValue() >= clothPriceEntity.getFirstAmountRange() && clothPriceEntity.getLastAmountRange() != null
                            && amountTotalByCloth.intValue() <= clothPriceEntity.getLastAmountRange())
                        priceDto.setSellPriceCloth(clothPriceEntity.getPrice());
                    if (priceDto.getSellPriceCloth().equals(BigDecimal.ZERO) && clothPriceEntity.getLastAmountRange() == null)
                        priceDto.setSellPriceCloth(clothPriceEntity.getPrice());
                });

                final var product = paymentClothVariantRequest.getVariant();
                final var amount = paymentClothVariantRequest.getAmount();
                final var sellPrice = priceDto.getSellPriceCloth();
                final var totalSellPrice = priceDto.getSellPriceCloth().multiply(amount);
                final var priceNormal = priceDto.getSellPriceClothNormal();
                final var totalSellPriceNormal = priceDto.getSellPriceClothNormal().multiply(amount);
                final var discount = totalSellPriceNormal.subtract(totalSellPrice);

                listCalculatePriceResponse.add(new CalculatePriceResponse(product, amount, sellPrice, totalSellPrice, priceNormal, totalSellPriceNormal, discount));
            }
        });

        listPaymentClothSamplerRequest.forEach(paymentClothSamplerRequest -> {
            final var optionalClothSamplerEntity = this.clothSamplerRepository.findById(paymentClothSamplerRequest.getSampler());
            if (optionalClothSamplerEntity.isPresent()) {
                final var clothEntity = optionalClothSamplerEntity.get().getClothEntity();
                final var clothSamplerEntity = clothEntity.getClothSamplerEntity();
                final var product = paymentClothSamplerRequest.getSampler();
                final var amount = paymentClothSamplerRequest.getAmount();
                final var sellPrice = clothSamplerEntity.getPrice();
                final var totalSellPrice = clothSamplerEntity.getPrice().multiply(amount);
                final var priceNormal = clothSamplerEntity.getPrice();
                final var totalSellPriceNormal = clothSamplerEntity.getPrice().multiply(amount);
                final var discount = totalSellPriceNormal.subtract(totalSellPrice);
                listCalculatePriceResponse.add(new CalculatePriceResponse(product, amount, sellPrice, totalSellPrice, priceNormal, totalSellPriceNormal, discount));
            }
        });
        PaymentServiceImplementation.log.info("Finished calculate price product.");
        return listCalculatePriceResponse;
    }

    @Override
    public List<CalculatePriceResponse> calculatePriceOrder(OrderEntity orderEntity) {

        var orderClothEntities = orderEntity.getOrderClothEntities();

        final List<CalculatePriceResponse> listCalculatePriceResponse = new ArrayList<>();

        orderClothEntities.forEach(orderClothEntity -> {

            final var product = orderClothEntity.getClothEntity().getId();
            final var amount = orderClothEntity.getAmount();
            final var sellPrice = orderClothEntity.getSellPrice();
            final var totalSellPrice = orderClothEntity.getTotalSellPrice().multiply(amount);
            final var priceNormal = orderClothEntity.getSellPrice();
            final var totalSellPriceNormal = orderClothEntity.getTotalSellPrice().multiply(amount);
            final var discount = totalSellPriceNormal.subtract(totalSellPrice);

            listCalculatePriceResponse.add(new CalculatePriceResponse(product, amount, sellPrice, totalSellPrice, priceNormal, totalSellPriceNormal, discount));

        });
        return listCalculatePriceResponse;
    }

    @Override
    public boolean calculateInventoryProduct(final List<PaymentClothVariantRequest> listPaymentClothVariantRequest, final List<PaymentClothSamplerRequest> listPaymentClothSamplerRequest) throws InventoryException {
        final var exceptionThrow = new ExceptionThrow();

        listPaymentClothVariantRequest.forEach(paymentClothVariantRequest -> {
            final var optionalClothVariantEntity = this.clothVariantRepository.findById(paymentClothVariantRequest.getVariant());
            if (optionalClothVariantEntity.isPresent()) {
                final var clothVariantAmount = optionalClothVariantEntity.get().getAmount();
                if (paymentClothVariantRequest.getAmount().compareTo(clothVariantAmount) == 1)
                    exceptionThrow.setThrowInventoryException(true);
            }
        });

        listPaymentClothSamplerRequest.forEach(paymentClothSamplerRequest -> {
            final var optionalClothSamplerEntity = this.clothSamplerRepository.findById(paymentClothSamplerRequest.getSampler());
            if (optionalClothSamplerEntity.isPresent()) {
                final var clothSamplerAmount = optionalClothSamplerEntity.get().getAmount();
                if (paymentClothSamplerRequest.getAmount().compareTo(clothSamplerAmount) == 1)
                    exceptionThrow.setThrowInventoryException(true);
            }
        });

        if (exceptionThrow.isThrowInventoryException())
            throw new InventoryException();

        return true;
    }

    @Override
    public CheckoutResponse createCheckoutMercadoPago(final CheckoutMercadoPagoCreateRequest checkoutMercadoPagoCreate) throws CheckoutException, DataBaseException, NotFoundException {
        PaymentServiceImplementation.log.info("Starting created checkout MercadoPago.");
        MercadoPagoConfig.setAccessToken(this.accessTokenMercadoPago);

        final var listCalculatePriceResponse = this.calculatePriceProduct(checkoutMercadoPagoCreate.getCloths(), checkoutMercadoPagoCreate.getSamplers());

        final List<PreferenceItemRequest> listPrefenceItemRequests = new ArrayList<>();

        listCalculatePriceResponse.forEach(calculatePriceResponse -> {
            final var optionalClothVariantEntity = this.clothVariantRepository.findById(calculatePriceResponse.getProduct());
            if (optionalClothVariantEntity.isPresent()) {
                final var clothResponse = this.clothHelper.convertCloth(optionalClothVariantEntity.get().getClothEntity(), null);
                final var preferenceItemRequestBuilder = PreferenceItemRequest.builder();
                preferenceItemRequestBuilder.id(clothResponse.getId().toString());
                preferenceItemRequestBuilder.title(clothResponse.getName());
                preferenceItemRequestBuilder.pictureUrl(clothResponse.getImage());
                preferenceItemRequestBuilder.description(clothResponse.getMainDescription());
                preferenceItemRequestBuilder.categoryId(calculatePriceResponse.getProduct().toString());
                preferenceItemRequestBuilder.quantity(calculatePriceResponse.getAmount().intValue());
                preferenceItemRequestBuilder.unitPrice(calculatePriceResponse.getSellPrice());
                preferenceItemRequestBuilder.currencyId("MXN");
                listPrefenceItemRequests.add(preferenceItemRequestBuilder.build());
            }

            final var optionalClothSamplerEntity = this.clothSamplerRepository.findById(calculatePriceResponse.getProduct());
            if (optionalClothSamplerEntity.isPresent()) {
                final var clothResponse = this.clothHelper.convertCloth(optionalClothSamplerEntity.get().getClothEntity(), Arrays.asList(ClothResponseStructure.SAMPLER));
                final var preferenceItemRequestBuilder = PreferenceItemRequest.builder();
                preferenceItemRequestBuilder.id(clothResponse.getId().toString());
                preferenceItemRequestBuilder.title("Muestrario " + clothResponse.getName());
                preferenceItemRequestBuilder.description(clothResponse.getSampler().getDescription());
                preferenceItemRequestBuilder.categoryId(calculatePriceResponse.getProduct().toString());
                preferenceItemRequestBuilder.quantity(calculatePriceResponse.getAmount().intValue());
                preferenceItemRequestBuilder.unitPrice(calculatePriceResponse.getSellPrice());
                preferenceItemRequestBuilder.currencyId("MXN");
                listPrefenceItemRequests.add(preferenceItemRequestBuilder.build());
            }
        });

        final var clientCreateWithoutAccountRequest = checkoutMercadoPagoCreate.getClient();
        final var preferencePayerRequestBuilder = PreferencePayerRequest.builder();
        preferencePayerRequestBuilder.name(clientCreateWithoutAccountRequest.getName());
        preferencePayerRequestBuilder.surname(clientCreateWithoutAccountRequest.getFirstLastname());
        preferencePayerRequestBuilder.email(clientCreateWithoutAccountRequest.getEmail());
        if (clientCreateWithoutAccountRequest.getPhone() != null && !clientCreateWithoutAccountRequest.getPhone().isBlank())
            preferencePayerRequestBuilder.phone(PhoneRequest.builder().areaCode("52").number(clientCreateWithoutAccountRequest.getPhone()).build());

        final var preferenceBackUrlsRequestBuilder = PreferenceBackUrlsRequest.builder()
                .success(this.urlCheckoutMercadoPagoSuccess)
                .failure(this.urlCheckoutMercadoPagoSuccess)
                .pending(this.urlCheckoutMercadoPagoSuccess)
                .build();

        final var preferencePaymentMethodsRequestBuilder = PreferencePaymentMethodsRequest.builder()
                .installments(12)
                .build();

        final var preferenceRequestBuilder = PreferenceRequest.builder()
                .items(listPrefenceItemRequests)
                .payer(preferencePayerRequestBuilder.build())
                .autoReturn("approved")
                .backUrls(preferenceBackUrlsRequestBuilder)
                .notificationUrl(this.urlNotifications)
                .paymentMethods(preferencePaymentMethodsRequestBuilder)
                .statementDescriptor("Central-Textilera");


        if (checkoutMercadoPagoCreate.getClientAddress() != null) {
            final var clientAddressCreateWithoutAccount = checkoutMercadoPagoCreate.getClientAddress();
            final var addressRequestBuilder = AddressRequest.builder()
                    .streetName(clientAddressCreateWithoutAccount.getStreetName())
                    .streetNumber(clientAddressCreateWithoutAccount.getNumExt())
                    .zipCode(clientAddressCreateWithoutAccount.getZipCode());

            preferencePayerRequestBuilder.address(addressRequestBuilder.build());

            final var preferenceShipmentsRequestBuilder = PreferenceShipmentsRequest.builder();
            preferenceShipmentsRequestBuilder.mode("not_specified");
            preferenceShipmentsRequestBuilder.cost(checkoutMercadoPagoCreate.getShippingPrice());
            final var preferenceReceiverAddressRequestBuilder = PreferenceReceiverAddressRequest.builder();
            preferenceReceiverAddressRequestBuilder.countryName("Mx");
            preferenceReceiverAddressRequestBuilder.stateName(clientAddressCreateWithoutAccount.getState());
            preferenceReceiverAddressRequestBuilder.cityName(clientAddressCreateWithoutAccount.getCountry());
            preferenceReceiverAddressRequestBuilder.streetName(clientAddressCreateWithoutAccount.getStreetName());
            preferenceReceiverAddressRequestBuilder.streetNumber(clientAddressCreateWithoutAccount.getNumExt());
            preferenceReceiverAddressRequestBuilder.zipCode(clientAddressCreateWithoutAccount.getZipCode());
            preferenceShipmentsRequestBuilder.receiverAddress(preferenceReceiverAddressRequestBuilder.build());

            preferenceRequestBuilder.payer(preferencePayerRequestBuilder.build());
            preferenceRequestBuilder.shipments(preferenceShipmentsRequestBuilder.build());
        }

        Preference preference = null;

        try {
            final var preferenceClient = new PreferenceClient();
            preference = preferenceClient.create(preferenceRequestBuilder.build());

        } catch (MPException | MPApiException exception) {
            PaymentServiceImplementation.log.error("Could not create checkout to mercado pago.", exception);
            throw new CheckoutException();
        }
        PaymentServiceImplementation.log.info("Finished create checkout MercadoPago.");

        return new CheckoutResponse(preference.getId());
    }

    public void checkMP(Notification notificaMP) throws MPException, MPApiException {

        PaymentServiceImplementation.log.info("Starting to receive notifications from MercadoPago");

        httpHeaders.setBearerAuth(this.accessTokenMercadoPago);
        httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<GetPaymentMP> resp = null;

        PaymentServiceImplementation.log.info("Starting the search for the payment received in the MercadoPago notification");
        try {
            resp = restTemplate.exchange(UriComponentsBuilder.fromUriString(urlPayment)
                            .buildAndExpand(notificaMP.getData().getId()).toString(),
                    HttpMethod.GET, httpEntity, GetPaymentMP.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                PaymentServiceImplementation.log.error("Payment not found {} ", notificaMP.getData().getId());
            }
        } catch (Exception ex) {
            PaymentServiceImplementation.log.error("An Exception occurred while searching for the payment ID {} ", ex.getMessage());
        }

        if (resp != null) {
            PaymentNotification statusPayment = PaymentNotification.fromStatus(resp.getBody().getStatus());
            String preferenceId = searchOrder(resp.getBody().getOrder().getId());
            HelperJson orderUpdate = this.orderJsonComponent.searchPreference(preferenceId);
            try {
                switch (statusPayment) {
                    case APPROVED:
                        if(orderUpdate != null ){
                            orderUpdate.getOrderCreateRequest().setPaymentId(notificaMP.getData().getId());
                            this.orderService.createOrder(orderUpdate.getOrderCreateRequest(),orderUpdate.getIdUser());
                            this.messageLangHelper.getMessageLang(Controller.PAYMENT_ORDER, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
                            this.orderJsonComponent.delOrderJson(orderUpdate);
                        }
                        PaymentServiceImplementation.log.info("Finishing the storage of the order, which was updated with the arrival of the MP notification");
                        break;
                    case REJECTED:
                        this.orderJsonComponent.delOrderJson(orderUpdate);
                        PaymentServiceImplementation.log.warn("Order registration with IdPago number was removed from JSONTemp file because payment was rejected");
                        break;
                    case PENDING:
                    case CHARGEDBACK:
                    case INMEDIATION:
                    case AUTHORIZED:
                    case CANCELLED:
                    case INPROCESS:
                    case REFUNDED:
                        PaymentServiceImplementation.log.warn("An uncontrolled state occurred, but was stored for review {}.", statusPayment);
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException iar) {
                PaymentServiceImplementation.log.trace(iar.getMessage());
            } catch (NotFoundException | DataBaseException e) {
                PaymentServiceImplementation.log.trace(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        PaymentServiceImplementation.log.info("Completing the search for the payment received in the MercadoPago notification ");
    }

    public String searchOrder(String orderId){
        //Consultar la orden en MP
        httpHeaders.setBearerAuth(this.accessTokenMercadoPago);
        httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<GetOrderMP> resp = null;

        PaymentServiceImplementation.log.info("Starting the search for the order received in the Payment notification");
        try {
            resp = restTemplate.exchange(urlOrders+orderId,
                    HttpMethod.GET, httpEntity, GetOrderMP.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                PaymentServiceImplementation.log.error("OrderId not found {} ", orderId);
        } catch (Exception ex) {
            PaymentServiceImplementation.log.error("An Exception occurred while searching for the orderId {} ", orderId);
        }
        return resp.getBody().getPreference_id();
    }

}

@Data
class ExceptionThrow {
    private boolean throwInventoryException = false;
}