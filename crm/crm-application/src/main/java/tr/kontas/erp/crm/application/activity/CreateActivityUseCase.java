package tr.kontas.erp.crm.application.activity;

import tr.kontas.erp.crm.domain.activity.ActivityId;

public interface CreateActivityUseCase {
    ActivityId execute(CreateActivityCommand command);
}

