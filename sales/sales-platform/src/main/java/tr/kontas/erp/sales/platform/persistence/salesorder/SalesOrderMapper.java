package tr.kontas.erp.sales.platform.persistence.salesorder;

import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.shared.Quantity;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.sales.domain.salesorder.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalesOrderMapper {

    public static SalesOrderJpaEntity toEntity(SalesOrder domain) {
        SalesOrderJpaEntity entity = new SalesOrderJpaEntity();
        entity.setId(domain.getId().asUUID());
        entity.setTenantId(domain.getTenantId().asUUID());
        entity.setCompanyId(domain.getCompanyId().asUUID());
        entity.setOrderNumber(domain.getOrderNumber().getValue());
        entity.setOrderDate(domain.getOrderDate());
        entity.setExpiryDate(domain.getExpiryDate());
        entity.setCustomerId(domain.getCustomerId() != null ? domain.getCustomerId().asUUID() : null);
        entity.setCurrencyCode(domain.getCurrency() != null ? domain.getCurrency().getId().getValue() : null);
        entity.setPaymentTermCode(domain.getPaymentTerm() != null ? domain.getPaymentTerm().getId().getValue() : null);

        ShippingAddress sa = domain.getShippingAddress();
        if (sa != null) {
            entity.setShippingAddressLine1(sa.getAddressLine1());
            entity.setShippingAddressLine2(sa.getAddressLine2());
            entity.setShippingCity(sa.getCity());
            entity.setShippingStateOrProvince(sa.getStateOrProvince());
            entity.setShippingPostalCode(sa.getPostalCode());
            entity.setShippingCountryCode(sa.getCountryCode());
        }

        entity.setStatus(domain.getStatus().name());
        entity.setFulfillmentStatus(domain.getFulfillmentStatus().name());
        entity.setInvoicedAmount(domain.getInvoicedAmount());
        entity.setSubtotal(domain.getSubtotal());
        entity.setTaxTotal(domain.getTaxTotal());
        entity.setTotal(domain.getTotal());

        List<SalesOrderLineJpaEntity> lineEntities = new ArrayList<>();
        for (SalesOrderLine line : domain.getLines()) {
            SalesOrderLineJpaEntity lineEntity = toLineEntity(line, entity);
            lineEntities.add(lineEntity);
        }
        entity.setLines(lineEntities);

        return entity;
    }

    private static SalesOrderLineJpaEntity toLineEntity(SalesOrderLine line, SalesOrderJpaEntity orderEntity) {
        SalesOrderLineJpaEntity entity = new SalesOrderLineJpaEntity();
        entity.setId(line.getId().asUUID());
        entity.setOrder(orderEntity);
        entity.setSequence(line.getSequence());
        entity.setItemId(line.getItemId());
        entity.setItemDescription(line.getItemDescription());
        entity.setUnitCode(line.getUnitCode());
        entity.setQuantity(line.getQuantity().getValue());
        entity.setUnitPrice(line.getUnitPrice());
        entity.setTaxCode(line.getTax() != null ? line.getTax().getId().getValue() : null);
        entity.setTaxRate(line.getTax() != null ? line.getTax().getRate().getValue() : BigDecimal.ZERO);
        entity.setLineTotal(line.getLineTotal());
        entity.setTaxAmount(line.getTaxAmount());
        entity.setLineTotalWithTax(line.getLineTotalWithTax());
        return entity;
    }

    public static SalesOrder toDomain(
            SalesOrderJpaEntity entity,
            Currency currency,
            PaymentTerm paymentTerm,
            Map<String, Tax> taxMap
    ) {
        List<SalesOrderLine> lines = entity.getLines().stream()
                .map(lineEntity -> toLineDomain(lineEntity, entity.getId(), taxMap))
                .toList();

        ShippingAddress shippingAddress = null;
        if (entity.getShippingAddressLine1() != null) {
            shippingAddress = new ShippingAddress(
                    entity.getShippingAddressLine1(),
                    entity.getShippingAddressLine2(),
                    entity.getShippingCity(),
                    entity.getShippingStateOrProvince(),
                    entity.getShippingPostalCode(),
                    entity.getShippingCountryCode()
            );
        }

        return new SalesOrder(
                SalesOrderId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                CompanyId.of(entity.getCompanyId()),
                new SalesOrderNumber(entity.getOrderNumber()),
                entity.getCustomerId() != null ? BusinessPartnerId.of(entity.getCustomerId()) : null,
                currency,
                paymentTerm,
                entity.getOrderDate(),
                entity.getExpiryDate(),
                shippingAddress,
                SalesOrderStatus.valueOf(entity.getStatus()),
                FulfillmentStatus.valueOf(entity.getFulfillmentStatus()),
                entity.getInvoicedAmount(),
                lines
        );
    }

    private static SalesOrderLine toLineDomain(
            SalesOrderLineJpaEntity lineEntity,
            java.util.UUID orderId,
            Map<String, Tax> taxMap
    ) {
        Tax tax = lineEntity.getTaxCode() != null ? taxMap.get(lineEntity.getTaxCode()) : null;

        return new SalesOrderLine(
                SalesOrderLineId.of(lineEntity.getId()),
                SalesOrderId.of(orderId),
                lineEntity.getSequence(),
                lineEntity.getItemId(),
                lineEntity.getItemDescription(),
                lineEntity.getUnitCode(),
                Quantity.of(lineEntity.getQuantity()),
                lineEntity.getUnitPrice(),
                tax
        );
    }
}
