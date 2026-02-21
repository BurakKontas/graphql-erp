package tr.kontas.erp.core.domain.reference.tax;

import lombok.Getter;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class TaxRateChangedEvent extends DomainEvent {

    private final UUID tenantId;
    private final UUID companyId;
    private final String taxCode;
    private final BigDecimal oldRate;
    private final BigDecimal newRate;

    public TaxRateChangedEvent(UUID tenantId, UUID companyId, String taxCode,
                               BigDecimal oldRate, BigDecimal newRate) {
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.taxCode = taxCode;
        this.oldRate = oldRate;
        this.newRate = newRate;
    }

    @Override
    public UUID getAggregateId() {
        // Tax PK is a code, not UUID; use a deterministic UUID from code
        return UUID.nameUUIDFromBytes(taxCode.getBytes());
    }

    @Override
    public String getAggregateType() {
        return "Tax";
    }
}
