package tr.kontas.erp.purchase.domain.vendorinvoice;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.event.VendorInvoicePostedEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Getter
public class VendorInvoice extends AggregateRoot<VendorInvoiceId> {
    private final TenantId tenantId;
    private final CompanyId companyId;
    private final VendorInvoiceNumber invoiceNumber;
    private final String vendorInvoiceRef;
    private final String purchaseOrderId;
    private final String vendorId;
    private final String vendorName;
    private final String accountingPeriodId;
    private final LocalDate invoiceDate;
    private final LocalDate dueDate;
    private final String currencyCode;
    private final BigDecimal exchangeRate;
    private VendorInvoiceStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private final List<VendorInvoiceLine> lines;
    public VendorInvoice(VendorInvoiceId id, TenantId tenantId, CompanyId companyId, VendorInvoiceNumber invoiceNumber, String vendorInvoiceRef, String purchaseOrderId, String vendorId, String vendorName, String accountingPeriodId, LocalDate invoiceDate, LocalDate dueDate, String currencyCode, BigDecimal exchangeRate, VendorInvoiceStatus status, BigDecimal paidAmount, List<VendorInvoiceLine> lines) {
        super(id);
        this.tenantId = tenantId; this.companyId = companyId; this.invoiceNumber = invoiceNumber; this.vendorInvoiceRef = vendorInvoiceRef; this.purchaseOrderId = purchaseOrderId; this.vendorId = vendorId; this.vendorName = vendorName; this.accountingPeriodId = accountingPeriodId; this.invoiceDate = invoiceDate; this.dueDate = dueDate; this.currencyCode = currencyCode; this.exchangeRate = exchangeRate; this.status = status; this.paidAmount = paidAmount != null ? paidAmount : BigDecimal.ZERO; this.lines = new ArrayList<>(lines != null ? lines : List.of()); recalculate();
    }

    public List<VendorInvoiceLine> getLines() { return Collections.unmodifiableList(lines); }

    public BigDecimal getRemainingAmount() { return total.subtract(paidAmount); }

    public void post() {
        if (status != VendorInvoiceStatus.DRAFT) throw new IllegalStateException("Can only post DRAFT vendor invoices");
        this.status = VendorInvoiceStatus.POSTED;
        registerEvent(new VendorInvoicePostedEvent(getId().asUUID(), tenantId.asUUID(), companyId.asUUID(), invoiceNumber.getValue(), purchaseOrderId, vendorId, total));
    }

    public void recordPayment(BigDecimal amount) {
        this.paidAmount = this.paidAmount.add(amount);
        if (getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) { this.status = VendorInvoiceStatus.PAID; }
        else if (this.paidAmount.compareTo(BigDecimal.ZERO) > 0) { this.status = VendorInvoiceStatus.PARTIALLY_PAID; }
    }

    public void cancel(String reason) {
        if (status == VendorInvoiceStatus.DRAFT || (status == VendorInvoiceStatus.POSTED && paidAmount.compareTo(BigDecimal.ZERO) == 0)) { this.status = VendorInvoiceStatus.CANCELLED; }
        else { throw new IllegalStateException("Cannot cancel vendor invoice in status: " + status); }
    }

    private void recalculate() {
        this.subtotal = lines.stream().map(VendorInvoiceLine::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxTotal = lines.stream().map(VendorInvoiceLine::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.total = subtotal.add(taxTotal);
    }
}