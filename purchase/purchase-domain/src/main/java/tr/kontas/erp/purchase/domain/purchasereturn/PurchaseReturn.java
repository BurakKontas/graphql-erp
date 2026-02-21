package tr.kontas.erp.purchase.domain.purchasereturn;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.event.PurchaseReturnPostedEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PurchaseReturn extends AggregateRoot<PurchaseReturnId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final PurchaseReturnNumber returnNumber;
    private final String purchaseOrderId;
    private final String goodsReceiptId;
    private final String vendorId;
    private final String warehouseId;
    private final LocalDate returnDate;
    private final ReturnReason reason;
    private PurchaseReturnStatus status;
    private final List<PurchaseReturnLine> lines;

    public PurchaseReturn(PurchaseReturnId id, TenantId tenantId, CompanyId companyId,
                          PurchaseReturnNumber returnNumber, String purchaseOrderId,
                          String goodsReceiptId, String vendorId, String warehouseId,
                          LocalDate returnDate, ReturnReason reason,
                          PurchaseReturnStatus status, List<PurchaseReturnLine> lines) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.returnNumber = returnNumber;
        this.purchaseOrderId = purchaseOrderId;
        this.goodsReceiptId = goodsReceiptId;
        this.vendorId = vendorId;
        this.warehouseId = warehouseId;
        this.returnDate = returnDate;
        this.reason = reason;
        this.status = status;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }


    public List<PurchaseReturnLine> getLines() {
        return Collections.unmodifiableList(lines);
    }


    public void post() {
        if (status != PurchaseReturnStatus.DRAFT) {
            throw new IllegalStateException("Can only post DRAFT purchase returns");
        }
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cannot post a purchase return with no lines");
        }
        this.status = PurchaseReturnStatus.POSTED;

        List<PurchaseReturnPostedEvent.LineDetail> ld = lines.stream()
                .map(l -> new PurchaseReturnPostedEvent.LineDetail(
                        l.getId().asUUID().toString(), l.getItemId(), l.getQuantity()))
                .toList();

        registerEvent(new PurchaseReturnPostedEvent(
                getId().asUUID(), tenantId.asUUID(), companyId.asUUID(),
                returnNumber.getValue(), purchaseOrderId, warehouseId, ld));
    }


    public void complete() {
        if (status != PurchaseReturnStatus.POSTED) {
            throw new IllegalStateException("Can only complete POSTED purchase returns");
        }
        this.status = PurchaseReturnStatus.COMPLETED;
    }


    public void cancel() {
        if (status != PurchaseReturnStatus.DRAFT) {
            throw new IllegalStateException("Can only cancel DRAFT purchase returns");
        }
        this.status = PurchaseReturnStatus.CANCELLED;
    }
}

