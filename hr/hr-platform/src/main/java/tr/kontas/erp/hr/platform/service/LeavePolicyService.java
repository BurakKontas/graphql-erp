package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.leavepolicy.*;
import tr.kontas.erp.hr.domain.leavepolicy.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeavePolicyService implements CreateLeavePolicyUseCase, GetLeavePolicyByIdUseCase,
        GetLeavePolicysByCompanyUseCase, GetLeavePolicysByIdsUseCase {

    private final LeavePolicyRepository leavePolicyRepository;

    @Override
    public LeavePolicyId execute(CreateLeavePolicyCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LeavePolicyId id = LeavePolicyId.newId();
        List<LeaveTypeDef> types = cmd.leaveTypes() != null ? cmd.leaveTypes().stream()
                .map(lt -> new LeaveTypeDef(LeaveType.valueOf(lt.leaveType()), lt.annualEntitlementDays(),
                        lt.maxCarryoverDays(), lt.requiresApproval(), lt.requiresDocument(), lt.minNoticeDays()))
                .toList() : List.of();
        LeavePolicy policy = new LeavePolicy(id, tenantId, cmd.companyId(), cmd.name(), cmd.countryCode(), types, true);
        leavePolicyRepository.save(policy);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public LeavePolicy execute(LeavePolicyId id) {
        return leavePolicyRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("LeavePolicy not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeavePolicy> execute(CompanyId companyId) {
        return leavePolicyRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeavePolicy> execute(List<LeavePolicyId> ids) {
        return leavePolicyRepository.findByIds(ids);
    }
}
