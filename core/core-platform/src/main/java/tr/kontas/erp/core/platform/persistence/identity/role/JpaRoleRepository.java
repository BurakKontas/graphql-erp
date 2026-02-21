package tr.kontas.erp.core.platform.persistence.identity.role;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaRoleRepository extends JpaRepository<RoleJpaEntity, UUID> {
    @EntityGraph(attributePaths = "permissions")
    List<RoleJpaEntity> findByTenantId(UUID tenantId);
}