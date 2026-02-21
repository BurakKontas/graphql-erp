package tr.kontas.erp.crm.application.contact;

import tr.kontas.erp.core.domain.company.CompanyId;

public record CreateContactCommand(
        CompanyId companyId,
        String contactType,
        String firstName,
        String lastName,
        String companyName,
        String jobTitle,
        String email,
        String phone,
        String website,
        String address,
        String ownerId,
        String source,
        String notes
) {}

