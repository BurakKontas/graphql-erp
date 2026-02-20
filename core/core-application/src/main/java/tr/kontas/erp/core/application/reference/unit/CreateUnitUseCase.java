package tr.kontas.erp.core.application.reference.unit;

import tr.kontas.erp.core.domain.reference.unit.UnitCode;

public interface CreateUnitUseCase {
    UnitCode execute(CreateUnitCommand command);
}
