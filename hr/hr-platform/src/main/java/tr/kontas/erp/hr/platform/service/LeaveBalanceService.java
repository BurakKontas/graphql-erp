package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.leavebalance.*;
import tr.kontas.erp.hr.domain.leavebalance.*;
import tr.kontas.erp.hr.domain.leavepolicy.LeaveType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveBalanceService implements CreateLeaveBalanceUseCase, GetLeaveBalanceByIdUseCase,
        GetLeaveBalancesByCompanyUseCase, GetLeaveBalancesByIdsUseCase {

    private final LeaveBalanceRepository leaveBalanceRepository;

    @Override
    public LeaveBalanceId execute(CreateLeaveBalanceCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LeaveBalanceId id = LeaveBalanceId.newId();
        LeaveBalance balance = new LeaveBalance(id, tenantId, cmd.companyId(), cmd.employeeId(),
                LeaveType.valueOf(cmd.leaveType()), cmd.year(), cmd.entitlementDays(), 0, cmd.carryoverDays(), 0);
        leaveBalanceRepository.save(balance);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveBalance execute(LeaveBalanceId id) {
        return leaveBalanceRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("LeaveBalance not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalance> execute(CompanyId companyId) {
        return leaveBalanceRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalance> execute(List<LeaveBalanceId> ids) {
        return leaveBalanceRepository.findByIds(ids);
    }
}
