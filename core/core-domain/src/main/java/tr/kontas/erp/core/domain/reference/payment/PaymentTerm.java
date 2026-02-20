package tr.kontas.erp.core.domain.reference.payment;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.Entity;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class PaymentTerm extends Entity<PaymentTermCode> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String name;
    private final int dueDays;
    private boolean active;

    public PaymentTerm(
            PaymentTermCode code,
            TenantId tenantId,
            CompanyId companyId,
            String name,
            int dueDays
    ) {
        super(code);

        if (dueDays < 0) {
            throw new IllegalArgumentException("Due days cannot be negative");
        }

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.name = name;
        this.dueDays = dueDays;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}