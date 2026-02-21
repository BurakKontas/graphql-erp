package tr.kontas.erp.app.hr.dtos;

import lombok.Data;

@Data
public class CreateLeavePolicyInput {
    private String companyId;
    private String name;
    private String countryCode;
}
