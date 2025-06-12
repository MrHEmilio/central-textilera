package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceShippingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaymentClothVariantRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingTrackingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.dtos.MeasureDto;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ApiRestTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ShippingHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BoxRepository;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.PqtxCreateCollect;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.PqtxTokenRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.ReqPqtxCreateShipping;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.ReqPqtxQuotation;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.*;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.PqtxProgrammingHarvest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.PqtxResponseCreateGuide;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.PqtxResponseQuote;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.PqtxTokenResponse;
import mx.software.solutions.centraltextileraonline.apiservices.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("shippingPqtxService")
public class ShippingPqtxServiceImplementation implements ShippingService {
    @Autowired
    private ShippingHelper shippingHelper;

    @Autowired
    private BoxServiceImplementation calculateBox;

    @Autowired
    private OrderHelper orderHelper;

    @Autowired
    private BoxRepository boxRepository;

    @Value("${url.api.pqtx.rates}")
    private String urlApiPqtxRates;

    @Value("${url.api.pqtx.guides}")
    private String urlApiPqtxGuides;

    @Value("${url.api.pqtx.collect}")
    private String urlApiPqtxCollect;

    @Value("${url.api.pqtx.tokenGen}")
    private String urlApiPqtxToken;

    @Value("${url.api.pqtx.guiaPdf}")
    private String urlApiPqtxPdf;

    @Value("${token.pqtx.user}")
    private String userName;

    @Value("${token.pqtx.password}")
    private String password;

    @Value("${token.pqtx.pwsShipping}")
    private String pws;

    @Value("${token.pqtx.billClntId}")
    private String billClntId;

    @Value("${token.pqtx.tokenStatic}")
    private String tokenSt;

    @Value("${central-textilera.address.phone}")
    private String storePhone;

    @Value("${central-textilera.address.email}")
    private String storeEmail;

    @Value("${central-textilera.billing.iva}")
    private BigDecimal iva;

    @Value("${central-textilera.address.zip-code}")
    private String codeStore;

    @Value("${central-textilera.address.colonyName}")
    private String colony;

    @Value("${central-textilera.address.street-name}")
    private String streetName;

    @Value("${central-textilera.address.city}")
    private String city;

    @Value("${central-textilera.address.phone}")
    private String phone;

