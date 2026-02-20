package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.reference.unit.CreateUnitCommand;
import tr.kontas.erp.core.application.reference.unit.CreateUnitUseCase;
import tr.kontas.erp.core.application.reference.unit.GetUnitByCodeUseCase;
import tr.kontas.erp.core.application.reference.unit.GetUnitsUseCase;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.domain.reference.unit.UnitCode;
import tr.kontas.erp.core.domain.reference.unit.UnitRepository;
import tr.kontas.erp.core.domain.reference.unit.UnitType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService implements
        CreateUnitUseCase,
        GetUnitByCodeUseCase,
        GetUnitsUseCase {

    private final UnitRepository unitRepository;

    @Override
    @Transactional
    public UnitCode execute(CreateUnitCommand command) {
        UnitCode code = new UnitCode(command.code());

        if (unitRepository.findByCode(code).isPresent()) {
            throw new IllegalArgumentException("Unit with code " + command.code() + " already exists");
        }

        UnitType type = UnitType.valueOf(command.type());

        Unit unit = new Unit(code, command.name(), type);

        unitRepository.save(unit);

        return code;
    }

    @Override
    public Unit execute(UnitCode code) {
        return unitRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found: " + code.getValue()));
    }

    @Override
    public List<Unit> execute() {
        return unitRepository.findAllActive();
    }
}
