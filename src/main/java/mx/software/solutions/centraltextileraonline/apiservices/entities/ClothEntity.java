package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "\"Cloth\"")
public class ClothEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"name_url\"")
	private String nameUrl;

	@Column(name = "\"main_description\"")
	private String mainDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"fiber\"")
	private FiberEntity fiberEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"sale\"")
	private SaleEntity saleEntity;

	@OneToOne(mappedBy = "clothEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ClothSamplerEntity clothSamplerEntity;

	@OneToOne(mappedBy = "clothEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ClothMeasureEntity clothMeasureEntity;

	@OneToOne(mappedBy = "clothEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ClothBillingEntity clothBillingEntity;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "\"cloth\"", nullable = false)
	List<ClothDescriptionEntity> clothDescriptionEntities;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "\"cloth\"", nullable = false)
	List<ClothVariantEntity> clothVariantEntities;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "\"cloth\"", nullable = false)
	@OrderBy("order")
	List<ClothPriceEntity> clothPriceEntities;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "\"Cloth_Collection\"", joinColumns = @JoinColumn(name = "\"cloth\""), inverseJoinColumns = @JoinColumn(name = "\"collection\""))
	List<CollectionEntity> collectionEntities;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "\"Cloth_Use\"", joinColumns = @JoinColumn(name = "\"cloth\""), inverseJoinColumns = @JoinColumn(name = "\"use\""))
	List<UseEntity> useEntities;

	@Column(name = "\"active\"")
	private boolean active;

	public ClothEntity(final ClothEntity clothEntity) {
		this.id = clothEntity.id;
		this.name = clothEntity.name;
		this.mainDescription = clothEntity.mainDescription;
		this.fiberEntity = new FiberEntity(clothEntity.fiberEntity);
		this.saleEntity = new SaleEntity(clothEntity.saleEntity);
		this.clothSamplerEntity = new ClothSamplerEntity(clothEntity.clothSamplerEntity);
		this.clothMeasureEntity = new ClothMeasureEntity(clothEntity.clothMeasureEntity);
		this.clothDescriptionEntities = clothEntity.clothDescriptionEntities.stream().map(ClothDescriptionEntity::new).collect(Collectors.toList());
		this.clothVariantEntities = clothEntity.clothVariantEntities.stream().map(ClothVariantEntity::new).collect(Collectors.toList());
		this.clothPriceEntities = clothEntity.clothPriceEntities.stream().map(ClothPriceEntity::new).collect(Collectors.toList());
		this.collectionEntities = clothEntity.collectionEntities.stream().map(CollectionEntity::new).collect(Collectors.toList());
		this.useEntities = clothEntity.useEntities.stream().map(UseEntity::new).collect(Collectors.toList());
		this.active = clothEntity.active;
	}

}
