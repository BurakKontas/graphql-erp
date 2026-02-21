package tr.kontas.erp.crm.domain.quote;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.crm.domain.event.QuoteCreatedEvent;
import tr.kontas.erp.crm.domain.event.QuoteAcceptedEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Quote extends AggregateRoot<QuoteId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final QuoteNumber quoteNumber;
    private String opportunityId;
    private String contactId;
    private String ownerId;
    private LocalDate quoteDate;
    private LocalDate expiryDate;
    private String currencyCode;
    private String paymentTermCode;
    private QuoteStatus status;
    private String version;
    private String previousQuoteId;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private BigDecimal discountRate;
    private String notes;
    private final List<QuoteLine> lines;

    public Quote(QuoteId id, TenantId tenantId, CompanyId companyId,
                 QuoteNumber quoteNumber, String opportunityId, String contactId,
                 String ownerId, LocalDate quoteDate, LocalDate expiryDate,
                 String currencyCode, String paymentTermCode, QuoteStatus status,
                 String version, String previousQuoteId, BigDecimal subtotal,
                 BigDecimal taxTotal, BigDecimal total, BigDecimal discountRate,
                 String notes, List<QuoteLine> lines) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.quoteNumber = quoteNumber;
        this.opportunityId = opportunityId;
        this.contactId = contactId;
        this.ownerId = ownerId;
        this.quoteDate = quoteDate;
        this.expiryDate = expiryDate;
        this.currencyCode = currencyCode;
        this.paymentTermCode = paymentTermCode;
        this.status = status;
        this.version = version;
        this.previousQuoteId = previousQuoteId;
        this.subtotal = subtotal;
        this.taxTotal = taxTotal;
        this.total = total;
        this.discountRate = discountRate;
        this.notes = notes;
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
    }


    public static Quote create(QuoteId id, TenantId tenantId, CompanyId companyId,
                               QuoteNumber number, String opportunityId, String contactId,
                               String ownerId, LocalDate quoteDate, LocalDate expiryDate,
                               String currencyCode, String paymentTermCode,
                               BigDecimal discountRate, String notes) {
        Quote q = new Quote(id, tenantId, companyId, number, opportunityId, contactId,
                ownerId, quoteDate, expiryDate, currencyCode, paymentTermCode,
                QuoteStatus.DRAFT, "v1", null,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                discountRate, notes, new ArrayList<>());
        q.registerEvent(new QuoteCreatedEvent(
                id.asUUID(), tenantId.asUUID(), companyId.asUUID(), number.getValue()));
        return q;
    }


    public void addLine(QuoteLine line) {
        if (status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Can only add lines to DRAFT quotes");
        }
        this.lines.add(line);
        recalculate();
    }


    public void removeLine(QuoteLineId lineId) {
        if (status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Can only remove lines from DRAFT quotes");
        }
        lines.removeIf(l -> l.getId().equals(lineId));
        recalculate();
    }


    public void send() {
        if (status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Can only send DRAFT quotes");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot send a quote with no lines");
        }
        this.status = QuoteStatus.SENT;
    }


    public void accept() {
        if (status != QuoteStatus.SENT) {
            throw new IllegalStateException("Can only accept SENT quotes");
        }
        this.status = QuoteStatus.ACCEPTED;
        registerEvent(new QuoteAcceptedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                quoteNumber.getValue(), opportunityId));
    }


    public void reject() {
        if (status != QuoteStatus.SENT) {
            throw new IllegalStateException("Can only reject SENT quotes");
        }
        this.status = QuoteStatus.REJECTED;
    }


    public void expire() {
        if (status != QuoteStatus.SENT) {
            throw new IllegalStateException("Can only expire SENT quotes");
        }
        this.status = QuoteStatus.EXPIRED;
    }


    public void cancel() {
        if (status != QuoteStatus.DRAFT && status != QuoteStatus.SENT) {
            throw new IllegalStateException("Can only cancel DRAFT or SENT quotes");
        }
        this.status = QuoteStatus.CANCELLED;
    }


    public void recalculate() {
        BigDecimal sub = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        for (QuoteLine line : lines) {
            line.recalculate();
            sub = sub.add(line.getLineTotal());
            tax = tax.add(line.getTaxAmount());
        }
        this.subtotal = sub;
        this.taxTotal = tax;
        this.total = sub.add(tax);
    }


    public List<QuoteLine> getLines() {
        return Collections.unmodifiableList(lines);
    }
}

