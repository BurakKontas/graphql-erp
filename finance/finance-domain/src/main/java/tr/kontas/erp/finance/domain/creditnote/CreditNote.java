package tr.kontas.erp.finance.domain.creditnote;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.finance.domain.event.CreditNotePostedEvent;
import tr.kontas.erp.finance.domain.exception.InvalidCreditNoteStateException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class CreditNote extends AggregateRoot<CreditNoteId> {
    private static final int AMOUNT_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final String creditNoteNumber;
    private final String invoiceId;
    private final String customerId;
    private final LocalDate creditNoteDate;
    private final String currencyCode;
    private CreditNoteStatus status;
    private BigDecimal total;
    private BigDecimal appliedAmount;
    private final String reason;
    private final List<CreditNoteLine> lines;

    public CreditNote(CreditNoteId id,
                      TenantId tenantId,
                      CompanyId companyId,
                      String creditNoteNumber,
                      String invoiceId,
                      String customerId,
                      LocalDate creditNoteDate,
                      String currencyCode,
                      CreditNoteStatus status,
                      BigDecimal appliedAmount,
                      String reason,
                      List<CreditNoteLine> lines) {
        super(id);

        if (tenantId == null) throw new IllegalArgumentException("tenantId cannot be null");
        if (companyId == null) throw new IllegalArgumentException("companyId cannot be null");

        this.tenantId = tenantId;
        this.companyId = companyId;
        this.creditNoteNumber = creditNoteNumber;
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.creditNoteDate = creditNoteDate;
        this.currencyCode = currencyCode;
        this.status = status != null ? status : CreditNoteStatus.DRAFT;
        this.appliedAmount = appliedAmount != null ? appliedAmount : BigDecimal.ZERO;
        this.reason = reason;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());

        recalculateTotals();
    }

    public void post() {
        if (status != CreditNoteStatus.DRAFT) {
            throw new InvalidCreditNoteStateException(status.name(), "post");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot post a credit note with no lines");
        }
        this.status = CreditNoteStatus.POSTED;

        registerEvent(new CreditNotePostedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                creditNoteNumber, invoiceId, customerId, total, creditNoteDate
        ));
    }

    public void applyToInvoice(BigDecimal amount) {
        if (status != CreditNoteStatus.POSTED) {
            throw new InvalidCreditNoteStateException(status.name(), "applyToInvoice");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Apply amount must be positive");
        }

        this.appliedAmount = this.appliedAmount.add(amount).setScale(AMOUNT_SCALE, ROUNDING);

        if (this.appliedAmount.compareTo(this.total) >= 0) {
            this.status = CreditNoteStatus.APPLIED;
        }
    }

    public void cancel() {
        if (status != CreditNoteStatus.DRAFT) {
            throw new InvalidCreditNoteStateException(status.name(), "cancel");
        }
        this.status = CreditNoteStatus.CANCELLED;
    }

    public List<CreditNoteLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    private void recalculateTotals() {
        this.total = lines.stream()
                .map(CreditNoteLine::getLineTotalWithTax)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(AMOUNT_SCALE, ROUNDING);
    }
}

