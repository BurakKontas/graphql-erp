package tr.kontas.erp.core.domain.reference.tax;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.Entity;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@Getter
public class Tax extends Entity<TaxCode> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String name;
    private final TaxType type;
    private final TaxRate rate;
    private boolean active;

    public Tax(
            TaxCode code,
            TenantId tenantId,
            CompanyId companyId,
            String name,
            TaxType type,
            TaxRate rate
    ) {
        super(code);

        if (tenantId == null) throw new IllegalArgumentException("TenantId required");
        if (companyId == null) throw new IllegalArgumentException("CompanyId required");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.name = name;
        this.type = type;
        this.rate = rate;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}