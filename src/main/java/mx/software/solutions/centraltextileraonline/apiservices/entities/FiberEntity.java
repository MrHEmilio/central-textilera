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
@Table(name = "\"Fiber\"")
public class FiberEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"active\"")
	private boolean active;

	public FiberEntity(final FiberEntity fiberEntity) {
		this.id = fiberEntity.id;
		this.name = fiberEntity.name;
		this.active = fiberEntity.active;
	}

}
