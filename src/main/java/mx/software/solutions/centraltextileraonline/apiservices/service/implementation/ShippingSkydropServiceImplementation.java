package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import mx.software.solutions.centraltextileraonline.apiservices.dtos.MeasureDto;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.IncludedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceShippingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingTrackingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ApiRestTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ShippingHelper;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.AddressRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.LabelCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.ParcelRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.ShipmentsRequest;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.LabelCreateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.ShippmentResponse;
import mx.software.solutions.centraltextileraonline.apiservices.service.ShippingService;

@Slf4j
@Service
@Qualifier("shippingSkydropService")
public class ShippingSkydropServiceImplementation implements ShippingService {

    @Autowired
    private ShippingHelper shippingHelper;

    @Autowired
    private OrderHelper orderHelper;

    @Value("${url.api.skydrop.shipments}")
    private String urlApiSkydropxShipments;

    @Value("${url.api.skydrop.labels}")
    private String urlApiSkydropxLabels;

    @Value("${token.skydrop.token}")
    private String tokenSkydropx;

    @Value("${central-textilera.address.zip-code}")
    private String storeZipCode;

    @Value("${central-textilera.address.street-name}")
    private String storeStreetName;

    @Value("${central-textilera.address.phone}")
    private String storePhone;

    @Value("${central-textilera.address.email}")
    private String storeEmail;

    @Value("${central-textilera.billing.iva}")
    private BigDecimal iva;


    @Override
    public List<ShippingResponse> calculatePriceShipping(final CalculatePriceShippingRequest calculatePriceShippingRequest, final List<ShippingResponse> listShippingResponse) throws RestTemplateException {
        if (calculatePriceShippingRequest.getCloths().isEmpty())
            return listShippingResponse;

        ShippingSkydropServiceImplementation.log.info("Starting calculated price shipping by SKYDROP.");

        final List<MeasureDto> listMeasuresDtos = this.shippingHelper.getMeasureDto(calculatePriceShippingRequest.getCloths());
        // Si hay una medida de que rebase los 60 para fedex
        boolean noFedex = listMeasuresDtos.stream()
                .anyMatch(item -> item.getWidth().compareTo(new BigDecimal("60")) > 0);

        //Almacenamos las medidas
        final var addressFromRequest = new AddressRequest();
        addressFromRequest.setCountry("MX");
        addressFromRequest.setZip(this.storeZipCode);
        addressFromRequest.setProvince("Ciudad de Mexico");
        addressFromRequest.setCity("Cuauhtemoc");
        addressFromRequest.setAddress2("Centro Histórico");
        addressFromRequest.setAddress1(this.storeStreetName);
        addressFromRequest.setName("Central Textilera");
        addressFromRequest.setCompany("Central Textilera");
        addressFromRequest.setPhone(this.storePhone);
        addressFromRequest.setEmail(this.storeEmail);
        addressFromRequest.setReference("Comercio");
        addressFromRequest.setContents("Contents");

        final var addressToRequest = new AddressRequest();
        addressToRequest.setProvince(calculatePriceShippingRequest.getAddress().getCity());
        addressToRequest.setCity(calculatePriceShippingRequest.getAddress().getCity());
        addressToRequest.setName(calculatePriceShippingRequest.getClientName());
        addressToRequest.setZip(calculatePriceShippingRequest.getAddress().getZipCode());
        addressToRequest.setCountry("MX");
        addressToRequest.setAddress1(calculatePriceShippingRequest.getAddress().getStreetName());
        addressToRequest.setCompany("-");
        addressToRequest.setAddress2(calculatePriceShippingRequest.getAddress().getNumExt());
        addressToRequest.setPhone(calculatePriceShippingRequest.getPhone());
        addressToRequest.setEmail(calculatePriceShippingRequest.getEmail());
        addressToRequest.setReference("-");
        addressToRequest.setContents("-");

        if (calculatePriceShippingRequest.getAddress().getReferences() != null) {
            addressToRequest.setReference(calculatePriceShippingRequest.getAddress().getReferences());
            addressToRequest.setContents(calculatePriceShippingRequest.getAddress().getReferences());
        }

        final var shipmentsRequest = new ShipmentsRequest();
        shipmentsRequest.setAddressFrom(addressFromRequest);

        List<ParcelRequest> listParcel = listMeasuresDtos.stream()
                .map(measureDto -> {
                    ParcelRequest parcelRequest = new ParcelRequest();
                    parcelRequest.setWeight(measureDto.getWeight().setScale(0, RoundingMode.CEILING));
                    parcelRequest.setDistanceUnit("CM");
                    parcelRequest.setMassUnit("KG");
                    parcelRequest.setHeight(measureDto.getHeight().setScale(0, RoundingMode.CEILING));
                    parcelRequest.setLength(measureDto.getLength().setScale(0, RoundingMode.CEILING));
                    parcelRequest.setWidth(measureDto.getWidth().setScale(0, RoundingMode.CEILING));
                    return parcelRequest;
                }).collect(Collectors.toList());

        shipmentsRequest.setParcels(listParcel);
        shipmentsRequest.setAddressTo(addressToRequest);
        shipmentsRequest.setConsignmentNoteClassCode("14111500");
        shipmentsRequest.setConsignmentNodePackagingCode("4G");

        final var restTemplate = new RestTemplate();
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(this.tokenSkydropx);
        final var httpEntity = new HttpEntity<>(shipmentsRequest, httpHeaders);

        ShippmentResponse shipmentResponses;
        try {
            ShippingSkydropServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiSkydropxShipments, shipmentsRequest);
            shipmentResponses = restTemplate.postForObject(this.urlApiSkydropxShipments, httpEntity, ShippmentResponse.class);
            ShippingSkydropServiceImplementation.log.info("Finished consume service {} with this response {}.", this.urlApiSkydropxShipments, shipmentResponses);
        } catch (final Exception exception) {
            ShippingSkydropServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiSkydropxShipments, shipmentsRequest, exception);
            try {
                throw new RestTemplateException(ApiRestTemplate.SKYDROPX, "shippments", exception.getMessage());
            } catch (RestTemplateException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        if (shipmentResponses != null && shipmentResponses.getIncluded() != null) {
            for (IncludedResponse includedElement : shipmentResponses.getIncluded()) {
                if ("rates".equals(includedElement.getType())) {

                    final var attributes = includedElement.getAttributes();
                    List<String> notProviders = new ArrayList<>(Arrays.asList("SENDEX", "NINETYNINEMINUTES", "REDPACK", "CARSSA", "QUIKEN", "ESTAFETA"));

                    if (notProviders.contains(attributes.getProvider()) || (noFedex && attributes.getProvider().equalsIgnoreCase("FeDex"))) continue;

                    final var calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, attributes.getDays());

                    final var shippingResponse = new ShippingResponse();
                    shippingResponse.setShippingMethod(ShippingMethod.SKYDROP);
                    shippingResponse.setRateId(includedElement.getId());
                    shippingResponse.setProvider(attributes.getProvider());
                    shippingResponse.setServiceCode(attributes.getServiceLevelCode());
                    shippingResponse.setServiceName(attributes.getServiceLevelName());
                    shippingResponse.setPrice(attributes.getTotalPricing());
                    shippingResponse.setDate(calendar.getTime());
                    listShippingResponse.add(shippingResponse);
                }
            }
        }


