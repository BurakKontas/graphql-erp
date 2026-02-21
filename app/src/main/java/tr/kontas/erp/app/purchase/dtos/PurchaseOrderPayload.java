package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseOrderPayload {
    private String id;
    private String companyId;
    private String orderNumber;
    private String requestId;
    private String vendorId;
    private String vendorName;
    private String orderDate;
    private String expectedDeliveryDate;
    private String currencyCode;
    private String paymentTermCode;
    private AddressPayload deliveryAddress;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private List<PurchaseOrderLinePayload> lines;
}

