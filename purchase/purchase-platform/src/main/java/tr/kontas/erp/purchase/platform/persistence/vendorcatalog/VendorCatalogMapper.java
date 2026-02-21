package tr.kontas.erp.purchase.platform.persistence.vendorcatalog;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.vendorcatalog.*;

import java.util.ArrayList;
import java.util.List;

public class VendorCatalogMapper {

    public static VendorCatalogJpaEntity toEntity(VendorCatalog domain) {
        VendorCatalogJpaEntity entity = new VendorCatalogJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setVendorId(domain.getVendorId());
        entity.setVendorName(domain.getVendorName());
        entity.setCurrencyCode(domain.getCurrencyCode());
        entity.setValidFrom(domain.getValidFrom());
        entity.setValidTo(domain.getValidTo());
        entity.setActive(domain.isActive());

        List<VendorCatalogEntryJpaEntity> entryEntities = new ArrayList<>();
        for (VendorCatalogEntry entry : domain.getEntries()) {
            VendorCatalogEntryJpaEntity ee = new VendorCatalogEntryJpaEntity();
            ee.setId(entry.getId().asUUID());
            ee.setCatalog(entity);
            ee.setItemId(entry.getItemId());
            ee.setItemDescription(entry.getItemDescription());
            ee.setUnitCode(entry.getUnitCode());
            ee.setUnitPrice(entry.getUnitPrice());
            ee.setMinimumOrderQty(entry.getMinimumOrderQty());
            ee.setPriceBreakQty(entry.getPriceBreakQty());
            ee.setPriceBreakUnitPrice(entry.getPriceBreakUnitPrice());
            entryEntities.add(ee);
        }
        entity.setEntries(entryEntities);
        return entity;
    }

    public static VendorCatalog toDomain(VendorCatalogJpaEntity entity) {
        List<VendorCatalogEntry> entries = entity.getEntries().stream()
                .map(ee -> new VendorCatalogEntry(
                        VendorCatalogEntryId.of(ee.getId()),
                        VendorCatalogId.of(entity.getId()),
                        ee.getItemId(),
                        ee.getItemDescription(),
                        ee.getUnitCode(),
                        ee.getUnitPrice(),
                        ee.getMinimumOrderQty(),
                        ee.getPriceBreakQty(),
                        ee.getPriceBreakUnitPrice()
                ))
                .toList();

        return new VendorCatalog(
                VendorCatalogId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                entity.getVendorId(),
                entity.getVendorName(),
                entity.getCurrencyCode(),
                entity.getValidFrom(),
                entity.getValidTo(),
                entity.isActive(),
                entries
        );
    }
}

