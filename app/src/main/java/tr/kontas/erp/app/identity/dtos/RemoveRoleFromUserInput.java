package tr.kontas.erp.app.identity.dtos;

import lombok.Data;

@Data
public class RemoveRoleFromUserInput {
    private String userId;
    private String roleId;
}
