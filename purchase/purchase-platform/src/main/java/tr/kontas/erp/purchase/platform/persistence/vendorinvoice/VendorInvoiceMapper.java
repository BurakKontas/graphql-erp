package tr.kontas.erp.purchase.platform.persistence.vendorinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.vendorinvoice.*;

import java.util.ArrayList;
import java.util.List;

public class VendorInvoiceMapper {

    public static VendorInvoiceJpaEntity toEntity(VendorInvoice domain) {
        VendorInvoiceJpaEntity entity = new VendorInvoiceJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setInvoiceNumber(domain.getInvoiceNumber().getValue());
        entity.setVendorInvoiceRef(domain.getVendorInvoiceRef());
        entity.setPurchaseOrderId(domain.getPurchaseOrderId());
        entity.setVendorId(domain.getVendorId());
        entity.setVendorName(domain.getVendorName());
        entity.setAccountingPeriodId(domain.getAccountingPeriodId());
        entity.setInvoiceDate(domain.getInvoiceDate());
        entity.setDueDate(domain.getDueDate());
        entity.setCurrencyCode(domain.getCurrencyCode());
        entity.setExchangeRate(domain.getExchangeRate());
        entity.setStatus(domain.getStatus().name());
        entity.setSubtotal(domain.getSubtotal());
        entity.setTaxTotal(domain.getTaxTotal());
        entity.setTotal(domain.getTotal());
        entity.setPaidAmount(domain.getPaidAmount());

        List<VendorInvoiceLineJpaEntity> lineEntities = new ArrayList<>();
        for (VendorInvoiceLine line : domain.getLines()) {
            VendorInvoiceLineJpaEntity le = new VendorInvoiceLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setInvoice(entity);
            le.setPoLineId(line.getPoLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setQuantity(line.getQuantity());
            le.setUnitPrice(line.getUnitPrice());
            le.setTaxCode(line.getTaxCode());
            le.setTaxRate(line.getTaxRate());
            le.setLineTotal(line.getLineTotal());
            le.setTaxAmount(line.getTaxAmount());
            le.setLineTotalWithTax(line.getLineTotalWithTax());
            le.setAccountId(line.getAccountId());
            lineEntities.add(le);
        }
        entity.setLines(lineEntities);
        return entity;
    }

    public static VendorInvoice toDomain(VendorInvoiceJpaEntity entity) {
        List<VendorInvoiceLine> lines = entity.getLines().stream()
                .map(le -> new VendorInvoiceLine(
                        VendorInvoiceLineId.of(le.getId()),
                        VendorInvoiceId.of(entity.getId()),
                        le.getPoLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity(),
                        le.getUnitPrice(),
                        le.getTaxCode(),
                        le.getTaxRate(),
                        le.getAccountId()
                ))
                .toList();

        return new VendorInvoice(
                VendorInvoiceId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new VendorInvoiceNumber(entity.getInvoiceNumber()),
                entity.getVendorInvoiceRef(),
                entity.getPurchaseOrderId(),
                entity.getVendorId(),
                entity.getVendorName(),
                entity.getAccountingPeriodId(),
                entity.getInvoiceDate(),
                entity.getDueDate(),
                entity.getCurrencyCode(),
                entity.getExchangeRate(),
                VendorInvoiceStatus.valueOf(entity.getStatus()),
                entity.getPaidAmount(),
                lines
        );
    }
}

