package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientAddressResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientAddressEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientAddressRepository;

@Slf4j
@Component
public class ClientAddressHelper {

	@Autowired
	private ClientAddressRepository clientAddressRepository;

	public ClientAddressEntity getClientAddressEntity(final UUID clientAddress) throws DataBaseException, NotFoundException {
		Optional<ClientAddressEntity> optionalCliendAddressEntity = Optional.empty();
		try {
			ClientAddressHelper.log.info("Starting searched the client address with the id {}.", clientAddress);
			optionalCliendAddressEntity = this.clientAddressRepository.findById(clientAddress);
		} catch (final Exception exception) {
			ClientAddressHelper.log.error("The client address with the id {} could not read.", clientAddress, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.READ, clientAddress.toString());
		}

		if (optionalCliendAddressEntity.isEmpty()) {
			ClientAddressHelper.log.error("The client address not found with the id {}.", clientAddress);
			throw new NotFoundException(Controller.PROFILE_CLIENT_ADDRESS, "id", clientAddress.toString());
		}
		ClientAddressHelper.log.info("Finished search the client address with the id {}.", clientAddress);
		return optionalCliendAddressEntity.get();
	}

	public ClientAddressResponse convertClientAddress(final ClientAddressEntity clientAddressEntity) {
		final var id = clientAddressEntity.getId();
		final var name = clientAddressEntity.getName();
		final var streetName = clientAddressEntity.getStreetName();
		final var numExt = clientAddressEntity.getNumExt();
		final var numInt = clientAddressEntity.getNumInt();
		final var zipCode = clientAddressEntity.getZipCode();
		final var suburb = clientAddressEntity.getSuburb();
		final var municipality = clientAddressEntity.getMunicipality();
		final var state = clientAddressEntity.getState();
		final var city = clientAddressEntity.getCity();
		final var country = clientAddressEntity.getCountry();
		final var latitude = clientAddressEntity.getLatitude();
		final var longitude = clientAddressEntity.getLongitude();
		final var isPredetermined = clientAddressEntity.isPredetermined();
		final var isBillingAddress = clientAddressEntity.isBillingAddress();
		final var isActive = clientAddressEntity.isActive();

		return new ClientAddressResponse(id, name, streetName, numExt, numInt, zipCode, suburb, municipality, state, city, country, latitude, longitude, isPredetermined, isBillingAddress, isActive);
	}

}
