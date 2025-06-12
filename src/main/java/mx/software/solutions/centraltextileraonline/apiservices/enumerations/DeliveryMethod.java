package mx.software.solutions.centraltextileraonline.apiservices.enumerations;

public enum DeliveryMethod {

	SHIPPING("Env\u00EDo a domicilio"),
	PICK_UP("Recoger en sucursal");

	private String label;

	DeliveryMethod(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
