package tr.kontas.erp.core.platform.persistence.reference.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.domain.reference.unit.UnitCode;
import tr.kontas.erp.core.domain.reference.unit.UnitRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UnitRepositoryImpl implements UnitRepository {

    private final JpaUnitRepository jpaRepository;

    @Override
    public Optional<Unit> findByCode(UnitCode code) {
        return jpaRepository.findById(code.getValue())
                .map(UnitMapper::toDomain);
    }

    @Override
    public List<Unit> findAllActive() {
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(UnitMapper::toDomain)
                .toList();
    }

    public List<Unit> findByCodes(List<UnitCode> codes) {
        List<String> codeStrings = codes.stream()
                .map(UnitCode::getValue)
                .toList();
        return jpaRepository.findByCodeIn(codeStrings)
                .stream()
                .map(UnitMapper::toDomain)
                .toList();
    }

    @Override
    public void save(Unit unit) {
        jpaRepository.save(UnitMapper.toEntity(unit));
    }
}
