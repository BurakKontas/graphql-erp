package tr.kontas.erp.app.reference.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTaxRateInput {
    private String companyId;
    private String taxCode;
    private BigDecimal newRate;
}
