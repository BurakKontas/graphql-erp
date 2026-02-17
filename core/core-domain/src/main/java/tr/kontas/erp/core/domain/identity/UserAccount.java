package tr.kontas.erp.core.domain.identity;

import lombok.Getter;
import tr.kontas.erp.core.domain.identity.events.UserCreatedEvent;
import tr.kontas.erp.core.domain.identity.valueobjects.*;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.HashSet;
import java.util.Set;

@Getter
public class UserAccount extends AggregateRoot<UserId> {

    private final TenantId tenantId;
    private final Set<RoleId> roles = new HashSet<>();
    private final UserName username;
    private PasswordHash password;
    private ExternalIdentity externalIdentity;
    private boolean active;
    private boolean locked;
    private long authVersion;

    private UserAccount(
            UserId id,
            TenantId tenantId,
            UserName username
    ) {
        super(id);
        this.tenantId = tenantId;
        this.username = username;
    }

    public static UserAccount createLocal(
            TenantId tenantId,
            UserName username,
            PasswordHash password
    ) {
        UserAccount user = new UserAccount(UserId.newId(), tenantId, username);
        user.password = password;
        user.active = true;
        user.locked = false;
        user.authVersion = 1L;
        user.registerEvent(new UserCreatedEvent(user.getId()));
        return user;
    }

    public static UserAccount reconstitute(
            UserId id,
            TenantId tenantId,
            UserName username,
            PasswordHash password,
            ExternalIdentity externalIdentity,
            boolean active,
            boolean locked,
            long authVersion,
            Set<RoleId> roles
    ) {
        UserAccount user = new UserAccount(id, tenantId, username);
        user.password = password;
        user.externalIdentity = externalIdentity;
        user.active = active;
        user.locked = locked;
        user.authVersion = authVersion;
        user.roles.addAll(roles);
        return user;
    }

    public void incrementAuthVersion() {
        this.authVersion++;
    }

    public void assignRole(RoleId roleId) {
        if (roles.add(roleId)) {
            incrementAuthVersion();
        }
    }

    public void removeRole(RoleId roleId) {
        if (roles.remove(roleId)) {
            incrementAuthVersion();
        }
    }
}
