package tr.kontas.erp.core.platform.persistence.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaTenantRepository extends JpaRepository<TenantJpaEntity, UUID> {

    @Query("SELECT t.id FROM TenantJpaEntity t WHERE t.code=:code")
    Optional<UUID> findIdByCode(@Param("code") String code);
}
