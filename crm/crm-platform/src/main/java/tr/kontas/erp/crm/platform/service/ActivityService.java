package tr.kontas.erp.crm.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.crm.application.activity.*;
import tr.kontas.erp.crm.domain.activity.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService implements CreateActivityUseCase, GetActivityByIdUseCase,
        GetActivitiesByCompanyUseCase {

    private final ActivityRepository activityRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public ActivityId execute(CreateActivityCommand cmd) {
        TenantId tenantId = TenantContext.get();
        ActivityId id = ActivityId.newId();
        Activity activity = Activity.create(id, tenantId, cmd.companyId(),
                ActivityType.valueOf(cmd.activityType()),
                cmd.subject(), cmd.ownerId(),
                cmd.relatedEntityType(), cmd.relatedEntityId(),
                cmd.scheduledAt(), cmd.description());
        activityRepository.save(activity);
        activity.getDomainEvents().forEach(eventPublisher::publish);
        activity.clearDomainEvents();
        return id;
    }

    @Override
    public Activity execute(ActivityId id) {
        TenantId tenantId = TenantContext.get();
        return activityRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + id));
    }

    @Override
    public List<Activity> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return activityRepository.findByCompanyId(tenantId, companyId);
    }
}

