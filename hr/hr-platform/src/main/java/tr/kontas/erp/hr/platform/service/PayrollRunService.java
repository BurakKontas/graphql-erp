package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.payrollrun.*;
import tr.kontas.erp.hr.application.port.PayrollRunNumberGeneratorPort;
import tr.kontas.erp.hr.domain.payrollrun.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollRunService implements CreatePayrollRunUseCase, GetPayrollRunByIdUseCase,
        GetPayrollRunsByCompanyUseCase, GetPayrollRunsByIdsUseCase {

    private final PayrollRunRepository payrollRunRepository;
    private final PayrollRunNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    public PayrollRunId execute(CreatePayrollRunCommand cmd) {
        TenantId tenantId = TenantContext.get();
        PayrollRunId id = PayrollRunId.newId();
        PayrollRunNumber number = numberGenerator.generate(tenantId, cmd.companyId(), cmd.year());
        PayrollRun run = new PayrollRun(id, tenantId, cmd.companyId(), number, cmd.year(), cmd.month(),
                PayrollRunStatus.DRAFT, cmd.paymentDate(), cmd.payrollConfigId(), List.of());
        payrollRunRepository.save(run);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollRun execute(PayrollRunId id) {
        return payrollRunRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("PayrollRun not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollRun> execute(CompanyId companyId) {
        return payrollRunRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollRun> execute(List<PayrollRunId> ids) {
        return payrollRunRepository.findByIds(ids);
    }
}
