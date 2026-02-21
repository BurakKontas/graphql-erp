package tr.kontas.erp.hr.application.performancecycle;

import tr.kontas.erp.hr.domain.performancecycle.PerformanceCycle;
import tr.kontas.erp.hr.domain.performancecycle.PerformanceCycleId;

import java.util.List;

public interface GetPerformanceCyclesByIdsUseCase {
    List<PerformanceCycle> execute(List<PerformanceCycleId> ids);
}
