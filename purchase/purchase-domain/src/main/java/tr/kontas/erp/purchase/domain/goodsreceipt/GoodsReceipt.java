package tr.kontas.erp.purchase.domain.goodsreceipt;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.event.GoodsReceiptPostedEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Getter
public class GoodsReceipt extends AggregateRoot<GoodsReceiptId> {
    private final TenantId tenantId;
    private final CompanyId companyId;
    private final GoodsReceiptNumber receiptNumber;
    private final String purchaseOrderId;
    private final String vendorId;
    private final String warehouseId;
    private final LocalDate receiptDate;
    private GoodsReceiptStatus status;
    private final String vendorDeliveryNote;
    private final List<GoodsReceiptLine> lines;
    public GoodsReceipt(GoodsReceiptId id, TenantId tenantId, CompanyId companyId, GoodsReceiptNumber receiptNumber, String purchaseOrderId, String vendorId, String warehouseId, LocalDate receiptDate, GoodsReceiptStatus status, String vendorDeliveryNote, List<GoodsReceiptLine> lines) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.receiptNumber = receiptNumber;
        this.purchaseOrderId = purchaseOrderId;
        this.vendorId = vendorId;
        this.warehouseId = warehouseId;
        this.receiptDate = receiptDate;
        this.status = status;
        this.vendorDeliveryNote = vendorDeliveryNote;
        this.lines = new ArrayList<>(lines != null ? lines : List.of());
    }

    public List<GoodsReceiptLine> getLines() { return Collections.unmodifiableList(lines); }

    public void post() {
        if (status != GoodsReceiptStatus.DRAFT) throw new IllegalStateException("Can only post DRAFT goods receipts");
        if (lines.isEmpty()) throw new IllegalStateException("Cannot post a goods receipt with no lines");
        this.status = GoodsReceiptStatus.POSTED;
        List<GoodsReceiptPostedEvent.LineDetail> ld = lines.stream().map(l -> new GoodsReceiptPostedEvent.LineDetail(l.getId().asUUID().toString(), l.getPoLineId(), l.getItemId(), l.getQuantity())).toList();
        registerEvent(new GoodsReceiptPostedEvent(getId().asUUID(), tenantId.asUUID(), companyId.asUUID(), receiptNumber.getValue(), purchaseOrderId, warehouseId, ld));
    }

    public void cancel() {
        if (status != GoodsReceiptStatus.DRAFT) throw new IllegalStateException("Can only cancel DRAFT goods receipts");
        this.status = GoodsReceiptStatus.CANCELLED;
    }
}
