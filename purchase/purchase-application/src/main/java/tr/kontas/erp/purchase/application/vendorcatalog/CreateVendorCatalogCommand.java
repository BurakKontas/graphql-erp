package tr.kontas.erp.purchase.application.vendorcatalog;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateVendorCatalogCommand(
        CompanyId companyId,
        String vendorId,
        String vendorName,
        String currencyCode,
        LocalDate validFrom,
        LocalDate validTo,
        List<EntryCommand> entries
) {
    public record EntryCommand(
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal unitPrice,
            BigDecimal minimumOrderQty,
            BigDecimal priceBreakQty,
            BigDecimal priceBreakUnitPrice
    ) {
    }
}

