package tr.kontas.erp.crm.platform.persistence.lead;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "crm_leads")
@Getter
@Setter
@NoArgsConstructor
public class LeadJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "lead_number", nullable = false)
    private String leadNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contact_id")
    private String contactId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "source")
    private String source;

    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;

    @Column(name = "disqualification_reason")
    private String disqualificationReason;

    @Column(name = "notes")
    private String notes;

    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;

    @Column(name = "opportunity_id")
    private String opportunityId;
}

