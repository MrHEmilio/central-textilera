package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;

public interface CountryCodeRepository extends PagingAndSortingRepository<CountryCodeEntity, UUID> {

}
