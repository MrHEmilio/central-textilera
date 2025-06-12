package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailNewsletterTemplateEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailNewsletterTemplateHistoryEntity;

public interface EmailNewsletterTemplateHistoryRepository extends PagingAndSortingRepository<EmailNewsletterTemplateHistoryEntity, UUID> {

	Page<EmailNewsletterTemplateHistoryEntity> findAllByEmailNewsletterTemplateEntity(EmailNewsletterTemplateEntity emailNewsletterTemplateEntity, Pageable pageable);

}
