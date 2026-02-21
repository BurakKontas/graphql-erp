package tr.kontas.erp.crm.platform.persistence.contact;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "crm_contacts")
@Getter
@Setter
@NoArgsConstructor
public class ContactJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "contact_type", nullable = false)
    private String contactType;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @Column(name = "address")
    private String address;

    @Column(name = "business_partner_id")
    private String businessPartnerId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "source")
    private String source;

    @Column(name = "notes")
    private String notes;
}

