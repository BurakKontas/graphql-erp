package tr.kontas.erp.core.domain.identity.repositories;

import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.kernel.domain.repository.Repository;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;

public interface UserRepository extends Repository<UserAccount, UserId> {
    Optional<UserAccount> findById(UserId id);

    Optional<UserAccount> findByExternalIdentity(TenantId tenantId, ExternalIdentity externalIdentity);

    Optional<UserAccount> findByUsername(TenantId tenantId, UserName username);

    Optional<Long> findAuthVersionById(UserId id);

    void save(UserAccount user);
}
