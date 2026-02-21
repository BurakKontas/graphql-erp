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
    private TaxRate rate;
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

    public static Tax reconstitute(
            TaxCode code, TenantId tenantId, CompanyId companyId,
            String name, TaxType type, TaxRate rate, boolean active) {
        Tax tax = new Tax(code, tenantId, companyId, name, type, rate);
        tax.active = active;
        return tax;
    }

    public void updateRate(TaxRate newRate) {
        if (newRate == null) throw new IllegalArgumentException("TaxRate cannot be null");
        this.rate = newRate;
    }

    public void deactivate() {
        this.active = false;
    }
}