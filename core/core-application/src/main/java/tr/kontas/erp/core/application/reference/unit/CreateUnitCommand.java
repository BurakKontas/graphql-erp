package tr.kontas.erp.core.application.reference.unit;

public record CreateUnitCommand(
        String code,
        String name,
        String type
) {
}
