package tr.kontas.erp.inventory.platform.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.domain.reference.unit.UnitCode;
import tr.kontas.erp.core.domain.reference.unit.UnitRepository;
import tr.kontas.erp.inventory.application.port.UnitResolutionPort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UnitResolutionImpl implements UnitResolutionPort {

    private final UnitRepository unitRepository;

    @Override
    public Optional<Unit> resolveUnit(String unitCode) {
        if (unitCode == null || unitCode.isBlank()) {
            return Optional.empty();
        }
        return unitRepository.findByCode(new UnitCode(unitCode));
    }
}
