package tr.kontas.erp.reporting.platform.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rpt_crm_opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RptCrmOpportunity {

    @Id
    @Column(name = "opportunity_id")
    private UUID opportunityId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "opportunity_number")
    private String opportunityNumber;

    private String title;

    private String stage;

    private BigDecimal probability;

    @Column(name = "expected_value")
    private BigDecimal expectedValue;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;

    @Column(name = "owner_id")
    private String ownerId;
}

