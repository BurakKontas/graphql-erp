package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;

@Data
public class CreateSalesOrderInput {
    private String companyId;
    private String customerId;
    private String currencyCode;
    private String paymentTermCode;
    private String orderDate;
}