        ShippingSkydropServiceImplementation.log.info("Finished calculate price shipping by SKYDROP.");
        return listShippingResponse;
    }

    @Override
    public void createShipping(final ShippingTrackingCreateRequest shippingTrackingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException {
        ShippingSkydropServiceImplementation.log.info("Starting create tracking by SKYDROP.");
        var orderEntity = this.orderHelper.getOrderEntity(shippingTrackingCreateRequest.getOrder());
        this.shippingHelper.validateTrackingIdNotExist(orderEntity.getOrderShippingEntity());

        ShippingSkydropServiceImplementation.log.info("Strating created shipping by SKYDROP.");
        final var labelCreateRequest = new LabelCreateRequest();
        labelCreateRequest.setLabelFormat("pdf");
        labelCreateRequest.setRateId(Integer.parseInt(orderEntity.getOrderShippingEntity().getRateId()));

        final var restTemplate = new RestTemplate();
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(this.tokenSkydropx);
        final var httpEntity = new HttpEntity<>(labelCreateRequest, httpHeaders);
        var labelCreateResponse = new LabelCreateResponse();

        try {
            ShippingSkydropServiceImplementation.log.info("Starting consume service {} with this request {}.", this.urlApiSkydropxLabels, labelCreateRequest);
            labelCreateResponse = restTemplate.postForObject(this.urlApiSkydropxLabels, httpEntity, LabelCreateResponse.class);
            ShippingSkydropServiceImplementation.log.info("Finished consume service {} with this response {}.", this.urlApiSkydropxLabels, labelCreateRequest);

        } catch (final Exception exception) {
            ShippingSkydropServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiSkydropxLabels, labelCreateRequest, exception);
            throw new RestTemplateException(ApiRestTemplate.SKYDROPX, "labels", exception.getMessage());
        }

        if ("ERROR".equals(labelCreateResponse.getLabelDataCreateResponse().getLabelAttributesResponse().getStatus())) {
            ShippingSkydropServiceImplementation.log.error("Error to consume service {} with this request {}.", this.urlApiSkydropxLabels, labelCreateRequest);
            throw new RestTemplateException(ApiRestTemplate.SKYDROPX, "labels", "Error");
        }
        orderEntity = this.shippingHelper.saveTrackingNumber(orderEntity, labelCreateResponse.getLabelDataCreateResponse().getLabelAttributesResponse().getTracingNumber());
        this.shippingHelper.saveTrackingUrlProvider(orderEntity, labelCreateResponse.getLabelDataCreateResponse().getLabelAttributesResponse().getTrackingUrlProvider());
        ShippingSkydropServiceImplementation.log.info("Finished create shipping by SKYDROP.");
    }
}


