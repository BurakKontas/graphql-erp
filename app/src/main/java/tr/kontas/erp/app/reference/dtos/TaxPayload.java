package tr.kontas.erp.app.reference.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TaxPayload {
    private String code;
    private String name;
    private String type;
    private BigDecimal rate;
    private boolean active;
    private String companyId;
}
