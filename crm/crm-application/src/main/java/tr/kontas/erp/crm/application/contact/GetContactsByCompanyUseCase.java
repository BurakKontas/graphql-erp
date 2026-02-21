package tr.kontas.erp.crm.application.contact;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.crm.domain.contact.Contact;

import java.util.List;

public interface GetContactsByCompanyUseCase {
    List<Contact> execute(CompanyId companyId);
}

