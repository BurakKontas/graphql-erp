package tr.kontas.erp.hr.application.performancecycle;

import tr.kontas.erp.hr.domain.performancecycle.PerformanceCycleId;

public interface CreatePerformanceCycleUseCase {
    PerformanceCycleId execute(CreatePerformanceCycleCommand command);
}
