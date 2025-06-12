package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailTemplateEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.EmailTemplate;

public interface EmailTemplateRepository extends CrudRepository<EmailTemplateEntity, UUID> {

	EmailTemplateEntity findByName(EmailTemplate name);

}
