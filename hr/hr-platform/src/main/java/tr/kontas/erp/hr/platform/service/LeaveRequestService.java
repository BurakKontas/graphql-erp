package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.leaverequest.*;
import tr.kontas.erp.hr.application.port.LeaveRequestNumberGeneratorPort;
import tr.kontas.erp.hr.domain.leavepolicy.LeaveType;
import tr.kontas.erp.hr.domain.leaverequest.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService implements CreateLeaveRequestUseCase, GetLeaveRequestByIdUseCase,
        GetLeaveRequestsByCompanyUseCase, GetLeaveRequestsByIdsUseCase {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestNumberGeneratorPort numberGenerator;

    @Override
    public LeaveRequestId execute(CreateLeaveRequestCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LeaveRequestId id = LeaveRequestId.newId();
        LeaveRequestNumber number = numberGenerator.generate(tenantId, cmd.companyId(), LocalDate.now().getYear());
        LeaveRequest request = new LeaveRequest(id, tenantId, cmd.companyId(), number, cmd.employeeId(),
                null, LeaveType.valueOf(cmd.leaveType()), cmd.startDate(), cmd.endDate(), cmd.requestedDays(),
                LeaveRequestStatus.DRAFT, cmd.reason(), cmd.documentRef(), null);
        leaveRequestRepository.save(request);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveRequest execute(LeaveRequestId id) {
        return leaveRequestRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("LeaveRequest not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequest> execute(CompanyId companyId) {
        return leaveRequestRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveRequest> execute(List<LeaveRequestId> ids) {
        return leaveRequestRepository.findByIds(ids);
    }
}
