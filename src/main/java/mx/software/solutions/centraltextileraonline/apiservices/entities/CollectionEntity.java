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
@Table(name = "\"Collection\"")
public class CollectionEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"active\"")
	private boolean active;

	public CollectionEntity(final CollectionEntity collectionEntity) {
		this.id = collectionEntity.id;
		this.name = collectionEntity.name;
		this.active = collectionEntity.active;
	}

}
