package tr.kontas.erp.shipment.platform.persistence.shipmentreturn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.shipment.domain.shipmentreturn.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShipmentReturnRepositoryImpl implements ShipmentReturnRepository {

    private final JpaShipmentReturnRepository jpa;

    @Override
    public void save(ShipmentReturn sr) {
        jpa.save(ShipmentReturnMapper.toEntity(sr));
    }

    @Override
    public Optional<ShipmentReturn> findById(ShipmentReturnId id, TenantId tenantId) {
        return jpa.findByIdAndTenantId(id.asUUID(), tenantId.asUUID()).map(ShipmentReturnMapper::toDomain);
    }

    @Override
    public Optional<ShipmentReturn> findByNumber(ShipmentReturnNumber number, TenantId tenantId) {
        return jpa.findByNumberAndTenantId(number.getValue(), tenantId.asUUID()).map(ShipmentReturnMapper::toDomain);
    }

    @Override
    public boolean existsByNumber(ShipmentReturnNumber number, TenantId tenantId) {
        return jpa.existsByNumberAndTenantId(number.getValue(), tenantId.asUUID());
    }

    @Override
    public List<ShipmentReturn> findByShipmentId(String shipmentId, TenantId tenantId) {
        return jpa.findByShipmentIdAndTenantId(shipmentId, tenantId.asUUID())
                .stream().map(ShipmentReturnMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ShipmentReturn> findByCompanyId(TenantId tenantId, CompanyId companyId) {
        return jpa.findByTenantIdAndCompanyId(tenantId.asUUID(), companyId.asUUID())
                .stream().map(ShipmentReturnMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ShipmentReturn> findByIds(List<ShipmentReturnId> ids) {
        List<UUID> uuids = ids.stream().map(ShipmentReturnId::asUUID).collect(Collectors.toList());
        return jpa.findByIdIn(uuids).stream().map(ShipmentReturnMapper::toDomain).collect(Collectors.toList());
    }
}

