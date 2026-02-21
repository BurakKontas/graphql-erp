package tr.kontas.erp.crm.application.contact;

import tr.kontas.erp.crm.domain.contact.Contact;
import tr.kontas.erp.crm.domain.contact.ContactId;

public interface GetContactByIdUseCase {
    Contact execute(ContactId id);
}

