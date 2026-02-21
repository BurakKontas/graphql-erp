package tr.kontas.erp.app.identity.graphql;

import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.identity.dtos.*;
import tr.kontas.erp.core.application.identity.*;
import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.domain.identity.repositories.PermissionRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.PermissionId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.context.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class RolePermissionGraphql {

    private final GetRolesByTenantUseCase getRolesByTenantUseCase;
    private final GetPermissionsUseCase getPermissionsUseCase;
    private final AssignRoleToUserUseCase assignRoleToUserUseCase;
    private final RemoveRoleFromUserUseCase removeRoleFromUserUseCase;
    private final PermissionRepository permissionRepository;

    // ─── Queries ───

    @DgsQuery
    public List<RolePayload> roles(DataFetchingEnvironment env) {
        Context context = DgsContext.getCustomContext(env);
        UUID tenantUuid = context.tenantId();

        List<Role> roles = getRolesByTenantUseCase.execute(TenantId.of(tenantUuid));

        // Collect all permission IDs to batch-load
        List<PermissionId> allPermIds = roles.stream()
                .flatMap(r -> r.getPermissions().stream())
                .distinct()
                .toList();

        Map<UUID, Permission> permMap = permissionRepository.findAllByIds(allPermIds)
                .stream()
                .collect(Collectors.toMap(p -> p.id().asUUID(), p -> p));

        return roles.stream().map(role -> {
            List<PermissionPayload> perms = role.getPermissions().stream()
                    .map(pid -> permMap.get(pid.asUUID()))
                    .filter(Objects::nonNull)
                    .map(p -> new PermissionPayload(
                            p.id().asUUID().toString(),
                            p.key().getModule().getValue(),
                            p.key().getAction().getValue(),
                            p.asKey()
                    ))
                    .toList();

            return new RolePayload(
                    role.getId().asUUID().toString(),
                    role.getName().getValue(),
                    perms
            );
        }).toList();
    }

    @DgsQuery
    public List<PermissionPayload> permissions() {
        return getPermissionsUseCase.execute().stream()
                .map(p -> new PermissionPayload(
                        p.id().asUUID().toString(),
                        p.key().getModule().getValue(),
                        p.key().getAction().getValue(),
                        p.asKey()
                ))
                .toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public boolean assignRoleToUser(@InputArgument("input") AssignRoleToUserInput input) {
        assignRoleToUserUseCase.execute(
                new AssignRoleToUserCommand(input.getUserId(), input.getRoleId())
        );
        return true;
    }

    @DgsMutation
    public boolean removeRoleFromUser(@InputArgument("input") RemoveRoleFromUserInput input) {
        removeRoleFromUserUseCase.execute(
                new RemoveRoleFromUserCommand(input.getUserId(), input.getRoleId())
        );
        return true;
    }
}
