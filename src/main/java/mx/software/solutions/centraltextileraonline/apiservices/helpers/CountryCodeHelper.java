package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountryCodeResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CountryCodeRepository;

@Slf4j
@Component
public class CountryCodeHelper {

	@Autowired
	private CountryCodeRepository countryCodeRepository;

	public CountryCodeEntity getCountryCodeEntity(final UUID countryCode) throws DataBaseException, NotFoundException {
		Optional<CountryCodeEntity> optionalCountryCodeEntity;
		try {
			CountryCodeHelper.log.info("Starting searched the country code with the id {}.", countryCode);
			optionalCountryCodeEntity = this.countryCodeRepository.findById(countryCode);
		} catch (final Exception exception) {
			CountryCodeHelper.log.error("The country code with the id {} could not read.", countryCode, exception);
			throw new DataBaseException(Controller.CATALOG_COUNTRY_CODE, DataBaseActionType.READ, countryCode.toString());
		}

		if (optionalCountryCodeEntity.isEmpty()) {
			CountryCodeHelper.log.error("The country code not found with the id {}.", countryCode);
			throw new NotFoundException(Controller.CATALOG_COUNTRY_CODE, "id", countryCode.toString());
		}
		CountryCodeHelper.log.info("Finished search the country code with the id {}.", countryCode);
		return optionalCountryCodeEntity.get();
	}

	public CountryCodeResponse convertCountryCode(final CountryCodeEntity countryCodeEntity) {
		final var id = countryCodeEntity.getId();
		final var name = countryCodeEntity.getName();
		final var code = countryCodeEntity.getCode();
		return new CountryCodeResponse(id, name, code);
	}

}
