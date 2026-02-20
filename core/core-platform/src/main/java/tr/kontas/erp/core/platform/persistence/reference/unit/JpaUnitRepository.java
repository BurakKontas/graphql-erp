package tr.kontas.erp.core.platform.persistence.reference.unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUnitRepository extends JpaRepository<UnitJpaEntity, String> {
    List<UnitJpaEntity> findByActiveTrue();

    List<UnitJpaEntity> findByCodeIn(List<String> codes);
}
