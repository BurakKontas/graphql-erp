package tr.kontas.erp.shipment.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.shipment.application.port.ShipmentNumberGeneratorPort;
import tr.kontas.erp.shipment.application.shipment.*;
import tr.kontas.erp.shipment.domain.deliveryorder.*;
import tr.kontas.erp.shipment.domain.shipment.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService implements
        CreateShipmentUseCase,
        GetShipmentByIdUseCase,
        GetShipmentsByCompanyUseCase,
        GetShipmentsByIdsUseCase,
        SetTrackingInfoUseCase,
        DispatchShipmentUseCase,
        DeliverShipmentUseCase {

    private final ShipmentRepository shipmentRepository;
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final ShipmentNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public ShipmentId execute(CreateShipmentCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        ShipmentNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());

        Address address = null;
        if (command.addressLine1() != null) {
            address = new Address(
                    command.addressLine1(), command.addressLine2(),
                    command.city(), command.stateOrProvince(),
                    command.postalCode(), command.countryCode()
            );
        }

        List<ShipmentLine> lines = command.lines().stream()
                .map(lc -> new ShipmentLine(
                        ShipmentLineId.newId(),
                        lc.deliveryOrderLineId(),
                        lc.itemId(),
                        lc.itemDescription(),
                        lc.unitCode(),
                        lc.quantity()
                ))
                .toList();

        ShipmentId id = ShipmentId.newId();
        Shipment shipment = new Shipment(
                id, tenantId, companyId, number,
                command.deliveryOrderId(), command.salesOrderId(), command.warehouseId(),
                address, lines
        );

        saveAndPublish(shipment);
        return id;
    }

    @Override
    public Shipment execute(ShipmentId id) {
        TenantId tenantId = TenantContext.get();
        return shipmentRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found: " + id));
    }

    @Override
    public List<Shipment> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return shipmentRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<Shipment> execute(List<ShipmentId> ids) {
        return shipmentRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void execute(String shipmentId, String carrierName, String trackingNumber) {
        Shipment shipment = loadShipment(shipmentId);
        shipment.setTrackingInfo(carrierName, trackingNumber);
        saveAndPublish(shipment);
    }

    @Override
    @Transactional
    public void dispatch(String shipmentId) {
        Shipment shipment = loadShipment(shipmentId);
        shipment.dispatch();
        saveAndPublish(shipment);
    }

    @Override
    @Transactional
    public void deliver(String shipmentId) {
        TenantId tenantId = TenantContext.get();
        Shipment shipment = loadShipment(shipmentId);
        shipment.deliver();

        // Update DeliveryOrder shippedQty for each line
        if (shipment.getDeliveryOrderId() != null) {
            DeliveryOrder deliveryOrder = deliveryOrderRepository
                    .findById(DeliveryOrderId.of(shipment.getDeliveryOrderId()), tenantId)
                    .orElse(null);

            if (deliveryOrder != null) {
                for (ShipmentLine sl : shipment.getLines()) {
                    if (sl.getDeliveryOrderLineId() != null) {
                        deliveryOrder.recordShipment(
                                DeliveryOrderLineId.of(sl.getDeliveryOrderLineId()),
                                sl.getQuantity()
                        );
                    }
                }
                deliveryOrderRepository.save(deliveryOrder);
                eventPublisher.publishAll(deliveryOrder.getDomainEvents());
                deliveryOrder.clearDomainEvents();
            }
        }

        saveAndPublish(shipment);
    }

    private Shipment loadShipment(String shipmentId) {
        TenantId tenantId = TenantContext.get();
        return shipmentRepository.findById(ShipmentId.of(shipmentId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found: " + shipmentId));
    }

    private void saveAndPublish(Shipment shipment) {
        shipmentRepository.save(shipment);
        eventPublisher.publishAll(shipment.getDomainEvents());
        shipment.clearDomainEvents();
    }
}

