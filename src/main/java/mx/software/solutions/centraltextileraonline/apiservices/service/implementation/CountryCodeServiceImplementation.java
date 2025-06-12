package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountryCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.CountryCodeHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CountryCodeSearchRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.CountryCodeService;

@Slf4j
@Service
public class CountryCodeServiceImplementation implements CountryCodeService {

	@Autowired
	private CountryCodeSearchRepository countryCodeSearchRepository;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private CountryCodeHelper countryCodeHelper;

	@Override
	public GetResponse<CountryCodeResponse> getAllCountryCode(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		CountryCodeServiceImplementation.log.info("Starting searched of all country code.");
		Page<CountryCodeEntity> pageCountryCodeEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageCountryCodeEntity = this.countryCodeSearchRepository.findAll(filterRequest.getSearch(), pageable);
		} catch (final Exception exception) {
			CountryCodeServiceImplementation.log.error("The countries codes could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_COUNTRY_CODE, DataBaseActionType.READ);
		}
		final var listCountryCodeResponse = pageCountryCodeEntity.get().map(this.countryCodeHelper::convertCountryCode).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageCountryCodeEntity);
		if (listCountryCodeResponse.isEmpty()) {
			CountryCodeServiceImplementation.log.error("The countries codes not found.");
			throw new NotFoundException(Controller.CATALOG_COUNTRY_CODE, "all");
		}
		CountryCodeServiceImplementation.log.info("Finished search the countries codes.");
		return new GetResponse<>(listCountryCodeResponse, paginationResponse);
	}

}
