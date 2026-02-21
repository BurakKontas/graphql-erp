package tr.kontas.erp.hr.platform.persistence.position;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.position.*;

public class PositionMapper {

    public static PositionJpaEntity toEntity(Position p) {
        PositionJpaEntity e = new PositionJpaEntity();
        e.setId(p.getId().asUUID());
        e.setTenantId(p.getTenantId().asUUID());
        e.setCompanyId(p.getCompanyId().asUUID());
        e.setCode(p.getCode().getValue());
        e.setTitle(p.getTitle().getValue());
        e.setDepartmentId(p.getDepartmentId());
        e.setLevel(p.getLevel() != null ? p.getLevel().name() : null);
        e.setSalaryGrade(p.getSalaryGrade() != null ? p.getSalaryGrade().getValue() : null);
        e.setHeadcount(p.getHeadcount());
        e.setFilledCount(p.getFilledCount());
        e.setStatus(p.getStatus().name());
        return e;
    }

    public static Position toDomain(PositionJpaEntity e) {
        return new Position(
                PositionId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new PositionCode(e.getCode()),
                new PositionTitle(e.getTitle()),
                e.getDepartmentId(),
                e.getLevel() != null ? PositionLevel.valueOf(e.getLevel()) : null,
                e.getSalaryGrade() != null ? new SalaryGrade(e.getSalaryGrade()) : null,
                e.getHeadcount(),
                e.getFilledCount(),
                PositionStatus.valueOf(e.getStatus())
        );
    }
}

