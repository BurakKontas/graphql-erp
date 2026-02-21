package tr.kontas.erp.crm.platform.persistence.contact;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.contact.*;

public class ContactMapper {

    public static ContactJpaEntity toEntity(Contact c) {
        ContactJpaEntity e = new ContactJpaEntity();
        e.setId(c.getId().asUUID());
        e.setTenantId(c.getTenantId().asUUID());
        e.setCompanyId(c.getCompanyId().asUUID());
        e.setContactNumber(c.getContactNumber().getValue());
        e.setContactType(c.getContactType().name());
        e.setFirstName(c.getFirstName());
        e.setLastName(c.getLastName());
        e.setCompanyName(c.getCompanyName());
        e.setJobTitle(c.getJobTitle());
        e.setEmail(c.getEmail());
        e.setPhone(c.getPhone());
        e.setWebsite(c.getWebsite());
        e.setAddress(c.getAddress());
        e.setBusinessPartnerId(c.getBusinessPartnerId());
        e.setOwnerId(c.getOwnerId());
        e.setStatus(c.getStatus().name());
        e.setSource(c.getSource() != null ? c.getSource().name() : null);
        e.setNotes(c.getNotes());
        return e;
    }

    public static Contact toDomain(ContactJpaEntity e) {
        return new Contact(
                ContactId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new ContactNumber(e.getContactNumber()),
                ContactType.valueOf(e.getContactType()),
                e.getFirstName(), e.getLastName(), e.getCompanyName(),
                e.getJobTitle(), e.getEmail(), e.getPhone(), e.getWebsite(),
                e.getAddress(), e.getBusinessPartnerId(), e.getOwnerId(),
                ContactStatus.valueOf(e.getStatus()),
                e.getSource() != null ? ContactSource.valueOf(e.getSource()) : null,
                e.getNotes()
        );
    }
}

