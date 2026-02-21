package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountPayload {
    private String id;
    private String companyId;
    private String code;
    private String name;
    private String type;
    private String nature;
    private String parentAccountId;
    private boolean systemAccount;
    private boolean active;
}

