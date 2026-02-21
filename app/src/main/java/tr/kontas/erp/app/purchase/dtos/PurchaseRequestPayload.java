package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseRequestPayload {
    private String id;
    private String companyId;
    private String requestNumber;
    private String requestedBy;
    private String approvedBy;
    private String requestDate;
    private String neededBy;
    private String status;
    private String notes;
    private List<PurchaseRequestLinePayload> lines;
}

