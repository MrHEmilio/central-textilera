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
@Table(name = "\"ClothPrice\"")
public class ClothPriceEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"first_amount_range\"")
	private int firstAmountRange;

	@Column(name = "\"last_amount_range\"")
	private Integer lastAmountRange;

	@Column(name = "\"price\"")
	private BigDecimal price;

	@Column(name = "\"order\"")
	private int order;

	public ClothPriceEntity(final ClothPriceEntity clothPriceEntity) {
		this.id = clothPriceEntity.id;
		this.firstAmountRange = clothPriceEntity.firstAmountRange;
		this.lastAmountRange = clothPriceEntity.lastAmountRange;
		this.price = clothPriceEntity.price;
		this.order = clothPriceEntity.order;
	}

}
