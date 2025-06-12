package mx.software.solutions.centraltextileraonline.apiservices.enumerations;

public enum PaymentNotification {
    PENDING("pending"),
    APPROVED("approved"),
    AUTHORIZED("authorized"),
    INPROCESS("in_process"),
    INMEDIATION("in_mediation"),
    REJECTED("rejected"),
    CANCELLED("cancelled"),
    REFUNDED("refunded"),
    CHARGEDBACK("charged_back");

    private final String status;

    PaymentNotification(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public static PaymentNotification fromStatus(String status) {
        for (PaymentNotification payNot : PaymentNotification.values()) {
            if (payNot.getStatus().equalsIgnoreCase(status)) {
                return payNot;
            }
        }
        throw new IllegalArgumentException("Estado de notificación no válido " + status);
    }
}
