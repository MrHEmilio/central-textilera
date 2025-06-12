package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothSamplerEntity;

public interface ClothSamplerRepository extends PagingAndSortingRepository<ClothSamplerEntity, UUID> {

}
