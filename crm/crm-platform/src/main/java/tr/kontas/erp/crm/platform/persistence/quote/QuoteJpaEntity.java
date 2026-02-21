package tr.kontas.erp.crm.platform.persistence.quote;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "crm_quotes")
@Getter
@Setter
@NoArgsConstructor
public class QuoteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "quote_number", nullable = false)
    private String quoteNumber;

    @Column(name = "opportunity_id")
    private String opportunityId;

    @Column(name = "contact_id")
    private String contactId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "quote_date")
    private LocalDate quoteDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "payment_term_code")
    private String paymentTermCode;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "version")
    private String version;

    @Column(name = "previous_quote_id")
    private String previousQuoteId;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "tax_total")
    private BigDecimal taxTotal;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "lines_json", columnDefinition = "TEXT")
    private String linesJson;
}

