package tr.kontas.erp.core.platform.service.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.application.identity.*;
import tr.kontas.erp.core.domain.identity.Permission;
import tr.kontas.erp.core.domain.identity.Role;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.repositories.PermissionRepository;
import tr.kontas.erp.core.domain.identity.repositories.RoleRepository;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.RoleId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolePermissionService implements
        GetRolesByTenantUseCase,
        GetPermissionsUseCase,
        AssignRoleToUserUseCase,
        RemoveRoleFromUserUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> execute(TenantId tenantId) {
        return roleRepository.findByTenantId(tenantId);
    }

    @Override
    public List<Permission> execute() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional
    public void execute(AssignRoleToUserCommand command) {
        UserId userId = UserId.of(UUID.fromString(command.userId()));
        RoleId roleId = RoleId.of(UUID.fromString(command.roleId()));

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + command.userId()));

        roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + command.roleId()));

        user.assignRole(roleId);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void execute(RemoveRoleFromUserCommand command) {
        UserId userId = UserId.of(UUID.fromString(command.userId()));
        RoleId roleId = RoleId.of(UUID.fromString(command.roleId()));

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + command.userId()));

        user.removeRole(roleId);
        userRepository.save(user);
    }
}
