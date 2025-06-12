package mx.software.solutions.centraltextileraonline.apiservices.enumerations;

public enum PaymentMethod {

	PAYPAL("PayPal"),
	MERCADO_PAGO("MercadoPago");

	private String label;

	PaymentMethod(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

}
