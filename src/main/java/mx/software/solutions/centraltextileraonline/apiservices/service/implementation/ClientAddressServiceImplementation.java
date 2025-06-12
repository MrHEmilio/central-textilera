package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ClientAddressUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ClientAddressResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientAddressEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClientAddressHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClientHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientAddressRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.ClientAddressService;

@Slf4j
@Service
public class ClientAddressServiceImplementation implements ClientAddressService {

	@Autowired
	private ClientAddressRepository clientAddressRepository;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private ClientHelper clientHelper;

	@Autowired
	private ClientAddressHelper clientAddressHelper;

	@Override
	public ClientAddressResponse createClientAddress(final ClientAddressCreateRequest clientAddressCreateRequest, final UUID client) throws DataBaseException, NotFoundException, ExistException {
		ClientAddressServiceImplementation.log.info("Starting created the client address {} with the client {}.", clientAddressCreateRequest.getName(), client);
		final var clientEntity = this.clientHelper.getClientEntity(client);
		this.validateClientAddressNotExist(clientEntity, clientAddressCreateRequest.getName());
		this.changePredetermined(clientEntity, clientAddressCreateRequest.isPredetermined());
		this.changeBillingAddress(clientEntity, clientAddressCreateRequest.isBillingAddress());
		try {
			final var clientAddressEntity = new ClientAddressEntity();
			clientAddressEntity.setClientEntity(clientEntity);
			clientAddressEntity.setName(clientAddressCreateRequest.getName());
			clientAddressEntity.setStreetName(clientAddressCreateRequest.getStreetName());
			clientAddressEntity.setNumExt(clientAddressCreateRequest.getNumExt());
			if (clientAddressCreateRequest.getNumInt() != null && !clientAddressCreateRequest.getNumInt().isBlank())
				clientAddressEntity.setNumInt(clientAddressCreateRequest.getNumInt());
			clientAddressEntity.setSuburb(clientAddressCreateRequest.getSuburb());
			clientAddressEntity.setZipCode(clientAddressCreateRequest.getZipCode());
			clientAddressEntity.setMunicipality(clientAddressCreateRequest.getMunicipality());
			clientAddressEntity.setState(clientAddressCreateRequest.getState());
			clientAddressEntity.setCity(clientAddressCreateRequest.getCity());
			clientAddressEntity.setCountry(clientAddressCreateRequest.getCountry());
			clientAddressEntity.setReferences(clientAddressCreateRequest.getReferences());
			clientAddressEntity.setLatitude(clientAddressCreateRequest.getLatitude());
			clientAddressEntity.setLongitude(clientAddressCreateRequest.getLongitude());
			clientAddressEntity.setPredetermined(clientAddressCreateRequest.isPredetermined());
			clientAddressEntity.setBillingAddress(clientAddressCreateRequest.isBillingAddress());
			clientAddressEntity.setActive(true);
			final var newClientAddressEntity = this.clientAddressRepository.save(clientAddressEntity);
			ClientAddressServiceImplementation.log.info("Finished create the client address {} with the client {}.", clientAddressCreateRequest.getName(), client);
			return this.clientAddressHelper.convertClientAddress(newClientAddressEntity);
		} catch (final Exception exception) {
			ClientAddressServiceImplementation.log.error("The client address {} could not been create with the client {}.", clientAddressCreateRequest.getName(), client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.CREATE, clientAddressCreateRequest.getName());
		}
	}

	@Override
	public ClientAddressResponse updateClientAddress(final ClientAddressUpdateRequest clientAddressUpdateRequest, final UUID client) throws DataBaseException, NotFoundException, ExistException {
		ClientAddressServiceImplementation.log.info("Starting updated the client address with the id {} and the client {}.", clientAddressUpdateRequest.getId(), client);
		final var clientEntity = this.clientHelper.getClientEntity(client);
		this.changePredetermined(clientEntity, clientAddressUpdateRequest.isPredetermined());
		this.changeBillingAddress(clientEntity, clientAddressUpdateRequest.isBillingAddress());
		final var clientAddressEntity = this.clientAddressHelper.getClientAddressEntity(clientAddressUpdateRequest.getId());
		clientAddressEntity.setClientEntity(clientEntity);
		clientAddressEntity.setName(clientAddressUpdateRequest.getName());
		clientAddressEntity.setStreetName(clientAddressUpdateRequest.getStreetName());
		clientAddressEntity.setNumExt(clientAddressUpdateRequest.getNumExt());
		if (clientAddressUpdateRequest.getNumInt() != null && !clientAddressUpdateRequest.getNumInt().isBlank())
			clientAddressEntity.setNumInt(clientAddressUpdateRequest.getNumInt());
		clientAddressEntity.setSuburb(clientAddressUpdateRequest.getSuburb());
		clientAddressEntity.setZipCode(clientAddressUpdateRequest.getZipCode());
		clientAddressEntity.setMunicipality(clientAddressUpdateRequest.getMunicipality());
		clientAddressEntity.setState(clientAddressUpdateRequest.getState());
		clientAddressEntity.setCity(clientAddressUpdateRequest.getCity());
		clientAddressEntity.setCountry(clientAddressUpdateRequest.getCountry());
		clientAddressEntity.setReferences(clientAddressUpdateRequest.getReferences());
		clientAddressEntity.setLatitude(clientAddressUpdateRequest.getLatitude());
		clientAddressEntity.setLongitude(clientAddressUpdateRequest.getLongitude());
		clientAddressEntity.setPredetermined(clientAddressUpdateRequest.isPredetermined());
		clientAddressEntity.setBillingAddress(clientAddressUpdateRequest.isBillingAddress());
		try {
			final var newClientAddressEntity = this.clientAddressRepository.save(clientAddressEntity);
			ClientAddressServiceImplementation.log.info("Finished update the client address with the client {}.", client);
			return this.clientAddressHelper.convertClientAddress(newClientAddressEntity);
		} catch (final Exception exception) {
			ClientAddressServiceImplementation.log.error("The client address could not been update with the id {} and the client {}.", clientAddressUpdateRequest.getId(), client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.UPDATE, clientAddressUpdateRequest.getName());
		}
	}

