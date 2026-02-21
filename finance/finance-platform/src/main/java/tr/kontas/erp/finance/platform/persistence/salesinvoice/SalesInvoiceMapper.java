package tr.kontas.erp.finance.platform.persistence.salesinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.salesinvoice.*;

import java.util.ArrayList;
import java.util.List;

public class SalesInvoiceMapper {
    public static SalesInvoiceJpaEntity toEntity(SalesInvoice d) {
        SalesInvoiceJpaEntity e = new SalesInvoiceJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setInvoiceNumber(d.getInvoiceNumber().getValue());
        e.setInvoiceType(d.getInvoiceType().name());
        e.setSalesOrderId(d.getSalesOrderId());
        e.setSalesOrderNumber(d.getSalesOrderNumber());
        e.setCustomerId(d.getCustomerId());
        e.setCustomerName(d.getCustomerName());
        e.setInvoiceDate(d.getInvoiceDate());
        e.setDueDate(d.getDueDate());
        e.setCurrencyCode(d.getCurrencyCode());
        e.setExchangeRate(d.getExchangeRate());
        e.setStatus(d.getStatus().name());
        e.setSubtotal(d.getSubtotal());
        e.setTaxTotal(d.getTaxTotal());
        e.setTotal(d.getTotal());
        e.setPaidAmount(d.getPaidAmount());

        List<SalesInvoiceLineJpaEntity> lineEntities = new ArrayList<>();
        for (SalesInvoiceLine line : d.getLines()) {
            SalesInvoiceLineJpaEntity le = new SalesInvoiceLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setInvoice(e);
            le.setSalesOrderLineId(line.getSalesOrderLineId());
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
            le.setRevenueAccountId(line.getRevenueAccountId());
            lineEntities.add(le);
        }
        e.setLines(lineEntities);
        return e;
    }

    public static SalesInvoice toDomain(SalesInvoiceJpaEntity e) {
        List<SalesInvoiceLine> lines = e.getLines().stream()
                .map(le -> new SalesInvoiceLine(
                        SalesInvoiceLineId.of(le.getId()),
                        SalesInvoiceId.of(e.getId()),
                        le.getSalesOrderLineId(), le.getItemId(), le.getItemDescription(),
                        le.getUnitCode(), le.getQuantity(), le.getUnitPrice(),
                        le.getTaxCode(), le.getTaxRate(), le.getRevenueAccountId()
                )).toList();

        return new SalesInvoice(
                SalesInvoiceId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                new InvoiceNumber(e.getInvoiceNumber()), InvoiceType.valueOf(e.getInvoiceType()),
                e.getSalesOrderId(), e.getSalesOrderNumber(), e.getCustomerId(), e.getCustomerName(),
                e.getInvoiceDate(), e.getDueDate(), e.getCurrencyCode(), e.getExchangeRate(),
                InvoiceStatus.valueOf(e.getStatus()), e.getPaidAmount(), lines
        );
    }
}

