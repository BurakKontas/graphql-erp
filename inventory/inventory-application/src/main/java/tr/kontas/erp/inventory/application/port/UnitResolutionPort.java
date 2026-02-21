package tr.kontas.erp.inventory.application.port;

import tr.kontas.erp.core.domain.reference.unit.Unit;

import java.util.Optional;

public interface UnitResolutionPort {
    Optional<Unit> resolveUnit(String unitCode);
}
