package tr.kontas.erp.purchase.platform.persistence.purchaseorder;

import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.purchase.domain.purchaseorder.*;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderMapper {

    public static PurchaseOrderJpaEntity toEntity(PurchaseOrder domain) {
        PurchaseOrderJpaEntity entity = new PurchaseOrderJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setOrderNumber(domain.getOrderNumber().getValue());
        entity.setRequestId(domain.getRequestId());
        entity.setVendorId(domain.getVendorId());
        entity.setVendorName(domain.getVendorName());
        entity.setOrderDate(domain.getOrderDate());
        entity.setExpectedDeliveryDate(domain.getExpectedDeliveryDate());
        entity.setCurrencyCode(domain.getCurrencyCode());
        entity.setPaymentTermCode(domain.getPaymentTermCode());

        Address addr = domain.getDeliveryAddress();
        if (addr != null) {
            entity.setAddressLine1(addr.getAddressLine1());
            entity.setAddressLine2(addr.getAddressLine2());
            entity.setCity(addr.getCity());
            entity.setStateOrProvince(addr.getStateOrProvince());
            entity.setPostalCode(addr.getPostalCode());
            entity.setCountryCode(addr.getCountryCode());
        }

        entity.setStatus(domain.getStatus().name());
        entity.setSubtotal(domain.getSubtotal());
        entity.setTaxTotal(domain.getTaxTotal());
        entity.setTotal(domain.getTotal());

        List<PurchaseOrderLineJpaEntity> lineEntities = new ArrayList<>();
        for (PurchaseOrderLine line : domain.getLines()) {
            PurchaseOrderLineJpaEntity le = new PurchaseOrderLineJpaEntity();
            le.setId(line.getId().asUUID());
            le.setOrder(entity);
            le.setRequestLineId(line.getRequestLineId());
            le.setItemId(line.getItemId());
            le.setItemDescription(line.getItemDescription());
            le.setUnitCode(line.getUnitCode());
            le.setOrderedQty(line.getOrderedQty());
            le.setReceivedQty(line.getReceivedQty());
            le.setUnitPrice(line.getUnitPrice());
            le.setTaxCode(line.getTaxCode());
            le.setTaxRate(line.getTaxRate());
            le.setLineTotal(line.getLineTotal());
            le.setTaxAmount(line.getTaxAmount());
            le.setExpenseAccountId(line.getExpenseAccountId());
            lineEntities.add(le);
        }
        entity.setLines(lineEntities);
        return entity;
    }

    public static PurchaseOrder toDomain(PurchaseOrderJpaEntity entity) {
        Address address = null;
        if (entity.getAddressLine1() != null) {
            address = new Address(
                    entity.getAddressLine1(), entity.getAddressLine2(),
                    entity.getCity(), entity.getStateOrProvince(),
                    entity.getPostalCode(), entity.getCountryCode()
            );
        }

        List<PurchaseOrderLine> lines = entity.getLines().stream()
                .map(le -> new PurchaseOrderLine(
                        PurchaseOrderLineId.of(le.getId()),
                        PurchaseOrderId.of(entity.getId()),
                        le.getRequestLineId(),
                        le.getItemId(),
                        le.getItemDescription(),
                        le.getUnitCode(),
                        le.getOrderedQty(),
                        le.getReceivedQty(),
                        le.getUnitPrice(),
                        le.getTaxCode(),
                        le.getTaxRate(),
                        le.getExpenseAccountId()
                ))
                .toList();

        return new PurchaseOrder(
                PurchaseOrderId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new PurchaseOrderNumber(entity.getOrderNumber()),
                entity.getRequestId(),
                entity.getVendorId(),
                entity.getVendorName(),
                entity.getOrderDate(),
                entity.getExpectedDeliveryDate(),
                entity.getCurrencyCode(),
                entity.getPaymentTermCode(),
                address,
                PurchaseOrderStatus.valueOf(entity.getStatus()),
                lines
        );
    }
}

