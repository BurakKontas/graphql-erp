package tr.kontas.erp.hr.platform.persistence.performancecycle;

import com.fasterxml.jackson.core.type.TypeReference;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;
import tr.kontas.erp.hr.domain.performancecycle.*;

import java.util.List;

public class PerformanceCycleMapper {

    public static PerformanceCycleJpaEntity toEntity(PerformanceCycle pc) {
        PerformanceCycleJpaEntity e = new PerformanceCycleJpaEntity();
        e.setId(pc.getId().asUUID());
        e.setTenantId(pc.getTenantId().asUUID());
        e.setCompanyId(pc.getCompanyId().asUUID());
        e.setName(pc.getName());
        e.setStartDate(pc.getStartDate());
        e.setEndDate(pc.getEndDate());
        e.setReviewDeadline(pc.getReviewDeadline());
        e.setStatus(pc.getStatus().name());
        e.setGoalsJson(JacksonProvider.serialize(pc.getGoals()));
        return e;
    }

    public static PerformanceCycle toDomain(PerformanceCycleJpaEntity e) {
        List<PerformanceGoal> goals = e.getGoalsJson() != null
                ? JacksonProvider.deserialize(e.getGoalsJson(), new TypeReference<>() {}) : List.of();
        return new PerformanceCycle(
                PerformanceCycleId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getName(), e.getStartDate(), e.getEndDate(), e.getReviewDeadline(),
                CycleStatus.valueOf(e.getStatus()), goals);
    }
}
