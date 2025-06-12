package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceShippingRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ShippingTrackingCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ShippingResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.RestTemplateException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ShippingHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.ShippingService;

@Slf4j
@Service
@Qualifier("shippingCentralTextileraSamplerService")
public class ShippingCentralTextileraSamplerServiceImplementation implements ShippingService {

	@Autowired
	private ShippingHelper shippingHelper;

	@Autowired
	private OrderHelper orderHelper;

	@Value("${central-textilera.sampler-shipping.provider}")
	private String samplerShippingProvider;

	@Value("${central-textilera.sampler-shipping.service-code}")
	private String samplerShippingServiceCode;

	@Value("${central-textilera.sampler-shipping.service-name}")
	private String samplerShippingServiceName;

	@Value("${central-textilera.sampler-shipping.price}")
	private BigDecimal samplerShippingPrice;

	@Override
	public List<ShippingResponse> calculatePriceShipping(final CalculatePriceShippingRequest calculatePriceShippingRequest, final List<ShippingResponse> listShippingResponse) throws RestTemplateException {
		final var hasSampler = !calculatePriceShippingRequest.getSamplers().isEmpty();
		final var hasCloth = !calculatePriceShippingRequest.getCloths().isEmpty();
		if (hasCloth || !hasSampler)
			return listShippingResponse;

		ShippingCentralTextileraSamplerServiceImplementation.log.info("Finished calculate price shipping by CENTRAL_TEXTILERA_SAMPLER.");
		final var calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 7);
		final var shippingResponse = new ShippingResponse();
		shippingResponse.setShippingMethod(ShippingMethod.CENTRAL_TEXTILERA_SAMPLER);
		shippingResponse.setRateId(UUID.randomUUID().toString());
		shippingResponse.setProvider(this.samplerShippingProvider);
		shippingResponse.setServiceCode(this.samplerShippingServiceCode);
		shippingResponse.setServiceName(this.samplerShippingServiceName);
		shippingResponse.setPrice(this.samplerShippingPrice);

		shippingResponse.setDate(calendar.getTime());
		listShippingResponse.add(shippingResponse);
		ShippingCentralTextileraSamplerServiceImplementation.log.info("Finished calculate price shipping by CENTRAL_TEXTILERA_SAMPLER.");
		return listShippingResponse;
	}

	@Override
	public void createShipping(final ShippingTrackingCreateRequest shippingTrackingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException {
		ShippingCentralTextileraSamplerServiceImplementation.log.info("Starting create tracking by CENTRAL_TEXTILERA_SAMPLER.");
		final var orderEntity = this.orderHelper.getOrderEntity(shippingTrackingCreateRequest.getOrder());
		this.shippingHelper.validateTrackingIdNotExist(orderEntity.getOrderShippingEntity());
		this.shippingHelper.saveTrackingNumber(orderEntity, UUID.randomUUID().toString());
		ShippingCentralTextileraSamplerServiceImplementation.log.info("Finished created tracking by CENTRAL_TEXTILERA_SAMPLER.");
	}

}