    @Override
    public List<ShippingResponse> calculatePriceShipping(CalculatePriceShippingRequest calculatePriceShippingRequest, List<ShippingResponse> listShippingResponse) throws RestTemplateException
    {
        if (calculatePriceShippingRequest.getCloths().isEmpty()) return listShippingResponse;

        ShippingPqtxServiceImplementation.log.info("Start calculated price shipping by PaquetExpress");

        List<MeasureDto> listMeasuresDtos = this.shippingHelper.getMeasureDto(calculatePriceShippingRequest.getCloths());

        Map<MeasureDto, Long> mapMeasureDto = listMeasuresDtos.stream()
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        final var clienAddrDest = new ClientAddr();
        clienAddrDest.setZipCode(calculatePriceShippingRequest.getAddress().getZipCode());
        clienAddrDest.setColonyName(calculatePriceShippingRequest.getAddress().getCity());

        final var shipmentDetails = new ShipmentDetail();

        List<Shipment> listShipments = mapMeasureDto.entrySet()
                .stream()
                .map(obj -> {
                    MeasureDto measureDto = obj.getKey();
                    Long quantity = obj.getValue();
                    var shipment = new Shipment();
                    shipment.setSequence(mapMeasureDto.getOrDefault(measureDto, quantity));
                    shipment.setQuantity(Integer.parseInt(String.valueOf(quantity)));
                    shipment.setHighShip(measureDto.getHeight().setScale(2, RoundingMode.HALF_UP));
                    shipment.setWidthShip(measureDto.getLength().setScale(2, RoundingMode.HALF_UP));
                    shipment.setLongShip(measureDto.getWidth().setScale(2, RoundingMode.HALF_UP));
                    shipment.setWeight(measureDto.getWeight().setScale(2, RoundingMode.HALF_UP));

                    shipment.setShpCode("2");
//                    if (shipment.getLongShip().compareTo(new BigDecimal(60)) <= 0)

//                    else
//                        shipment.setShpCode("26");
                    return shipment;
                }).collect(Collectors.toList());

        shipmentDetails.setShipments(listShipments);

        final var data = new Data();
        final var header = new Header();
        final var security = new Security();

        security.setUser(this.userName);
        security.setPassword(this.password);
        security.setToken(this.tokenSt);

        header.setSecurity(security);

        final var clientorigin = new ClientAddr();
        clientorigin.setColonyName(this.colony);
        clientorigin.setZipCode(this.codeStore);

        data.setClientAddrOrig(clientorigin);
        data.setClientAddrDest(clienAddrDest);

        data.setShipmentDetail(shipmentDetails);

        final var req = new Request();
        req.setData(data);

        final var body = new Body();
        body.setRequest(req);

        final var pqtxRequest = new ReqPqtxQuotation();
        pqtxRequest.setBody(body);
        pqtxRequest.setHeader(header);

        final var restTemplate = new RestTemplate();
        final var httpEntity = new HttpEntity<>(pqtxRequest);


        PqtxResponseQuote pqtxResponseQuote = new PqtxResponseQuote();
        try {
            // ShippingPqtxServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiPqtxRates, pqtxRequest.getBody().getRequest().getData().getShipmentDetail());
            pqtxResponseQuote = restTemplate.postForObject(this.urlApiPqtxRates, httpEntity, PqtxResponseQuote.class);
            if (pqtxResponseQuote != null)
                ShippingPqtxServiceImplementation.log.info("Finished consume service {} with this response {}.", this.urlApiPqtxRates, pqtxResponseQuote.getBody().getResponse().getData().getQuotations());
        } catch (final Exception exception) {
            ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxRates, pqtxRequest.getBody().getRequest().getData().getShipmentDetail(), exception);
            /*try {
                throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Quotations", exception.getLocalizedMessage());
            } catch (RestTemplateException e) {
                throw new RuntimeException(e.getLocalizedMessage(), e);
            }*/
        }

        if (pqtxResponseQuote != null){
            if(!pqtxResponseQuote.getBody().getResponse().isSuccess())
                return listShippingResponse;
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        pqtxResponseQuote
                .getBody()
                .getResponse()
                .getData()
                .getQuotations()
                .forEach(quote -> {
                    final var shippingResponse = new ShippingResponse();

                    shippingResponse.setShippingMethod(ShippingMethod.PQTX);
                    // Se está duplicando el RateId, se debe generar uno nuevo aleatorio
                    shippingResponse.setRateId(String.valueOf(UUID.randomUUID()));
                    shippingResponse.setProvider("PAQUETEXPRESS");
                    shippingResponse.setServiceCode(quote.getId());
                    shippingResponse.setServiceName(quote.getServiceName());
                    shippingResponse.setPrice(quote.getAmount().getTotalAmnt().setScale(0, RoundingMode.CEILING));
                    try {
                        shippingResponse.setDate(format.parse(quote.getPromiseDate()));
                    } catch (ParseException pe) {
                        ShippingPqtxServiceImplementation.log.error("Error when performing date parsing in Shipping PaquetExpress");
                    }
                    listShippingResponse.add(shippingResponse);
                });

        ShippingPqtxServiceImplementation.log.info("Finished calculate price shipping by PaquetExpress.");


        return listShippingResponse;
    }

