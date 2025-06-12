package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.text.ParseException;
import java.util.*;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.WeightHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.ShippingDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceShippingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingTrackingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingTrackingCreateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SendEmailHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.OrderService;
import mx.software.solutions.centraltextileraonline.apiservices.service.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController implements ShippingDocumentation {
    @Autowired
    private ClothVariantRepository clothVariantRepository;

    @Autowired
    private SendEmailHelper sendEmailHelper;

    @Autowired
    private MessageLangHelper messageLangHelper;

    @Autowired
    private WeightHelper weightHelper;

    @Autowired
    @Qualifier("shippingCentralTextileraSamplerService")
    private ShippingService shippingCentralTextileraSamplerService;

    @Autowired
    @Qualifier("shippingCentralTextileraFreighterService")
    private ShippingService shippingCentralTextileraFreighterService;

    @Autowired
    @Qualifier("shippingSkydropService")
    private ShippingService shippingSkydropService;

    @Autowired
    @Qualifier("shippingPqtxService")
    private ShippingService shippingPqtxService;

    @Autowired
    private OrderService orderService;

    @Value("${central-textilera.shipping-method-active}")
    private List<ShippingMethod> centralTextileraShippingMethodActive;

    @Override
    @PostMapping("/calculate/price")
    public List<ShippingResponse> calculatePriceShipping(@Valid @RequestBody final CalculatePriceShippingRequest calculatePriceShippingRequest) throws RestTemplateException, ParseException {
        final List<ShippingResponse> listShippingResponse = new ArrayList<>();

        if (this.centralTextileraShippingMethodActive.contains(ShippingMethod.CENTRAL_TEXTILERA_SAMPLER))
            this.shippingCentralTextileraSamplerService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);

        /*String resp = weightHelper.checkWeigth(calculatePriceShippingRequest.getCloths());

		switch (resp){
            case "PQTX":
                if(this.centralTextileraShippingMethodActive.contains(ShippingMethod.PQTX))
                    this.shippingPqtxService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);
                if(this.centralTextileraShippingMethodActive.contains(ShippingMethod.SKYDROP))
                    this.shippingSkydropService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);
                break;
			case "SKY":
				if(this.centralTextileraShippingMethodActive.contains(ShippingMethod.SKYDROP))
					this.shippingSkydropService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);
                if(this.centralTextileraShippingMethodActive.contains(ShippingMethod.PQTX))
                    this.shippingPqtxService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);
                break;
			default:
                break;
		}*/

        if(this.centralTextileraShippingMethodActive.contains(ShippingMethod.PQTX))
            this.shippingPqtxService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);
        if(this.centralTextileraShippingMethodActive.contains(ShippingMethod.SKYDROP))
            this.shippingSkydropService.calculatePriceShipping(calculatePriceShippingRequest, listShippingResponse);

        listShippingResponse.sort(Comparator.comparing(ShippingResponse::getPrice));
        return listShippingResponse;
    }

    @Override
    @PostMapping("/tracking")
    public ShippingTrackingCreateResponse createShipping(@Valid @RequestBody final ShippingTrackingCreateRequest shippingTrackingCreateRequest)
            throws DataBaseException, NotFoundException, ExistException, RestTemplateException, JsonProcessingException {
        final var orderResponse = this.orderService.getOrderById(shippingTrackingCreateRequest.getOrder());

        switch (orderResponse.getOrderShipping().getShippingMethod()) {
            case CENTRAL_TEXTILERA_SAMPLER:
                this.shippingCentralTextileraSamplerService.createShipping(shippingTrackingCreateRequest);
                break;
            case CENTRAL_TEXTILERA_FREIGHTER:
                this.shippingCentralTextileraFreighterService.createShipping(shippingTrackingCreateRequest);
                break;
            case SKYDROP:
                this.shippingSkydropService.createShipping(shippingTrackingCreateRequest);
                break;
            case PQTX:
                this.shippingPqtxService.createShipping(shippingTrackingCreateRequest);
                break;
            default:
                break;
        }
        this.sendEmailHelper.sendEmailOrderCreated(shippingTrackingCreateRequest.getOrder());
        final var messageResponse = this.messageLangHelper.getMessageLang(Controller.SHIPPING_TRACKING, MessageLangType.RESPONSE, DataBaseActionType.CREATE);
        return new ShippingTrackingCreateResponse(messageResponse);
    }

}
