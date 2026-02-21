package tr.kontas.erp.shipment.platform.persistence.deliveryorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.deliveryorder.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeliveryOrderRepositoryImpl implements DeliveryOrderRepository {

    private final JpaDeliveryOrderRepository jpa;

    @Override
    public void save(DeliveryOrder order) {
        jpa.save(DeliveryOrderMapper.toEntity(order));
    }

    @Override
    public Optional<DeliveryOrder> findById(DeliveryOrderId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID())
                .map(DeliveryOrderMapper::toDomain);
    }

    @Override
    public Optional<DeliveryOrder> findByNumber(DeliveryOrderNumber number, TenantId tenantId) {
        return jpa.findByNumberAndTenantId(number.getValue(), tenantId.asUUID())
                .map(DeliveryOrderMapper::toDomain);
    }

    @Override
    public boolean existsByNumber(DeliveryOrderNumber number, TenantId tenantId) {
        return jpa.existsByNumberAndTenantId(number.getValue(), tenantId.asUUID());
    }

    @Override
    public List<DeliveryOrder> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(DeliveryOrderMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOrder> findBySalesOrderId(String salesOrderId, TenantId tenantId) {
        return jpa.findBySalesOrderIdAndTenantId(salesOrderId, tenantId.asUUID())
                .stream().map(DeliveryOrderMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOrder> findByIds(List<DeliveryOrderId> ids) {
        List<UUID> uuids = ids.stream().map(DeliveryOrderId::asUUID).collect(Collectors.toList());
        return jpa.findByIdIn(uuids).stream().map(DeliveryOrderMapper::toDomain).collect(Collectors.toList());
    }
}

