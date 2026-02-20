package tr.kontas.erp.core.platform.persistence.reference.unit;

import tr.kontas.erp.core.domain.reference.unit.Unit;
import tr.kontas.erp.core.domain.reference.unit.UnitCode;
import tr.kontas.erp.core.domain.reference.unit.UnitType;

public class UnitMapper {

    public static Unit toDomain(UnitJpaEntity entity) {
        Unit unit = new Unit(
                new UnitCode(entity.getCode()),
                entity.getName(),
                UnitType.valueOf(entity.getType())
        );
        if (!entity.isActive()) {
            unit.deactivate();
        }
        return unit;
    }

    public static UnitJpaEntity toEntity(Unit domain) {
        return UnitJpaEntity.builder()
                .code(domain.getId().getValue())
                .name(domain.getName())
                .type(domain.getType().name())
                .active(domain.isActive())
                .build();
    }
}
