package tr.kontas.erp.finance.platform.persistence.journalentry;

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
@Table(name = "journal_entries")
@Getter
@Setter
@NoArgsConstructor
public class JournalEntryJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "entry_number", nullable = false, unique = true)
    private String entryNumber;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "period_id")
    private UUID periodId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate", precision = 19, scale = 6)
    private BigDecimal exchangeRate;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<JournalEntryLineJpaEntity> lines = new ArrayList<>();
}
