package tr.kontas.erp.crm.platform.persistence.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.contact.*;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContactRepositoryImpl implements ContactRepository {

    private final JpaContactRepository jpa;

    @Override
    public void save(Contact entity) {
        jpa.save(ContactMapper.toEntity(entity));
    }

    @Override
    public Optional<Contact> findById(ContactId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(ContactMapper::toDomain);
    }

    @Override
    public List<Contact> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(ContactMapper::toDomain).toList();
    }

    @Override
    public List<Contact> findByIds(List<ContactId> ids) {
        return jpa.findByIdIn(ids.stream().map(ContactId::asUUID).toList())
                .stream().map(ContactMapper::toDomain).toList();
    }
}

