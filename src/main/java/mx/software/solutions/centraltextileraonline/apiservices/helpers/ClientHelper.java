package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientRepository;

@Slf4j
@Component
public class ClientHelper {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CountryCodeHelper countryCodeHelper;

	public ClientEntity getClientEntity(final UUID client) throws DataBaseException, NotFoundException {
		Optional<ClientEntity> optionalClientEntity = Optional.empty();
		try {
			ClientHelper.log.info("Starting searched the client with the id {}.", client);
			optionalClientEntity = this.clientRepository.findById(client);
		} catch (final Exception exception) {
			ClientHelper.log.error("The client with the id {} could not read.", client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT, DataBaseActionType.READ, client.toString());
		}

		if (optionalClientEntity.isEmpty()) {
			ClientHelper.log.error("The client not found with the id {}.", client);
			throw new NotFoundException(Controller.PROFILE_CLIENT, "id", client.toString());
		}
		ClientHelper.log.info("Finished search the client with the id {}.", client);
		return optionalClientEntity.get();
	}

	public ClientResponse convertClient(final ClientEntity clientEntity) {
		final var id = clientEntity.getId();
		final var name = clientEntity.getName();
		final var firstLastname = clientEntity.getFirstLastname();
		final var secondLastname = clientEntity.getSecondLastname();
		final var countryCodeResponse = this.countryCodeHelper.convertCountryCode(clientEntity.getCountryCodeEntity());
		final var phone = clientEntity.getPhone();
		final var email = clientEntity.getCredentialEntity().getEmail();
		final var isEmailValidated = clientEntity.isEmailValidated();
		final var date = clientEntity.getCredentialEntity().getDate();
		final var rfc = clientEntity.getRfc();
		final var companyName = clientEntity.getCompanyName();
		final var fiscalRegimen = clientEntity.getFiscalRegimen();
		final var isActive = clientEntity.getCredentialEntity().isActive();
		return new ClientResponse(id, name, firstLastname, secondLastname, countryCodeResponse, phone, email, isEmailValidated, date, rfc, companyName, fiscalRegimen, isActive);
	}

}
