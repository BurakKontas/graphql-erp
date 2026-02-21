package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

@Data
public class CreateAccountInput {
    private String companyId;
    private String code;
    private String name;
    private String type;
    private String nature;
    private String parentAccountId;
    private Boolean systemAccount;
}

