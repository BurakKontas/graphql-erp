package tr.kontas.erp.app.identity.dtos;

import java.util.List;

public record RolePayload(String id, String name, List<PermissionPayload> permissions) {
}
