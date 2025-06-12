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
@Table(name = "\"Color\"")
public class ColorEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"code\"")
	private String code;

	@Column(name = "\"active\"")
	private boolean active;

	public ColorEntity(final ColorEntity colorEntity) {
		this.id = colorEntity.id;
		this.name = colorEntity.name;
		this.code = colorEntity.code;
		this.active = colorEntity.active;
	}

}
