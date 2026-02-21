package tr.kontas.erp.app.salesorder.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class SalesOrderPayload {
    private String id;
    private String companyId;
    private String orderNumber;
    private String orderDate;
    private String expiryDate;
    private String customerId;
    private String currencyCode;
    private String paymentTermCode;
    private ShippingAddressPayload shippingAddress;
    private String status;
    private String fulfillmentStatus;
    private String invoicingStatus;
    private BigDecimal invoicedAmount;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private List<SalesOrderLinePayload> lines;
}
