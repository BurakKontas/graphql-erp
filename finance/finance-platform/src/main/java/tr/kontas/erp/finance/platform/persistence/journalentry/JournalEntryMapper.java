package tr.kontas.erp.finance.platform.persistence.journalentry;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.accountingperiod.AccountingPeriodId;
import tr.kontas.erp.finance.domain.journalentry.*;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryMapper {
    public static JournalEntryJpaEntity toEntity(JournalEntry d) {
        JournalEntryJpaEntity e = new JournalEntryJpaEntity();
        e.setId(d.getId().asUUID());
        e.setTenantId(d.getTenantId().asUUID());
        e.setCompanyId(d.getCompanyId().asUUID());
        e.setEntryNumber(d.getEntryNumber().getValue());
        e.setType(d.getType().name());
        e.setPeriodId(d.getPeriodId() != null ? d.getPeriodId().asUUID() : null);
        e.setEntryDate(d.getEntryDate());
        e.setDescription(d.getDescription());
        e.setReferenceType(d.getReferenceType());
        e.setReferenceId(d.getReferenceId());
        e.setCurrencyCode(d.getCurrencyCode());
        e.setExchangeRate(d.getExchangeRate());
        e.setStatus(d.getStatus().name());

        List<JournalEntryLineJpaEntity> lineEntities = new ArrayList<>();
        for (JournalEntryLine line : d.getLines()) {
            JournalEntryLineJpaEntity le = new JournalEntryLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setEntry(e);
            le.setAccountId(line.getAccountId());
            le.setAccountCode(line.getAccountCode());
            le.setAccountName(line.getAccountName());
            le.setDebitAmount(line.getDebitAmount());
            le.setCreditAmount(line.getCreditAmount());
            le.setDescription(line.getDescription());
            lineEntities.add(le);
        }
        e.setLines(lineEntities);
        return e;
    }

    public static JournalEntry toDomain(JournalEntryJpaEntity e) {
        List<JournalEntryLine> lines = e.getLines().stream()
                .map(le -> new JournalEntryLine(
                        JournalEntryLineId.of(le.getId()),
                        JournalEntryId.of(e.getId()),
                        le.getAccountId(),
                        le.getAccountCode(),
                        le.getAccountName(),
                        le.getDebitAmount(),
                        le.getCreditAmount(),
                        le.getDescription()
                ))
                .toList();

        return new JournalEntry(
                JournalEntryId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                CompanyId.of(e.getCompanyId()),
                new JournalEntryNumber(e.getEntryNumber()),
                JournalEntryType.valueOf(e.getType()),
                e.getPeriodId() != null ? AccountingPeriodId.of(e.getPeriodId()) : null,
                e.getEntryDate(),
                e.getDescription(),
                e.getReferenceType(),
                e.getReferenceId(),
                e.getCurrencyCode(),
                e.getExchangeRate(),
                JournalEntryStatus.valueOf(e.getStatus()),
                lines
        );
    }
}

