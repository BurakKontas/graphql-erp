package tr.kontas.erp.purchase.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.purchase.application.vendorcatalog.*;
import tr.kontas.erp.purchase.domain.vendorcatalog.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorCatalogService implements
        CreateVendorCatalogUseCase,
        GetVendorCatalogByIdUseCase,
        GetVendorCatalogsByCompanyUseCase,
        GetVendorCatalogsByIdsUseCase,
        DeactivateVendorCatalogUseCase {

    private final VendorCatalogRepository vendorCatalogRepository;

    @Override
    @Transactional
    public VendorCatalogId execute(CreateVendorCatalogCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        List<VendorCatalogEntry> entries = command.entries() != null
                ? command.entries().stream()
                .map(ec -> new VendorCatalogEntry(
                        VendorCatalogEntryId.newId(), null,
                        ec.itemId(), ec.itemDescription(), ec.unitCode(),
                        ec.unitPrice(), ec.minimumOrderQty(),
                        ec.priceBreakQty(), ec.priceBreakUnitPrice()
                ))
                .toList()
                : List.of();

        VendorCatalogId id = VendorCatalogId.newId();
        VendorCatalog catalog = new VendorCatalog(
                id, tenantId, companyId,
                command.vendorId(), command.vendorName(), command.currencyCode(),
                command.validFrom(), command.validTo(), true, entries
        );

        vendorCatalogRepository.save(catalog);
        return id;
    }

    @Override
    public VendorCatalog execute(VendorCatalogId id) {
        TenantId tenantId = TenantContext.get();
        return vendorCatalogRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("VendorCatalog not found: " + id));
    }

    @Override
    public List<VendorCatalog> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return vendorCatalogRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<VendorCatalog> execute(List<VendorCatalogId> ids) {
        return vendorCatalogRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void deactivate(String catalogId) {
        TenantId tenantId = TenantContext.get();
        VendorCatalog catalog = vendorCatalogRepository.findById(VendorCatalogId.of(catalogId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("VendorCatalog not found: " + catalogId));
        catalog.deactivate();
        vendorCatalogRepository.save(catalog);
    }
}

