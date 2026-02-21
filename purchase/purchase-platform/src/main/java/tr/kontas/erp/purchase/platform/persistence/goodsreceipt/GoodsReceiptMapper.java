package tr.kontas.erp.purchase.platform.persistence.goodsreceipt;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.goodsreceipt.*;

import java.util.ArrayList;
import java.util.List;

public class GoodsReceiptMapper {

    public static GoodsReceiptJpaEntity toEntity(GoodsReceipt domain) {
        GoodsReceiptJpaEntity entity = new GoodsReceiptJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setReceiptNumber(domain.getReceiptNumber().getValue());
        entity.setPurchaseOrderId(domain.getPurchaseOrderId());
        entity.setVendorId(domain.getVendorId());
        entity.setWarehouseId(domain.getWarehouseId());
        entity.setReceiptDate(domain.getReceiptDate());
        entity.setStatus(domain.getStatus().name());
        entity.setVendorDeliveryNote(domain.getVendorDeliveryNote());

        List<GoodsReceiptLineJpaEntity> lineEntities = new ArrayList<>();
        for (GoodsReceiptLine line : domain.getLines()) {
            GoodsReceiptLineJpaEntity le = new GoodsReceiptLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setReceipt(entity);
            le.setPoLineId(line.getPoLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setQuantity(line.getQuantity());
            le.setBatchNote(line.getBatchNote());
            lineEntities.add(le);
        }
        entity.setLines(lineEntities);
        return entity;
    }

    public static GoodsReceipt toDomain(GoodsReceiptJpaEntity entity) {
        List<GoodsReceiptLine> lines = entity.getLines().stream()
                .map(le -> new GoodsReceiptLine(
                        GoodsReceiptLineId.of(le.getId()),
                        GoodsReceiptId.of(entity.getId()),
                        le.getPoLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity(),
                        le.getBatchNote()
                ))
                .toList();

        return new GoodsReceipt(
                GoodsReceiptId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new GoodsReceiptNumber(entity.getReceiptNumber()),
                entity.getPurchaseOrderId(),
                entity.getVendorId(),
                entity.getWarehouseId(),
                entity.getReceiptDate(),
                GoodsReceiptStatus.valueOf(entity.getStatus()),
                entity.getVendorDeliveryNote(),
                lines
        );
    }
}

