package tr.kontas.erp.crm.application.activity;

import tr.kontas.erp.crm.domain.activity.Activity;
import tr.kontas.erp.crm.domain.activity.ActivityId;

public interface GetActivityByIdUseCase {
    Activity execute(ActivityId id);
}

