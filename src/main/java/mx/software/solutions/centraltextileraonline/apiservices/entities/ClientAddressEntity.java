package mx.software.solutions.centraltextileraonline.apiservices.entities;

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
@Table(name = "\"ClientAddress\"")
public class ClientAddressEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"client\"")
	private ClientEntity clientEntity;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"street_name\"")
	private String streetName;

	@Column(name = "\"num_ext\"")
	private String numExt;

	@Column(name = "\"num_int\"")
	private String numInt;

	@Column(name = "\"zip_code\"")
	private String zipCode;

	@Column(name = "\"suburb\"")
	private String suburb;

	@Column(name = "\"municipality\"")
	private String municipality;

	@Column(name = "\"state\"")
	private String state;

	@Column(name = "\"city\"")
	private String city;

	@Column(name = "\"country\"")
	private String country;

	@Column(name = "\"references\"")
	private String references;

	@Column(name = "\"latitude\"")
	private String latitude;

	@Column(name = "\"longitude\"")
	private String longitude;

	@Column(name = "\"predetermined\"")
	private boolean predetermined;

	@Column(name = "\"billing_address\"")
	private boolean billingAddress;

	@Column(name = "\"active\"")
	private boolean active;

}
