package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CreditNotePayload {
    private String id;
    private String companyId;
    private String creditNoteNumber;
    private String invoiceId;
    private String customerId;
    private String creditNoteDate;
    private String currencyCode;
    private String status;
    private BigDecimal total;
    private BigDecimal appliedAmount;
    private String reason;
    private List<CreditNoteLinePayload> lines;
}

