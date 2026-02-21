package tr.kontas.erp.core.application.identity;

public record AssignRoleToUserCommand(String userId, String roleId) {
}
