package tr.kontas.erp.crm.platform.persistence.quote;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.quote.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuoteRepositoryImpl implements QuoteRepository {

    private final JpaQuoteRepository jpa;

    @Override
    public void save(Quote entity) {
        jpa.save(QuoteMapper.toEntity(entity));
    }

    @Override
    public Optional<Quote> findById(QuoteId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(QuoteMapper::toDomain);
    }

    @Override
    public List<Quote> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(QuoteMapper::toDomain).toList();
    }

    @Override
    public List<Quote> findByIds(List<QuoteId> ids) {
        return jpa.findByIdIn(ids.stream().map(QuoteId::asUUID).toList())
                .stream().map(QuoteMapper::toDomain).toList();
    }
}

