package tr.kontas.erp.finance.platform.persistence.creditnote;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit_notes")
@Getter
@Setter
@NoArgsConstructor
public class CreditNoteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "credit_note_number", nullable = false, unique = true)
    private String creditNoteNumber;

    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "credit_note_date", nullable = false)
    private LocalDate creditNoteDate;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total", precision = 19, scale = 2)
    private BigDecimal total;

    @Column(name = "applied_amount", precision = 19, scale = 2)
    private BigDecimal appliedAmount;

    @Column(name = "reason")
    private String reason;

    @OneToMany(mappedBy = "creditNote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CreditNoteLineJpaEntity> lines = new ArrayList<>();
}
