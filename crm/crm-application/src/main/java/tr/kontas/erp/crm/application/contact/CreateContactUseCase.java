package tr.kontas.erp.crm.application.contact;

import tr.kontas.erp.crm.domain.contact.ContactId;

public interface CreateContactUseCase {
    ContactId execute(CreateContactCommand command);
}

