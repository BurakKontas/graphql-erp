package tr.kontas.erp.app.reference.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTaxInput {
    private String companyId;
    private String code;
    private String name;
    private String type;
    private BigDecimal rate;
}
