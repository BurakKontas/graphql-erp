package tr.kontas.erp.purchase.application.purchaseorder;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreatePurchaseOrderCommand(
        CompanyId companyId,
        String requestId,
        String vendorId,
        String vendorName,
        LocalDate orderDate,
        LocalDate expectedDeliveryDate,
        String currencyCode,
        String paymentTermCode,
        String addressLine1,
        String addressLine2,
        String city,
        String stateOrProvince,
        String postalCode,
        String countryCode,
        List<LineCommand> lines
) {
    public record LineCommand(
            String requestLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal orderedQty,
            BigDecimal unitPrice,
            String taxCode,
            BigDecimal taxRate,
            String expenseAccountId
    ) {
    }
}

