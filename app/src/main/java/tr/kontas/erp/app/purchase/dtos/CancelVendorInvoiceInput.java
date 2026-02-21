package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;

@Data
public class CancelVendorInvoiceInput {
    private String invoiceId;
    private String reason;
}

