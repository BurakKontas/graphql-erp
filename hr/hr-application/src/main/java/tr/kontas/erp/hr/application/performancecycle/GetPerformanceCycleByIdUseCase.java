package tr.kontas.erp.hr.application.performancecycle;

import tr.kontas.erp.hr.domain.performancecycle.PerformanceCycle;
import tr.kontas.erp.hr.domain.performancecycle.PerformanceCycleId;

public interface GetPerformanceCycleByIdUseCase {
    PerformanceCycle execute(PerformanceCycleId id);
}
