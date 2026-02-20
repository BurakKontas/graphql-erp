package tr.kontas.erp.core.platform.persistence.reference.currency;

import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.currency.CurrencyCode;

public class CurrencyMapper {

    public static Currency toDomain(CurrencyJpaEntity entity) {
        Currency currency = new Currency(
                new CurrencyCode(entity.getCode()),
                entity.getName(),
                entity.getSymbol(),
                entity.getFractionDigits()
        );
        if (!entity.isActive()) {
            currency.deactivate();
        }
        return currency;
    }

    public static CurrencyJpaEntity toEntity(Currency domain) {
        return CurrencyJpaEntity.builder()
                .code(domain.getId().getValue())
                .name(domain.getName())
                .symbol(domain.getSymbol())
                .fractionDigits(domain.getFractionDigits())
                .active(domain.isActive())
                .build();
    }
}
