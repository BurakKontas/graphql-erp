package tr.kontas.erp.shipment.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.shipment.application.deliveryorder.*;
import tr.kontas.erp.shipment.application.port.DeliveryOrderNumberGeneratorPort;
import tr.kontas.erp.shipment.domain.deliveryorder.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryOrderService implements
        CreateDeliveryOrderUseCase,
        GetDeliveryOrderByIdUseCase,
        GetDeliveryOrdersByCompanyUseCase,
        GetDeliveryOrdersByIdsUseCase,
        CancelDeliveryOrderUseCase {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public DeliveryOrderId execute(CreateDeliveryOrderCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();

        DeliveryOrderNumber number = numberGenerator.generate(tenantId, companyId, LocalDate.now().getYear());

        Address address = null;
        if (command.addressLine1() != null) {
            address = new Address(
                    command.addressLine1(), command.addressLine2(),
                    command.city(), command.stateOrProvince(),
                    command.postalCode(), command.countryCode()
            );
        }

        List<DeliveryOrderLine> lines = command.lines().stream()
                .map(lc -> new DeliveryOrderLine(
                        DeliveryOrderLineId.newId(),
                        lc.salesOrderLineId(),
                        lc.itemId(),
                        lc.itemDescription(),
                        lc.unitCode(),
                        lc.orderedQty()
                ))
                .toList();

        DeliveryOrderId id = DeliveryOrderId.newId();
        DeliveryOrder order = new DeliveryOrder(
                id, tenantId, companyId, number,
                command.salesOrderId(), command.salesOrderNumber(), command.customerId(),
                address, lines
        );

        saveAndPublish(order);
        return id;
    }

    @Override
    public DeliveryOrder execute(DeliveryOrderId id) {
        TenantId tenantId = TenantContext.get();
        return deliveryOrderRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryOrder not found: " + id));
    }

    @Override
    public List<DeliveryOrder> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return deliveryOrderRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<DeliveryOrder> execute(List<DeliveryOrderId> ids) {
        return deliveryOrderRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void cancel(String deliveryOrderId, String reason) {
        TenantId tenantId = TenantContext.get();
        DeliveryOrder order = deliveryOrderRepository.findById(DeliveryOrderId.of(deliveryOrderId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryOrder not found: " + deliveryOrderId));
        order.cancel(new CancellationReason(reason));
        saveAndPublish(order);
    }

    private void saveAndPublish(DeliveryOrder order) {
        deliveryOrderRepository.save(order);
        eventPublisher.publishAll(order.getDomainEvents());
        order.clearDomainEvents();
    }
}

