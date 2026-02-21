package tr.kontas.erp.app.hr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayrollConfigPayload {
    private String id;
    private String companyId;
    private String countryCode;
    private String name;
    private int validYear;
    private String minimumWage;
    private String currencyCode;
    private boolean active;
}
