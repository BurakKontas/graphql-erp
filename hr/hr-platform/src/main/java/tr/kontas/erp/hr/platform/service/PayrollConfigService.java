package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.payrollconfig.*;
import tr.kontas.erp.hr.domain.payrollconfig.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollConfigService implements CreatePayrollConfigUseCase, GetPayrollConfigByIdUseCase,
        GetPayrollConfigsByCompanyUseCase, GetPayrollConfigsByIdsUseCase {

    private final PayrollConfigRepository payrollConfigRepository;

    @Override
    public PayrollConfigId execute(CreatePayrollConfigCommand cmd) {
        TenantId tenantId = TenantContext.get();
        PayrollConfigId id = PayrollConfigId.newId();
        PayrollConfig config = new PayrollConfig(id, tenantId, cmd.companyId(), cmd.countryCode(),
                cmd.name(), cmd.validYear(), cmd.minimumWage(), cmd.currencyCode(),
                List.of(), List.of(), true);
        payrollConfigRepository.save(config);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollConfig execute(PayrollConfigId id) {
        return payrollConfigRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("PayrollConfig not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollConfig> execute(CompanyId companyId) {
        return payrollConfigRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayrollConfig> execute(List<PayrollConfigId> ids) {
        return payrollConfigRepository.findByIds(ids);
    }
}
