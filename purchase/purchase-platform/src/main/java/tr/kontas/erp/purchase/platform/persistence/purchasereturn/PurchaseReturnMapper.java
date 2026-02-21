package tr.kontas.erp.purchase.platform.persistence.purchasereturn;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchasereturn.*;

import java.util.ArrayList;
import java.util.List;

public class PurchaseReturnMapper {

    public static PurchaseReturnJpaEntity toEntity(PurchaseReturn domain) {
        PurchaseReturnJpaEntity entity = new PurchaseReturnJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setReturnNumber(domain.getReturnNumber().getValue());
        entity.setPurchaseOrderId(domain.getPurchaseOrderId());
        entity.setGoodsReceiptId(domain.getGoodsReceiptId());
        entity.setVendorId(domain.getVendorId());
        entity.setWarehouseId(domain.getWarehouseId());
        entity.setReturnDate(domain.getReturnDate());
        entity.setReason(domain.getReason().name());
        entity.setStatus(domain.getStatus().name());

        List<PurchaseReturnLineJpaEntity> lineEntities = new ArrayList<>();
        for (PurchaseReturnLine line : domain.getLines()) {
            PurchaseReturnLineJpaEntity le = new PurchaseReturnLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setPurchaseReturn(entity);
            le.setReceiptLineId(line.getReceiptLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setQuantity(line.getQuantity());
            le.setLineReason(line.getLineReason());
            lineEntities.add(le);
        }
        entity.setLines(lineEntities);
        return entity;
    }

    public static PurchaseReturn toDomain(PurchaseReturnJpaEntity entity) {
        List<PurchaseReturnLine> lines = entity.getLines().stream()
                .map(le -> new PurchaseReturnLine(
                        PurchaseReturnLineId.of(le.getId()),
                        PurchaseReturnId.of(entity.getId()),
                        le.getReceiptLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity(),
                        le.getLineReason()
                ))
                .toList();

        return new PurchaseReturn(
                PurchaseReturnId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new PurchaseReturnNumber(entity.getReturnNumber()),
                entity.getPurchaseOrderId(),
                entity.getGoodsReceiptId(),
                entity.getVendorId(),
                entity.getWarehouseId(),
                entity.getReturnDate(),
                ReturnReason.valueOf(entity.getReason()),
                PurchaseReturnStatus.valueOf(entity.getStatus()),
                lines
        );
    }
}

