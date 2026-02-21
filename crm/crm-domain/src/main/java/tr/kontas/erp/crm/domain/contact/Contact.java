package tr.kontas.erp.crm.domain.contact;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.event.ContactCreatedEvent;
import tr.kontas.erp.crm.domain.event.ContactConvertedEvent;

@Getter
public class Contact extends AggregateRoot<ContactId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final ContactNumber contactNumber;
    private ContactType contactType;
    private String firstName;
    private String lastName;
    private String companyName;
    private String jobTitle;
    private String email;
    private String phone;
    private String website;
    private String address;
    private String businessPartnerId;
    private String ownerId;
    private ContactStatus status;
    private ContactSource source;
    private String notes;

    public Contact(ContactId id, TenantId tenantId, CompanyId companyId,
                   ContactNumber contactNumber, ContactType contactType,
                   String firstName, String lastName, String companyName,
                   String jobTitle, String email, String phone, String website,
                   String address, String businessPartnerId, String ownerId,
                   ContactStatus status, ContactSource source, String notes) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.contactNumber = contactNumber;
        this.contactType = contactType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.businessPartnerId = businessPartnerId;
        this.ownerId = ownerId;
        this.status = status;
        this.source = source;
        this.notes = notes;
    }


    public static Contact create(ContactId id, TenantId tenantId, CompanyId companyId,
                                 ContactNumber number, ContactType type,
                                 String firstName, String lastName, String companyName,
                                 String jobTitle, String email, String phone, String website,
                                 String address, String ownerId, ContactSource source, String notes) {
        Contact c = new Contact(id, tenantId, companyId, number, type,
                firstName, lastName, companyName, jobTitle, email, phone, website,
                address, null, ownerId, ContactStatus.ACTIVE, source, notes);
        c.registerEvent(new ContactCreatedEvent(
                id.asUUID(), tenantId.asUUID(), companyId.asUUID(), number.getValue(), type.name()));
        return c;
    }


    public void update(String firstName, String lastName, String companyName,
                       String jobTitle, String email, String phone, String website,
                       String address, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.notes = notes;
    }


    public void convertToBusinessPartner(String businessPartnerId) {
        if (status != ContactStatus.ACTIVE) {
            throw new IllegalStateException("Can only convert ACTIVE contacts");
        }
        this.businessPartnerId = businessPartnerId;
        this.status = ContactStatus.CONVERTED;
        registerEvent(new ContactConvertedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                contactNumber.getValue(), businessPartnerId));
    }


    public void archive() {
        if (status != ContactStatus.ACTIVE) {
            throw new IllegalStateException("Can only archive ACTIVE contacts");
        }
        this.status = ContactStatus.ARCHIVED;
    }
}

