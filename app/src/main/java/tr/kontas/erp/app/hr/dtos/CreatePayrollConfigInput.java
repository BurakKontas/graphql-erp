package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreatePayrollConfigInput {
    private String companyId;
    private String countryCode;
    private String name;
    private int validYear;
    private String minimumWage;
    private String currencyCode;
}
