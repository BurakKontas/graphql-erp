package tr.kontas.erp.sales.application.salesorder;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.time.LocalDate;

public record CreateSalesOrderCommand(
        CompanyId companyId,
        String customerId,
        String currencyCode,
        String paymentTermCode,
        LocalDate orderDate
) {
}
