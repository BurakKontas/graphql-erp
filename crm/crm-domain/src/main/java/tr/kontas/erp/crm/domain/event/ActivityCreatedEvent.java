package tr.kontas.erp.crm.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCreatedEvent extends DomainEvent {

    private UUID activityId;
    private UUID tenantId;
    private UUID companyId;
    private String activityType;
    private String subject;
    private String relatedEntityType;
    private String relatedEntityId;

    @Override
    public UUID getAggregateId() {
        return activityId;
    }


    @Override
    public String getAggregateType() {
        return "Activity";
    }
}

