package tr.kontas.erp.purchase.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tr.kontas.erp.core.kernel.domain.model.DomainEvent;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VendorInvoicePostedEvent extends DomainEvent {
    private UUID vendorInvoiceId;
    private UUID tenantId;
    private UUID companyId;
    private String invoiceNumber;
    private String purchaseOrderId;
    private String vendorId;
    private BigDecimal total;

    @Override
    public UUID getAggregateId() {
        return vendorInvoiceId;
    }


    @Override
    public String getAggregateType() {
        return "VendorInvoice";
    }
}