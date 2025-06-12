package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "\"OrderCloth\"")
public class OrderClothEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"cloth_name\"")
	private String clothName;

	@Column(name = "\"color_name\"")
	private String colorName;

	@Column(name = "\"amount\"")
	private BigDecimal amount;

	@Column(name = "\"sell_price\"")
	private BigDecimal sellPrice;

	@Column(name = "\"total_sell_price\"")
	private BigDecimal totalSellPrice;

	@Column(name = "\"discount\"")
	private BigDecimal discount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"cloth\"")
	private ClothEntity clothEntity;

}
