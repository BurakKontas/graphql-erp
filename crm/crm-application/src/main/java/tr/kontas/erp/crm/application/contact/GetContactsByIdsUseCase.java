package tr.kontas.erp.crm.application.contact;

import tr.kontas.erp.crm.domain.contact.Contact;
import tr.kontas.erp.crm.domain.contact.ContactId;

import java.util.List;

public interface GetContactsByIdsUseCase {
    List<Contact> execute(List<ContactId> ids);
}

