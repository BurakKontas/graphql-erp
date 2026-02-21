package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreatePurchaseRequestInput {
    private String companyId;
    private String requestedBy;
    private String requestDate;
    private String neededBy;
    private String notes;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private String preferredVendorId;
        private String notes;
    }
}

