package tr.kontas.erp.purchase.platform.persistence.purchaserequest;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchaserequest.*;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestMapper {

    public static PurchaseRequestJpaEntity toEntity(PurchaseRequest domain) {
        PurchaseRequestJpaEntity entity = new PurchaseRequestJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setRequestNumber(domain.getRequestNumber().getValue());
        entity.setRequestedBy(domain.getRequestedBy());
        entity.setApprovedBy(domain.getApprovedBy());
        entity.setRequestDate(domain.getRequestDate());
        entity.setNeededBy(domain.getNeededBy());
        entity.setStatus(domain.getStatus().name());
        entity.setNotes(domain.getNotes());

        List<PurchaseRequestLineJpaEntity> lineEntities = new ArrayList<>();
        for (PurchaseRequestLine line : domain.getLines()) {
            PurchaseRequestLineJpaEntity le = new PurchaseRequestLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setRequest(entity);
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setQuantity(line.getQuantity());
            le.setPreferredVendorId(line.getPreferredVendorId());
            le.setNotes(line.getNotes());
            lineEntities.add(le);
        }
        entity.setLines(lineEntities);
        return entity;
    }

    public static PurchaseRequest toDomain(PurchaseRequestJpaEntity entity) {
        List<PurchaseRequestLine> lines = entity.getLines().stream()
                .map(le -> new PurchaseRequestLine(
                        PurchaseRequestLineId.of(le.getId()),
                        PurchaseRequestId.of(entity.getId()),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getQuantity(),
                        le.getPreferredVendorId(),
                        le.getNotes()
                ))
                .toList();

        return new PurchaseRequest(
                PurchaseRequestId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new PurchaseRequestNumber(entity.getRequestNumber()),
                entity.getRequestedBy(),
                entity.getApprovedBy(),
                entity.getRequestDate(),
                entity.getNeededBy(),
                PurchaseRequestStatus.valueOf(entity.getStatus()),
                entity.getNotes(),
                lines
        );
    }
}