    @Override
    public void createShipping(ShippingTrackingCreateRequest shippingTrackingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException, JsonProcessingException {

        ShippingPqtxServiceImplementation.log.info("Starting create tracking by PaquetExpress");

        var orderEntity = this.orderHelper.getOrderEntity(shippingTrackingCreateRequest.getOrder());

        this.shippingHelper.validateTrackingIdNotExist(orderEntity.getOrderShippingEntity());

        // Crear la dirección de Origen
        List<ShippingPqtxAddr> radGuiaAddrDTOList = new ArrayList<>();
        var addrOrigin = new ShippingPqtxAddr();
        addrOrigin.setAddrLin1("MEXICO");
        addrOrigin.setAddrLin3(city);
        addrOrigin.setAddrLin4(city);
        addrOrigin.setAddrLin5(colony);
        addrOrigin.setAddrLin6("CENTRO HISTÓRICO");
        addrOrigin.setZipCode(codeStore);
        addrOrigin.setStrtName(streetName);
        addrOrigin.setDrnr("110-C");
        addrOrigin.setPhno1(phone);
        addrOrigin.setClntName("CENTRAL TEXTILERA");
        addrOrigin.setEmail(storeEmail);
        addrOrigin.setContacto("CENTRAL TEXTILERA");
        addrOrigin.setAddrType("ORIGIN");
        radGuiaAddrDTOList.add(addrOrigin);

        var addrDestination = new ShippingPqtxAddr();

        addrDestination.setAddrLin1(orderEntity.getOrderShippingEntity().getCountry());
        addrDestination.setAddrLin3(orderEntity.getOrderShippingEntity().getState());
        addrDestination.setAddrLin4(orderEntity.getOrderShippingEntity().getMunicipality());
        addrDestination.setAddrLin5(orderEntity.getOrderShippingEntity().getCity());
        addrDestination.setAddrLin6(orderEntity.getOrderShippingEntity().getSuburb());
        addrDestination.setZipCode(orderEntity.getOrderShippingEntity().getZipCode());
        addrDestination.setStrtName(orderEntity.getOrderShippingEntity().getStreetName());
        addrDestination.setDrnr(orderEntity.getOrderShippingEntity().getNumExt());
        addrDestination.setPhno1(orderEntity.getClientEntity().getPhone());
        addrDestination.setClntName(orderEntity.getClientEntity().getName() + " " + orderEntity.getClientEntity().getFirstLastname());
        addrDestination.setEmail(orderEntity.getClientEntity().getCredentialEntity().getEmail());
        addrDestination.setContacto(orderEntity.getClientEntity().getName() + " " + orderEntity.getClientEntity().getFirstLastname() + " " + orderEntity.getClientEntity().getSecondLastname());
        addrDestination.setAddrType("DESTINATION");
        radGuiaAddrDTOList.add(addrDestination);

        List<PaymentClothVariantRequest> listMeasuresPackages = new ArrayList<>();
        for (OrderClothEntity orderClothEntity : orderEntity.getOrderClothEntities()) {
            PaymentClothVariantRequest cloth = new PaymentClothVariantRequest();
            cloth.setAmount(orderClothEntity.getAmount());
            cloth.setVariant(orderClothEntity.getClothEntity().getClothVariantEntities()
                    .get(orderEntity.getOrderClothEntities().indexOf(orderClothEntity)).getId());
            listMeasuresPackages.add(cloth);
        }

        Map<MeasureDto, Long> mapMeasureDto = this.shippingHelper.getMeasureDto(listMeasuresPackages).stream()
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        final var clothEntities = orderEntity.getOrderClothEntities();

        List<ShippingPqtxItems> radSrvcItemDTOList = mapMeasureDto.entrySet()
                .stream()
                .map(obj ->{
                    MeasureDto medida = obj.getKey();
                    Long quantity = obj.getValue();

                    var item = new ShippingPqtxItems();

                    item.setProductIdSAT(clothEntities.get(0)
                            .getClothEntity()
                            .getClothBillingEntity()
                            .getProductCode());

                    item.setWeight(String.valueOf(medida.getWeight().setScale(2, RoundingMode.HALF_UP)));
                    item.setVolL(String.valueOf(medida.getWidth().setScale(2,RoundingMode.HALF_UP)));
                    item.setVolW(String.valueOf(medida.getLength().setScale(2,RoundingMode.HALF_UP)));
                    item.setVolH(String.valueOf(medida.getHeight().setScale(2,RoundingMode.HALF_UP)));
                    item.setCont(clothEntities.get(0)
                            .getClothEntity()
                            .getClothBillingEntity()
                            .getProductLabel());
                    item.setQunt(String.valueOf(quantity));

                    if(medida.getWidth().compareTo(new BigDecimal(60))<=0){
                        item.setSrvcId("CAJA");
                    }else{
                        item.setSrvcId("ROLLOS");
                    }
                    return item;
                }).collect(Collectors.toList());

        //Recolección y entrega
        ShippingPqtxItemDto itemEAD = new ShippingPqtxItemDto();
        itemEAD.setSrvcId("EAD");
        itemEAD.setValue1("");
        ShippingPqtxItemDto itemRAD = new ShippingPqtxItemDto();
        itemRAD.setSrvcId("RAD");
        itemRAD.setValue1("");


        List<ShippingPqtxItemDto> listSrvcItemDTO = new ArrayList<>();
        listSrvcItemDTO.add(itemEAD);
        listSrvcItemDTO.add(itemRAD);

        var data = new ShippingPqtxData();
        data.setBillClntId(this.billClntId);
        data.setRadGuiaAddrDTOList(radGuiaAddrDTOList);
        data.setRadSrvcItemDTOList(radSrvcItemDTOList);
        data.setListSrvcItemDTO(listSrvcItemDTO);
        data.setTypeSrvcId(orderEntity.getOrderShippingEntity().getServiceCode());
        StringBuilder comments = new StringBuilder();

        orderEntity.getOrderClothEntities()
                .forEach(cloth -> comments.append(cloth.getClothEntity().getClothBillingEntity().getProductLabel()));

        data.setComt(String.valueOf(comments));

        ShippingPqtxRef objShippinRef = new ShippingPqtxRef();
        objShippinRef.setGrGuiaRefr(this.userName+" Orden: " + orderEntity.getNumber()
                + " Client: " + orderEntity.getClientEntity().getName()
                + " " + orderEntity.getClientEntity().getFirstLastname());

        data.setListRefs(List.of(objShippinRef));
        // Request de la petición
        var request = new ShippingPqtxRequest();
        request.setData(List.of(data));
        //Cuerpo de la petición
        var body = new ShippingPqtxBody();
        body.setRequest(request);
        //Credenciales
        final var security = new Security();
        security.setUser(this.userName);
        security.setPassword(this.pws);

        try {
            security.setToken(generateToken());
        } catch (Exception exception) {
            throw new RestTemplateException(ApiRestTemplate.PQTX, "Generated Token PQTX Error", exception.getMessage());
        }

        final var header = new ShippingPqtxReqHeader();
        header.setSecurity(security);

        final var createShipping = new ReqPqtxCreateShipping();
        createShipping.setHeader(header);
        createShipping.setBody(body);

        final var restTemplate = new RestTemplate();
        var responseGuide = new PqtxResponseCreateGuide();

        try {
            ShippingPqtxServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiPqtxGuides, new ArrayList<>(createShipping.getBody().getRequest().getData()));
            responseGuide = restTemplate.postForObject(this.urlApiPqtxGuides, new HttpEntity<>(createShipping), PqtxResponseCreateGuide.class);

            ShippingPqtxServiceImplementation.log.error("Finish consume service {} with this request {}.", this.urlApiPqtxGuides, new ArrayList<>(createShipping.getBody().getRequest().getData()));
        } catch (final Exception exception) {
            ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxGuides, responseGuide.getBody().getResponse().getData(), exception);
            try {
                throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Create Guides", exception.getMessage());
            } catch (RestTemplateException e) {
                throw new RuntimeException(e);
            }
        }

