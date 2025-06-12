package mx.software.solutions.centraltextileraonline.apiservices.enumerations;

public enum ShippingMethod {
	CENTRAL_TEXTILERA_SAMPLER("Central Textilera Muestrarios"),
	CENTRAL_TEXTILERA_FREIGHTER("Central Textilera Calculadora Mayor"),
	SKYDROP("Skydropx"),
	ENVIA("Envia"),
	PQTX("PaquetExpress");

	private String label;

	ShippingMethod(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
