package tr.kontas.erp.core.application.reference.unit;

import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.domain.reference.unit.UnitCode;

public interface GetUnitByCodeUseCase {
    Unit execute(UnitCode code);
}
