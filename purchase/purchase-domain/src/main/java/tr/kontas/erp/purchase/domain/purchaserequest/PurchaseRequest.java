package tr.kontas.erp.purchase.domain.purchaserequest;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PurchaseRequest extends AggregateRoot<PurchaseRequestId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final PurchaseRequestNumber requestNumber;
    private final String requestedBy;
    private String approvedBy;
    private final LocalDate requestDate;
    private final LocalDate neededBy;
    private PurchaseRequestStatus status;
    private final String notes;
    private final List<PurchaseRequestLine> lines;

    public PurchaseRequest(PurchaseRequestId id, TenantId tenantId, CompanyId companyId,
                           PurchaseRequestNumber requestNumber, String requestedBy,
                           String approvedBy, LocalDate requestDate, LocalDate neededBy,
                           PurchaseRequestStatus status, String notes,
                           List<PurchaseRequestLine> lines) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.requestNumber = requestNumber;
        this.requestedBy = requestedBy;
        this.approvedBy = approvedBy;
        this.requestDate = requestDate;
        this.neededBy = neededBy;
        this.status = status;
        this.notes = notes;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }


    public List<PurchaseRequestLine> getLines() {
        return Collections.unmodifiableList(lines);
    }


    public void submit() {
        if (status != PurchaseRequestStatus.DRAFT) {
            throw new IllegalStateException("Can only submit DRAFT purchase requests");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot submit a purchase request with no lines");
        }
        this.status = PurchaseRequestStatus.SUBMITTED;
    }


    public void approve(String approverId) {
        if (status != PurchaseRequestStatus.SUBMITTED) {
            throw new IllegalStateException("Can only approve SUBMITTED purchase requests");
        }
        this.approvedBy = approverId;
        this.status = PurchaseRequestStatus.APPROVED;
    }


    public void reject(String approverId, String reason) {
        if (status != PurchaseRequestStatus.SUBMITTED) {
            throw new IllegalStateException("Can only reject SUBMITTED purchase requests");
        }
        this.approvedBy = approverId;
        this.status = PurchaseRequestStatus.REJECTED;
    }


    public void markAsConverted() {
        if (status != PurchaseRequestStatus.APPROVED) {
            throw new IllegalStateException("Can only convert APPROVED purchase requests");
        }
        this.status = PurchaseRequestStatus.CONVERTED;
    }


    public void cancel() {
        if (status != PurchaseRequestStatus.DRAFT && status != PurchaseRequestStatus.SUBMITTED) {
            throw new IllegalStateException("Can only cancel DRAFT or SUBMITTED purchase requests");
        }
        this.status = PurchaseRequestStatus.CANCELLED;
    }
}

