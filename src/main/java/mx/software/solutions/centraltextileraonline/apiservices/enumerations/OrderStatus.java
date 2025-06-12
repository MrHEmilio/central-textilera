package mx.software.solutions.centraltextileraonline.apiservices.enumerations;

public enum OrderStatus {

	REVISION("En revisi\u00F3n"),
	PREPARATION("Preparando los productos"),
	SHIPPING("Enviado"),
	PICK_UP("Listo para recoger en sucursal"),
	DELIVERED("Entregado");

	private String label;

	OrderStatus(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
