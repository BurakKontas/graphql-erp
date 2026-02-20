package tr.kontas.erp.app.reference.dtos;

import lombok.Data;

@Data
public class CreatePaymentTermInput {
    private String companyId;
    private String code;
    private String name;
    private int dueDays;
}
