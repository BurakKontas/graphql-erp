package tr.kontas.erp.core.domain.reference.unit;

import java.util.List;
import java.util.Optional;

public interface UnitRepository {
    Optional<Unit> findByCode(UnitCode code);

    List<Unit> findAllActive();

    void save(Unit unit);
}