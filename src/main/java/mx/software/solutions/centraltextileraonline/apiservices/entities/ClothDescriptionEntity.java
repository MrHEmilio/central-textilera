package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "\"ClothDescription\"")
public class ClothDescriptionEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"active\"")
	private boolean active;

	public ClothDescriptionEntity(final ClothDescriptionEntity clothDescriptionEntity) {
		this.id = clothDescriptionEntity.id;
		this.name = clothDescriptionEntity.name;
		this.active = clothDescriptionEntity.active;
	}

}
