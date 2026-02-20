package tr.kontas.erp.core.application.reference.unit;

import tr.kontas.erp.core.domain.reference.unit.Unit;

import java.util.List;

public interface GetUnitsUseCase {
    List<Unit> execute();
}
