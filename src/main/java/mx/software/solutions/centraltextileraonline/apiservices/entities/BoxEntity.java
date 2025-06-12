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
@Table(name = "\"Box\"")
public class BoxEntity {
	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"width\"")
	private BigDecimal width;

	@Column(name = "\"height\"")
	private BigDecimal height;

	@Column(name = "\"depth\"")
	private BigDecimal depth;

	@Column(name = "\"color_code\"")
	private String colorCode;

	@Column(name = "\"amount\"")
	private BigDecimal amount;

	@Column(name = "\"active\"")
	private boolean active;

	public BoxEntity(final BoxEntity boxEntity) {
		this.id = boxEntity.id;
		this.name = boxEntity.name;
		this.width = boxEntity.width;
		this.height = boxEntity.height;
		this.depth = boxEntity.depth;
		this.colorCode = boxEntity.colorCode;
		this.amount = boxEntity.amount;
		this.active = boxEntity.active;
	}

}
