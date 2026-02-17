package tr.kontas.erp.core.platform.persistence.identity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.identity.UserAccount;
import tr.kontas.erp.core.domain.identity.repositories.UserRepository;
import tr.kontas.erp.core.domain.identity.valueobjects.ExternalIdentity;
import tr.kontas.erp.core.domain.identity.valueobjects.UserId;
import tr.kontas.erp.core.domain.identity.valueobjects.UserName;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaRepository;

    @Override
    public void save(UserAccount user) {
        jpaRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public Optional<UserAccount> findById(UserId id) {
        return jpaRepository.findById(id.asUUID())
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<UserAccount> findByUsername(
            TenantId tenantId,
            UserName username
    ) {
        return jpaRepository
                .findByTenantIdAndUsername(
                        tenantId.asUUID(),
                        username.getValue()
                )
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<Long> findAuthVersionById(UserId id) {
        return jpaRepository.findAuthVersionById(id.asUUID());
    }

    @Override
    public Optional<UserAccount> findByExternalIdentity(
            TenantId tenantId,
            ExternalIdentity externalIdentity
    ) {
        return jpaRepository
                .findByTenantIdAndAuthProviderAndExternalId(
                        tenantId.asUUID(),
                        externalIdentity.getProvider().name(),
                        externalIdentity.getExternalId()
                )
                .map(UserMapper::toDomain);
    }
}
