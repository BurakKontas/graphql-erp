package tr.kontas.erp.core.platform.persistence.identity.permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPermissionRepository extends JpaRepository<PermissionJpaEntity, UUID> {
    Optional<PermissionJpaEntity> findByModuleAndAction(String module, String action);

    List<PermissionJpaEntity> findByIdIn(Collection<UUID> ids);
}
