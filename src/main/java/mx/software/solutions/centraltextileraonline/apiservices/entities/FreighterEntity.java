package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
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
@Table(name = "\"Freighter\"")
public class FreighterEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"cost_per_distance\"")
	private BigDecimal costPerDistance;

	@Column(name = "\"cost_per_weight\"")
	private BigDecimal costPerWeight;

	@Column(name = "\"active\"")
	private boolean active;

	public FreighterEntity(final FreighterEntity freighterEntity) {
		this.id = freighterEntity.id;
		this.name = freighterEntity.name;
		this.costPerDistance = freighterEntity.costPerDistance;
		this.costPerWeight = freighterEntity.costPerWeight;
		this.active = freighterEntity.active;
	}

}
