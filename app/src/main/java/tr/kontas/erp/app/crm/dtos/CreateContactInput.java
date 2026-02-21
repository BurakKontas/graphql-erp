package tr.kontas.erp.app.crm.dtos;

public record CreateContactInput(
        String companyId,
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

