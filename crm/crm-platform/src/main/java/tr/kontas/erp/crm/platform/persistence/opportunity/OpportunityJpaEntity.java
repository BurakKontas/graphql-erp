package tr.kontas.erp.crm.platform.persistence.opportunity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "crm_opportunities")
@Getter
@Setter
@NoArgsConstructor
public class OpportunityJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "opportunity_number", nullable = false)
    private String opportunityNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contact_id")
    private String contactId;

    @Column(name = "lead_id")
    private String leadId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "stage", nullable = false)
    private String stage;

    @Column(name = "probability")
    private BigDecimal probability;

    @Column(name = "expected_value")
    private BigDecimal expectedValue;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;

    @Column(name = "sales_order_id")
    private String salesOrderId;

    @Column(name = "lost_reason")
    private String lostReason;

    @Column(name = "notes")
    private String notes;
}

