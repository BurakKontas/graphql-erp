package tr.kontas.erp.hr.application.leavebalance;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.leavebalance.LeaveBalance;

import java.util.List;

public interface GetLeaveBalancesByCompanyUseCase {
    List<LeaveBalance> execute(CompanyId companyId);
}
