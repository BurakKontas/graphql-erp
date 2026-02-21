package tr.kontas.erp.sales.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.reference.currency.Currency;
import tr.kontas.erp.core.domain.reference.payment.PaymentTerm;
import tr.kontas.erp.core.domain.reference.tax.Tax;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.core.domain.shared.Quantity;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.sales.application.port.BusinessPartnerValidationPort;
import tr.kontas.erp.sales.application.port.ItemValidationPort;
import tr.kontas.erp.sales.application.port.SalesOrderNumberGeneratorPort;
import tr.kontas.erp.sales.application.port.TaxResolutionPort;
import tr.kontas.erp.sales.application.salesorder.*;
import tr.kontas.erp.sales.domain.salesorder.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderService implements
        CreateSalesOrderUseCase,
        GetSalesOrderByIdUseCase,
        GetSalesOrdersByCompanyUseCase,
        GetSalesOrdersByIdsUseCase,
        AddSalesOrderLineUseCase,
        UpdateSalesOrderLineUseCase,
        RemoveSalesOrderLineUseCase,
        UpdateSalesOrderHeaderUseCase,
        SendSalesOrderUseCase,
        ConfirmSalesOrderUseCase,
        CancelSalesOrderUseCase,
        AcceptSalesOrderUseCase {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderNumberGeneratorPort orderNumberGenerator;
    private final BusinessPartnerValidationPort businessPartnerValidation;
    private final TaxResolutionPort taxResolution;
    private final ItemValidationPort itemValidation;
    private final DomainEventPublisher eventPublisher;

    // ─── Create ───

    @Override
    @Transactional
    public SalesOrderId execute(CreateSalesOrderCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        LocalDate orderDate = command.orderDate() != null ? command.orderDate() : LocalDate.now();

        BusinessPartnerId customerId = null;
        if (command.customerId() != null) {
            customerId = BusinessPartnerId.of(command.customerId());
            businessPartnerValidation.validateCustomerExists(tenantId, companyId, customerId);
        }

        Currency currency = command.currencyCode() != null
                ? taxResolution.resolveCurrency(command.currencyCode())
                : null;

        PaymentTerm paymentTerm = command.paymentTermCode() != null
                ? taxResolution.resolvePaymentTerm(tenantId, companyId, command.paymentTermCode())
                : null;

        SalesOrderNumber orderNumber = orderNumberGenerator.generate(tenantId, companyId, orderDate.getYear());

        SalesOrderId id = SalesOrderId.newId();
        SalesOrder order = new SalesOrder(
                id,
                tenantId,
                companyId,
                orderNumber,
                customerId,
                currency,
                paymentTerm,
                orderDate
        );

        saveAndPublish(order);
        return id;
    }

    // ─── Read ───

    @Override
    public SalesOrder execute(SalesOrderId id) {
        TenantId tenantId = TenantContext.get();
        return salesOrderRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("SalesOrder not found: " + id));
    }

    @Override
    public List<SalesOrder> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return salesOrderRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<SalesOrder> execute(List<SalesOrderId> ids) {
        return salesOrderRepository.findByIds(ids);
    }

    // ─── Line operations ───

    @Override
    @Transactional
    public SalesOrderLineId execute(AddSalesOrderLineCommand command) {
        TenantId tenantId = TenantContext.get();
        SalesOrder order = loadOrder(command.orderId());

        if (command.itemId() != null) {
            itemValidation.validateItemExists(tenantId, order.getCompanyId(), command.itemId());
        }

        Tax tax = command.taxCode() != null
                ? taxResolution.resolveTax(tenantId, order.getCompanyId(), command.taxCode())
                : null;

        SalesOrderLineId lineId = SalesOrderLineId.newId();
        order.addLine(
                lineId,
                command.itemId(),
                command.itemDescription(),
                command.unitCode(),
                Quantity.of(command.quantity()),
                command.unitPrice(),
                tax
        );

        saveAndPublish(order);
        return lineId;
    }

    @Override
    @Transactional
    public void execute(UpdateSalesOrderLineCommand command) {
        SalesOrder order = loadOrder(command.orderId());

        order.updateLine(
                SalesOrderLineId.of(command.lineId()),
                Quantity.of(command.quantity()),
                command.unitPrice()
        );

        saveAndPublish(order);
    }

    @Override
    @Transactional
    public void removeLine(String orderId, String lineId) {
        SalesOrder order = loadOrder(orderId);
        order.removeLine(SalesOrderLineId.of(lineId));
        saveAndPublish(order);
    }

    // ─── Header update ───

    @Override
    @Transactional
    public void execute(UpdateSalesOrderHeaderCommand command) {
        TenantId tenantId = TenantContext.get();
        SalesOrder order = loadOrder(command.orderId());

        PaymentTerm paymentTerm = command.paymentTermCode() != null
                ? taxResolution.resolvePaymentTerm(tenantId, order.getCompanyId(), command.paymentTermCode())
                : null;

        Address shippingAddress = null;
        if (command.shippingAddress() != null) {
            var sa = command.shippingAddress();
            shippingAddress = new Address(
                    sa.addressLine1(),
                    sa.addressLine2(),
                    sa.city(),
                    sa.stateOrProvince(),
                    sa.postalCode(),
                    sa.countryCode()
            );
        }

        order.updateHeader(
                command.orderDate(),
                command.expiryDate(),
                paymentTerm,
                shippingAddress
        );

        saveAndPublish(order);
    }

    // ─── State transitions ───

    @Override
    @Transactional
    public void send(String orderId) {
        SalesOrder order = loadOrder(orderId);
        order.send();
        saveAndPublish(order);
    }

    @Override
    @Transactional
    public void confirm(String orderId) {
        SalesOrder order = loadOrder(orderId);
        order.confirm();
        saveAndPublish(order);
    }

    @Override
    @Transactional
    public void cancel(String orderId, String reason) {
        SalesOrder order = loadOrder(orderId);
        order.cancel(new CancellationReason(reason));
        saveAndPublish(order);
    }

    @Override
    @Transactional
    public void accept(String orderId) {
        SalesOrder order = loadOrder(orderId);
        order.accept();
        saveAndPublish(order);
    }

    // ─── Helpers ───

    private SalesOrder loadOrder(String orderId) {
        TenantId tenantId = TenantContext.get();
        return salesOrderRepository.findById(SalesOrderId.of(orderId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("SalesOrder not found: " + orderId));
    }

    private void saveAndPublish(SalesOrder order) {
        salesOrderRepository.save(order);
        eventPublisher.publishAll(order.getDomainEvents());
        order.clearDomainEvents();
    }
}