	@Override
	public ClientAddressResponse deleteClientAddress(final UUID clientAddress, final UUID client) throws DataBaseException, NotFoundException {
		ClientAddressServiceImplementation.log.info("Starting deleted the client address with the id {} and the client {}.", clientAddress, client);
		final var clientAddressEntity = this.clientAddressHelper.getClientAddressEntity(clientAddress);
		clientAddressEntity.setPredetermined(false);
		clientAddressEntity.setActive(false);
		try {
			final var newClientAddressEntity = this.clientAddressRepository.save(clientAddressEntity);
			ClientAddressServiceImplementation.log.info("Finished delete the client address with the client {}.", client);
			return this.clientAddressHelper.convertClientAddress(newClientAddressEntity);
		} catch (final Exception exception) {
			ClientAddressServiceImplementation.log.error("The client address could not been delete with the id {} and the client {}.", clientAddress, client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.DELETE, clientAddress.toString());
		}
	}

	@Override
	public GetResponse<ClientAddressResponse> getClientAddress(final UUID client, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		ClientAddressServiceImplementation.log.info("Starting searched of all the client address with the client {}.", client);
		Page<ClientAddressEntity> pageClientAddressEntity = null;
		try {
			final var clientEntity = new ClientEntity();
			clientEntity.setId(client);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageClientAddressEntity = this.clientAddressRepository.findByClientEntityAndActiveTrue(clientEntity, pageable);
		} catch (final Exception exception) {
			ClientAddressServiceImplementation.log.error("The client address with the client {} could not been read.", client, exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.READ, client.toString());
		}
		final var listClientAddressResponse = pageClientAddressEntity.get().map(this.clientAddressHelper::convertClientAddress).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageClientAddressEntity);
		if (listClientAddressResponse.isEmpty()) {
			ClientAddressServiceImplementation.log.error("The client address not found.");
			throw new NotFoundException(Controller.PROFILE_CLIENT_ADDRESS, "all");
		}
		ClientAddressServiceImplementation.log.info("Finished search the client address with the client {}.", client);
		return new GetResponse<>(listClientAddressResponse, paginationResponse);
	}

	private void validateClientAddressNotExist(final ClientEntity clientEntity, final String name) throws ExistException {
		ClientAddressServiceImplementation.log.info("Starting validate the client address if exist {}.", name);
		final var optionalUseEntity = this.clientAddressRepository.findByClientEntityAndNameAndActiveTrue(clientEntity, name);
		if (!optionalUseEntity.isEmpty()) {
			ClientAddressServiceImplementation.log.error("The client address {} exist.", name);
			throw new ExistException(Controller.PROFILE_CLIENT_ADDRESS, "name", name);
		}
	}

	private void changePredetermined(final ClientEntity clientEntity, final boolean isPredetermined) throws DataBaseException {
		try {
			ClientAddressServiceImplementation.log.info("Verify if new client address is predetermined.");
			if (!isPredetermined) {
				ClientAddressServiceImplementation.log.info("The new client address is not predetermined.");
				return;
			}

			ClientAddressServiceImplementation.log.info("Starting change client address predetermined.");
			final var optionalClientAddressEntity = this.clientAddressRepository.findByClientEntityAndPredeterminedTrue(clientEntity);
			if (optionalClientAddressEntity.isEmpty()) {
				ClientAddressServiceImplementation.log.info("The client address not found predetermined.");
				return;
			}
			final var clientAddressEntity = optionalClientAddressEntity.get();
			clientAddressEntity.setPredetermined(false);
			this.clientAddressRepository.save(clientAddressEntity);
			ClientAddressServiceImplementation.log.info("Finished change client address predetermined.");
		} catch (final Exception exception) {
			ClientAddressServiceImplementation.log.error("The client with the id {} could not read.", clientEntity.getId(), exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.READ, clientEntity.getId().toString());
		}
	}

	private void changeBillingAddress(final ClientEntity clientEntity, final boolean isBillingAddress) throws DataBaseException {
		try {
			ClientAddressServiceImplementation.log.info("Verify if new client address is billing address.");
			if (!isBillingAddress) {
				ClientAddressServiceImplementation.log.info("The new client address is not billing address.");
				return;
			}

			ClientAddressServiceImplementation.log.info("Starting change client address billing address.");
			final var optionalClientAddressEntity = this.clientAddressRepository.findByClientEntityAndBillingAddressTrue(clientEntity);
			if (optionalClientAddressEntity.isEmpty()) {
				ClientAddressServiceImplementation.log.info("The client address not found billing address.");
				return;
			}
			final var clientAddressEntity = optionalClientAddressEntity.get();
			clientAddressEntity.setBillingAddress(false);
			this.clientAddressRepository.save(clientAddressEntity);
			ClientAddressServiceImplementation.log.info("Finished change client address billing address.");
		} catch (final Exception exception) {
			ClientAddressServiceImplementation.log.error("The client with the id {} could not read.", clientEntity.getId(), exception);
			throw new DataBaseException(Controller.PROFILE_CLIENT_ADDRESS, DataBaseActionType.READ, clientEntity.getId().toString());
		}
	}

}
