package tr.kontas.erp.finance.platform.persistence.journalentry;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "journal_entry_lines")
@Getter
@Setter
@NoArgsConstructor
public class JournalEntryLineJpaEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id", nullable = false)
    private JournalEntryJpaEntity entry;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "debit_amount", precision = 19, scale = 2)
    private BigDecimal debitAmount;

    @Column(name = "credit_amount", precision = 19, scale = 2)
    private BigDecimal creditAmount;

    @Column(name = "description")
    private String description;
}
