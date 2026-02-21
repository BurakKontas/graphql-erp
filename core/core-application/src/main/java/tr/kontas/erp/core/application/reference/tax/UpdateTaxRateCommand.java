package tr.kontas.erp.core.application.reference.tax;

import java.math.BigDecimal;

public record UpdateTaxRateCommand(String companyId, String taxCode, BigDecimal newRate) {
}
