package tr.kontas.erp.crm.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.crm.application.contact.*;
import tr.kontas.erp.crm.application.port.ContactNumberGeneratorPort;
import tr.kontas.erp.crm.domain.contact.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService implements CreateContactUseCase, GetContactByIdUseCase,
        GetContactsByCompanyUseCase, GetContactsByIdsUseCase {

    private final ContactRepository contactRepository;
    private final ContactNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public ContactId execute(CreateContactCommand cmd) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = cmd.companyId();
        ContactNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());
        ContactId id = ContactId.newId();
        Contact contact = Contact.create(id, tenantId, companyId, number,
                ContactType.valueOf(cmd.contactType()),
                cmd.firstName(), cmd.lastName(), cmd.companyName(),
                cmd.jobTitle(), cmd.email(), cmd.phone(), cmd.website(),
                cmd.address(), cmd.ownerId(),
                cmd.source() != null ? ContactSource.valueOf(cmd.source()) : null,
                cmd.notes());
        contactRepository.save(contact);
        contact.getDomainEvents().forEach(eventPublisher::publish);
        contact.clearDomainEvents();
        return id;
    }

    @Override
    public Contact execute(ContactId id) {
        TenantId tenantId = TenantContext.get();
        return contactRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found: " + id));
    }

    @Override
    public List<Contact> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return contactRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<Contact> execute(List<ContactId> ids) {
        return contactRepository.findByIds(ids);
    }
}



