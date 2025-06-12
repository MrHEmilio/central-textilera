package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.NewsletterEntity;

public interface NewsletterRepository extends PagingAndSortingRepository<NewsletterEntity, UUID> {

	Optional<NewsletterEntity> findByEmailIgnoreCase(String email);

}
