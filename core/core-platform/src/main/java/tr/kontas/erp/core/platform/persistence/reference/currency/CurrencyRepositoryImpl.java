package tr.kontas.erp.core.platform.persistence.reference.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;
import tr.kontas.erp.core.domain.reference.currency.CurrencyRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CurrencyRepositoryImpl implements CurrencyRepository {

    private final JpaCurrencyRepository jpaRepository;

    @Override
    public Optional<Currency> findByCode(CurrencyCode code) {
        return jpaRepository.findById(code.getValue())
                .map(CurrencyMapper::toDomain);
    }

    @Override
    public List<Currency> findAllActive() {
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(CurrencyMapper::toDomain)
                .toList();
    }

    public List<Currency> findByCodes(List<CurrencyCode> codes) {
        List<String> codeStrings = codes.stream()
                .map(c -> c.getValue())
                .toList();
        return jpaRepository.findByCodeIn(codeStrings)
                .stream()
                .map(CurrencyMapper::toDomain)
                .toList();
    }

    @Override
    public void save(Currency currency) {
        jpaRepository.save(CurrencyMapper.toEntity(currency));
    }
}
