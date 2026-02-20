package tr.kontas.erp.core.domain.reference.currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {
    Optional<Currency> findByCode(CurrencyCode code);

    List<Currency> findAllActive();

    void save(Currency currency);
}