package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.performancecycle.*;
import tr.kontas.erp.hr.domain.performancecycle.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceCycleService implements CreatePerformanceCycleUseCase, GetPerformanceCycleByIdUseCase,
        GetPerformanceCyclesByCompanyUseCase, GetPerformanceCyclesByIdsUseCase {

    private final PerformanceCycleRepository performanceCycleRepository;

    @Override
    public PerformanceCycleId execute(CreatePerformanceCycleCommand cmd) {
        TenantId tenantId = TenantContext.get();
        PerformanceCycleId id = PerformanceCycleId.newId();
        List<PerformanceGoal> goals = cmd.goals() != null ? cmd.goals().stream()
                .map(g -> new PerformanceGoal(g.description(), g.targetScore())).toList() : List.of();
        PerformanceCycle cycle = new PerformanceCycle(id, tenantId, cmd.companyId(), cmd.name(),
                cmd.startDate(), cmd.endDate(), cmd.reviewDeadline(), CycleStatus.PLANNED, goals);
        performanceCycleRepository.save(cycle);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public PerformanceCycle execute(PerformanceCycleId id) {
        return performanceCycleRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("PerformanceCycle not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceCycle> execute(CompanyId companyId) {
        return performanceCycleRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerformanceCycle> execute(List<PerformanceCycleId> ids) {
        return performanceCycleRepository.findByIds(ids);
    }
}
