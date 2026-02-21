package tr.kontas.erp.finance.platform.persistence.creditnote;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.creditnote.*;

import java.util.ArrayList;
import java.util.List;

public class CreditNoteMapper {

    public static CreditNoteJpaEntity toEntity(CreditNote d) {
        CreditNoteJpaEntity e = new CreditNoteJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setCreditNoteNumber(d.getCreditNoteNumber());
        e.setInvoiceId(d.getInvoiceId());
        e.setCustomerId(d.getCustomerId());
        e.setCreditNoteDate(d.getCreditNoteDate());
        e.setCurrencyCode(d.getCurrencyCode());
        e.setStatus(d.getStatus().name());
        e.setTotal(d.getTotal());
        e.setAppliedAmount(d.getAppliedAmount());
        e.setReason(d.getReason());

        List<CreditNoteLineJpaEntity> lineEntities = new ArrayList<>();
        for (CreditNoteLine line : d.getLines()) {
            CreditNoteLineJpaEntity le = new CreditNoteLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setCreditNote(e);
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
            lineEntities.add(le);
        }
        e.setLines(lineEntities);
        return e;
    }

    public static CreditNote toDomain(CreditNoteJpaEntity e) {
        List<CreditNoteLine> lines = e.getLines().stream()
                .map(le -> new CreditNoteLine(
                        CreditNoteLineId.of(le.getId()),
                        CreditNoteId.of(e.getId()),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity(),
                        le.getUnitPrice(),
                        le.getTaxCode(),
                        le.getTaxRate()
                )).toList();

        return new CreditNote(
                CreditNoteId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                e.getCreditNoteNumber(),
                e.getInvoiceId(),
                e.getCustomerId(),
                e.getCreditNoteDate(),
                e.getCurrencyCode(),
                CreditNoteStatus.valueOf(e.getStatus()),
                e.getAppliedAmount(),
                e.getReason(),
                lines
        );
    }
}
