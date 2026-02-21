package tr.kontas.erp.purchase.domain.vendorcatalog;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class VendorCatalog extends AggregateRoot<VendorCatalogId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String vendorId;
    private final String vendorName;
    private final String currencyCode;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private boolean active;
    private final List<VendorCatalogEntry> entries;

    public VendorCatalog(VendorCatalogId id, TenantId tenantId, CompanyId companyId,
                         String vendorId, String vendorName, String currencyCode,
                         LocalDate validFrom, LocalDate validTo, boolean active,
                         List<VendorCatalogEntry> entries) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.currencyCode = currencyCode;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.active = active;
        this.entries = new ArrayList<>(entries != null ? entries : List.of());
    }


    public List<VendorCatalogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }


    public void addEntry(VendorCatalogEntry entry) {
        this.entries.add(entry);
    }


    public void removeEntry(VendorCatalogEntryId entryId) {
        boolean removed = this.entries.removeIf(e -> e.getId().equals(entryId));
        if (!removed) {
            throw new IllegalArgumentException("Catalog entry not found: " + entryId);
        }
    }


    public void deactivate() {
        this.active = false;
    }


    public void activate() {
        this.active = true;
    }
}

