package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ImagesHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ShippingHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FreighterRepository;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.DistanceMatrixResponse;
import mx.software.solutions.centraltextileraonline.apiservices.service.ShippingService;

@Slf4j
@Service
@Qualifier("shippingCentralTextileraFreighterService")
public class ShippingCentralTextileraFreighterServiceImplementation implements ShippingService {

	@Autowired
	private OrderHelper orderHelper;

	@Autowired
	private ShippingHelper shippingHelper;

	@Autowired
	private FreighterRepository freighterRepository;

	@Autowired
	private ImagesHelper imagesHelper;

	@Value("${url.api.google.distance-matrix}")
	private String urlApiGoogleDistanceMatrix;

	@Value("${token.google.api-key}")
	private String apiKeyGoogle;

	@Value("${central-textilera.address.latitude}")
	private String storeLatitude;

	@Value("${central-textilera.address.longitude}")
	private String storeLongitude;

	@Value("${url.images.freighters}")
	private String urlImagesFreighters;

	@Override
	public List<ShippingResponse> calculatePriceShipping(final CalculatePriceShippingRequest calculatePriceShippingRequest, final List<ShippingResponse> listShippingResponse) throws RestTemplateException {

		final var listMeasureDtos = this.shippingHelper.getMeasureDto(calculatePriceShippingRequest.getCloths());
		listMeasureDtos.forEach(measureDto ->{
			if(!this.shippingHelper.isValidMeasureDto(measureDto)) return;

			final var iterableFreighterEntity = this.freighterRepository.findAllByActiveTrue();

			if (iterableFreighterEntity.spliterator().getExactSizeIfKnown() == 0) return;

			ShippingCentralTextileraFreighterServiceImplementation.log.info("Starting calculated price shipping by CENTRAL_TEXTILERA_FREIGHTER.");
			final var restTemplate = new RestTemplate();
			final var origins = this.storeLatitude + "," + this.storeLongitude;
			final var destinations = calculatePriceShippingRequest.getLatitude() + "," + calculatePriceShippingRequest.getLongitude();
			final var uriComponents = UriComponentsBuilder.fromHttpUrl(this.urlApiGoogleDistanceMatrix).queryParam("origins", origins).queryParam("destinations", destinations).queryParam("key", this.apiKeyGoogle).encode().build();
			DistanceMatrixResponse distanceMatrixResponse = null;
			try {
				ShippingCentralTextileraFreighterServiceImplementation.log.info("Starting consume service {}.", uriComponents.toUriString());
				final var responseEntity = restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, DistanceMatrixResponse.class);
				distanceMatrixResponse = responseEntity.getBody();
				ShippingCentralTextileraFreighterServiceImplementation.log.info("Finished consume service {}.", uriComponents.toUriString());
			} catch (final Exception exception) {
				ShippingCentralTextileraFreighterServiceImplementation.log.info("Error to consume service {}.", uriComponents.toUriString(), exception);
				try {
					throw new RestTemplateException(ApiRestTemplate.GOOGLE, "distance.matrix", exception.getMessage());
				} catch (RestTemplateException e) {
					throw new RuntimeException(e);
				}
			}

			if (!"OK".equals(distanceMatrixResponse.getStatus())) {
				ShippingCentralTextileraFreighterServiceImplementation.log.info("Error to consume service {}.", uriComponents.toUriString(), distanceMatrixResponse.getErrorMessage());
                try {
                    throw new RestTemplateException(ApiRestTemplate.GOOGLE, "distance.matrix", distanceMatrixResponse.getErrorMessage());
                } catch (RestTemplateException e) {
                    throw new RuntimeException(e);
                }
            }

			final var kilometers = distanceMatrixResponse.getRows().get(0).getElements().get(0).getDistance().getValue().divide(BigDecimal.valueOf(1000));
			iterableFreighterEntity.forEach(freighterEntity -> {
				final var shippingResponse = new ShippingResponse();
				final var priceByDistance = freighterEntity.getCostPerDistance().multiply(kilometers);
				final var priceByWeight = freighterEntity.getCostPerWeight().multiply(measureDto.getWeight());
				final var calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, 7);
				final var image = this.imagesHelper.getUrlImage(this.urlImagesFreighters, freighterEntity.getId());
				shippingResponse.setShippingMethod(ShippingMethod.CENTRAL_TEXTILERA_FREIGHTER);
				shippingResponse.setRateId(UUID.randomUUID().toString());
				shippingResponse.setProvider(freighterEntity.getName());
				shippingResponse.setServiceCode(freighterEntity.getName());
				shippingResponse.setServiceName(freighterEntity.getName());
				shippingResponse.setPrice(priceByDistance.add(priceByWeight));
				shippingResponse.setDate(calendar.getTime());
				shippingResponse.setImage(image);
				listShippingResponse.add(shippingResponse);
			});
		});

		ShippingCentralTextileraFreighterServiceImplementation.log.info("Finished calculate price shipping by CENTRAL_TEXTILERA_FREIGHTER.");
		return listShippingResponse;
	}

	@Override
	public void createShipping(final ShippingTrackingCreateRequest shippingTrackingCreateRequest) throws DataBaseException, NotFoundException, ExistException, RestTemplateException {
		ShippingCentralTextileraFreighterServiceImplementation.log.info("Starting create tracking by CENTRAL_TEXTILERA_FREIGHTER.");
		final var orderEntity = this.orderHelper.getOrderEntity(shippingTrackingCreateRequest.getOrder());
		this.shippingHelper.validateTrackingIdNotExist(orderEntity.getOrderShippingEntity());
		this.shippingHelper.saveTrackingNumber(orderEntity, UUID.randomUUID().toString());
		ShippingCentralTextileraFreighterServiceImplementation.log.info("Finished created tracking by CENTRAL_TEXTILERA_FREIGHTER.");
	}

}
