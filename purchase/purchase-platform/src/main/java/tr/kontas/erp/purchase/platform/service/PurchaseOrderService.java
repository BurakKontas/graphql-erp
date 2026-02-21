package tr.kontas.erp.purchase.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.purchase.application.port.PurchaseOrderNumberGeneratorPort;
import tr.kontas.erp.purchase.application.purchaseorder.*;
import tr.kontas.erp.purchase.domain.purchaseorder.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements
        CreatePurchaseOrderUseCase,
        GetPurchaseOrderByIdUseCase,
        GetPurchaseOrdersByCompanyUseCase,
        GetPurchaseOrdersByIdsUseCase,
        SendPurchaseOrderUseCase,
        ConfirmPurchaseOrderUseCase,
        CancelPurchaseOrderUseCase {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public PurchaseOrderId execute(CreatePurchaseOrderCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        LocalDate orderDate = command.orderDate() != null ? command.orderDate() : LocalDate.now();

        PurchaseOrderNumber number = numberGenerator.generate(tenantId, companyId, orderDate.getYear());

        Address address = null;
        if (command.addressLine1() != null) {
            address = new Address(
                    command.addressLine1(), command.addressLine2(),
                    command.city(), command.stateOrProvince(),
                    command.postalCode(), command.countryCode()
            );
        }

        List<PurchaseOrderLine> lines = command.lines() != null
                ? command.lines().stream()
                .map(lc -> new PurchaseOrderLine(
                        PurchaseOrderLineId.newId(), null,
                        lc.requestLineId(), lc.itemId(), lc.itemDescription(),
                        lc.unitCode(), lc.orderedQty(), BigDecimal.ZERO,
                        lc.unitPrice(), lc.taxCode(), lc.taxRate(),
                        lc.expenseAccountId()
                ))
                .toList()
                : List.of();

        PurchaseOrderId id = PurchaseOrderId.newId();
        PurchaseOrder order = new PurchaseOrder(
                id, tenantId, companyId, number,
                command.requestId(), command.vendorId(), command.vendorName(),
                orderDate, command.expectedDeliveryDate(),
                command.currencyCode(), command.paymentTermCode(),
                address, PurchaseOrderStatus.DRAFT, lines
        );

        saveAndPublish(order);
        return id;
    }

    @Override
    public PurchaseOrder execute(PurchaseOrderId id) {
        TenantId tenantId = TenantContext.get();
        return purchaseOrderRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseOrder not found: " + id));
    }

    @Override
    public List<PurchaseOrder> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return purchaseOrderRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<PurchaseOrder> execute(List<PurchaseOrderId> ids) {
        return purchaseOrderRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void send(String orderId) {
        PurchaseOrder order = loadOrder(orderId);
        order.send();
        saveAndPublish(order);
    }

    @Override
    @Transactional
    public void confirm(String orderId) {
        PurchaseOrder order = loadOrder(orderId);
        order.confirm();
        saveAndPublish(order);
    }

    @Override
    @Transactional
    public void cancel(String orderId, String reason) {
        PurchaseOrder order = loadOrder(orderId);
        order.cancel(reason);
        saveAndPublish(order);
    }

    private PurchaseOrder loadOrder(String orderId) {
        TenantId tenantId = TenantContext.get();
        return purchaseOrderRepository.findById(PurchaseOrderId.of(orderId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseOrder not found: " + orderId));
    }

    private void saveAndPublish(PurchaseOrder order) {
        purchaseOrderRepository.save(order);
        eventPublisher.publishAll(order.getDomainEvents());
        order.clearDomainEvents();
    }
}

