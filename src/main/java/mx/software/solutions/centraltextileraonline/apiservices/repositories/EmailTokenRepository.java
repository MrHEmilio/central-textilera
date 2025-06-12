package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailTokenEntity;

public interface EmailTokenRepository extends CrudRepository<EmailTokenEntity, UUID> {

	Optional<EmailTokenEntity> findByToken(String token);

	Optional<EmailTokenEntity> findByCredentialEntityAndActiveTrue(CredentialEntity credential);

}
