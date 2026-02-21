package tr.kontas.erp.hr.application.performancecycle;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.hr.domain.performancecycle.PerformanceCycle;

import java.util.List;

public interface GetPerformanceCyclesByCompanyUseCase {
    List<PerformanceCycle> execute(CompanyId companyId);
}
