package tr.kontas.erp.app.identity.dtos;

import lombok.Data;

@Data
public class AssignRoleToUserInput {
    private String userId;
    private String roleId;
}
