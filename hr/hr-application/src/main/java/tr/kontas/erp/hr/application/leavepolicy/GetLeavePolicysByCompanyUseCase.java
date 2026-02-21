package tr.kontas.erp.hr.application.leavepolicy;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.leavepolicy.LeavePolicy;

import java.util.List;

public interface GetLeavePolicysByCompanyUseCase {
    List<LeavePolicy> execute(CompanyId companyId);
}
