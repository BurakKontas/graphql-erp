package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.position.*;
import tr.kontas.erp.hr.domain.position.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionService implements CreatePositionUseCase, GetPositionByIdUseCase,
        GetPositionsByCompanyUseCase, GetPositionsByIdsUseCase {

    private final PositionRepository positionRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public PositionId execute(CreatePositionCommand cmd) {
        TenantId tenantId = TenantContext.get();
        PositionId id = PositionId.newId();
        Position position = new Position(
                id, tenantId, cmd.companyId(),
                new PositionCode(cmd.code()), new PositionTitle(cmd.title()), cmd.departmentId(),
                cmd.level() != null ? PositionLevel.valueOf(cmd.level()) : null,
                cmd.salaryGrade() != null ? new SalaryGrade(cmd.salaryGrade()) : null, cmd.headcount(), 0, PositionStatus.ACTIVE
        );
        positionRepository.save(position);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Position execute(PositionId id) {
        TenantId tenantId = TenantContext.get();
        return positionRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Position> execute(CompanyId companyId) {
        return positionRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Position> execute(List<PositionId> ids) {
        return positionRepository.findByIds(ids);
    }
}

