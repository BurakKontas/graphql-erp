package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GoodsReceiptLinePayload {
    private String id;
    private String poLineId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private String batchNote;
}

