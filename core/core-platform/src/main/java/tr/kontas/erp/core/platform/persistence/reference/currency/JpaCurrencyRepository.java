package tr.kontas.erp.core.platform.persistence.reference.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCurrencyRepository extends JpaRepository<CurrencyJpaEntity, String> {
    List<CurrencyJpaEntity> findByActiveTrue();

    List<CurrencyJpaEntity> findByCodeIn(List<String> codes);
}
