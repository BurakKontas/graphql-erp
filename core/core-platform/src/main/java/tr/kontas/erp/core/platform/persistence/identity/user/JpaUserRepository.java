package tr.kontas.erp.core.platform.persistence.identity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByTenantIdAndUsername(UUID tenantId, String username);

    Optional<UserJpaEntity> findByTenantIdAndAuthProviderAndExternalId(UUID tenantId, String authProvider, String externalId);

    @Query("select u.authVersion from UserJpaEntity u where u.id = :id")
    Optional<Long> findAuthVersionById(@Param("id") UUID id);
}
