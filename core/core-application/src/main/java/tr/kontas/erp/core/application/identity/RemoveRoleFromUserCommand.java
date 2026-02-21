package tr.kontas.erp.core.application.identity;

public record RemoveRoleFromUserCommand(String userId, String roleId) {
}
