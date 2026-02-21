package tr.kontas.erp.crm.domain.contact;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    void save(Contact entity);
    Optional<Contact> findById(ContactId id, TenantId tenantId);
    List<Contact> findByCompanyId(TenantId tenantId, CompanyId companyId);
    List<Contact> findByIds(List<ContactId> ids);
}