        if (!responseGuide.getBody().getResponse().getSuccess()) {
            ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxGuides, responseGuide.getBody().getResponse().getMessages());
            throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Create Guides", "Error");
        }

        orderEntity = this.shippingHelper.saveTrackingNumber(orderEntity, responseGuide.getBody().getResponse().getData());

        this.shippingHelper.saveTrackingUrlProvider(orderEntity, getFormattedUrl(orderEntity.getOrderShippingEntity().getTrackingNumber()));
        ShippingPqtxServiceImplementation.log.info("Finish create tracking by PaquetExpress");

        ShippingPqtxServiceImplementation.log.info("Starting the collection generation service by PaquetExpress");
        var collect = new PqtxCreateCollect();
        collect.setHeader(header);
        var bodyCollect = new PqtxBodyCollect();
        var dataCollect = new PqtxDataCollect();

        dataCollect.setNumbPack(String.valueOf(data.getRadSrvcItemDTOList().size()));

        LocalTime dateNow = LocalTime.now();
        LocalDate today = LocalDate.now();
        LocalTime beforeTwo = LocalTime.of(14, 0);
        LocalDateTime nowDateTime = LocalDateTime.of(today, dateNow);

        long collectDate;

        if (dateNow.isBefore(beforeTwo)) {
            collectDate = nowDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } else {
            LocalDate tomorrow = today.plusDays(1);
            LocalDateTime dateTomorrow = LocalDateTime.of(tomorrow, dateNow);
            collectDate = dateTomorrow.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        dataCollect.setPlanCollDate(collectDate);
        dataCollect.setHourFrom(0L);
        dataCollect.setHourTo(0);
        dataCollect.setGuiaNo(orderEntity.getOrderShippingEntity().getTrackingNumber());
        dataCollect.setRadGuiaAddrDTOList(List.of());
        var requestCollect = new PqtxRequestCollect();
        requestCollect.setData(List.of(dataCollect));

        bodyCollect.setRequest(requestCollect);

        collect.setBody(bodyCollect);
        var responseCollect = new PqtxProgrammingHarvest();
        try {
            ShippingPqtxServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiPqtxCollect, collect.getBody().getRequest());
            responseCollect = restTemplate.postForObject(this.urlApiPqtxCollect, new HttpEntity<>(collect), PqtxProgrammingHarvest.class);

            ShippingPqtxServiceImplementation.log.info("Finish consume service {} with this request {}.", this.urlApiPqtxCollect, collect.getBody().getRequest());
        } catch (final Exception exception) {
            ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxCollect, collect.getBody().getRequest(), exception);
            try {
                throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Create collection", exception.getMessage());
            } catch (RestTemplateException e) {
                throw new RuntimeException(e);
            }
        }
        if (!responseCollect.getBody().getResponse().getSuccess()) {
            ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxCollect, responseCollect.getBody().getResponse().getMessages().get(0).getDescription());
            throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Create collection ", "Error");
        }

        ShippingPqtxServiceImplementation.log.info("Finish the collection generation service by PaquetExpress Collection: -> " + responseGuide.getBody().getResponse().getSuccess() + " :: " + responseGuide.getBody().getResponse().getData());

    }

    private String generateToken() throws RestTemplateException {

        PqtxTokenRequest tokenReq = new PqtxTokenRequest();
        PqtxTokenBody tokenBody = new PqtxTokenBody();
        PqtxTokenSecurity tokenSecurity = new PqtxTokenSecurity();
        tokenSecurity.setUser(this.userName);
        tokenSecurity.setPassword(this.pws);
        tokenBody.setSecurity(tokenSecurity);
        tokenReq.setHeader(tokenBody);

        var resposeToken = new PqtxTokenResponse();

        final var restTemplate = new RestTemplate();

        try {
            ShippingPqtxServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiPqtxToken, tokenReq.getHeader().getSecurity().getUser());
            resposeToken = restTemplate.postForObject(this.urlApiPqtxToken, new HttpEntity<>(tokenReq), PqtxTokenResponse.class);
            ShippingPqtxServiceImplementation.log.info("Finish consume service {} with this request {}.", this.urlApiPqtxToken, tokenReq.getHeader().getSecurity().getUser());
        } catch (final Exception exception) {
            ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxToken, resposeToken.getBody().getResponse().isSuccess()
                    + " :: " + resposeToken.getBody().getResponse().getMessages(), exception);
        }
        if(resposeToken != null){
            if(resposeToken.getBody() != null){
                if(resposeToken.getBody().getResponse() != null){
                    if (!resposeToken.getBody().getResponse().isSuccess()) {
                        ShippingPqtxServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiPqtxToken, resposeToken.getBody().getResponse().getMessages());
                        throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Generate Token ", "Error");
                    }
                }else
                    throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Generate Token ", "Error");
            }else
                throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Generate Token ", "Error");
        }else
            throw new RestTemplateException(ApiRestTemplate.PQTX, "Shippments PaquetExpress Generate Token ", "Error");


        return resposeToken.getBody().getResponse().getData().getToken();
    }

    private String getFormattedUrl(String parameter) {
        return UriComponentsBuilder
                .fromUriString(urlApiPqtxPdf)
                .buildAndExpand(parameter)
                .toUriString();
    }

}

@lombok.Data
class BoxDto {
    private BoxEntity boxEntity;
    private BigDecimal volumen = BigDecimal.ZERO;
}