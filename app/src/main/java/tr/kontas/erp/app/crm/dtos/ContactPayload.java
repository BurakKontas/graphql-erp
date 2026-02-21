package tr.kontas.erp.app.crm.dtos;

public record ContactPayload(
        String id,
        String companyId,
        String contactNumber,
        String contactType,
        String firstName,
        String lastName,
        String companyName,
        String jobTitle,
        String email,
        String phone,
        String website,
        String address,
        String businessPartnerId,
        String ownerId,
        String status,
        String source,
        String notes
) {}

