package tr.kontas.erp.purchase.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.purchase.application.port.VendorInvoiceNumberGeneratorPort;
import tr.kontas.erp.purchase.application.vendorinvoice.*;
import tr.kontas.erp.purchase.domain.vendorinvoice.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorInvoiceService implements
        CreateVendorInvoiceUseCase,
        GetVendorInvoiceByIdUseCase,
        GetVendorInvoicesByCompanyUseCase,
        GetVendorInvoicesByIdsUseCase,
        PostVendorInvoiceUseCase,
        CancelVendorInvoiceUseCase {

    private final VendorInvoiceRepository vendorInvoiceRepository;
    private final VendorInvoiceNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public VendorInvoiceId execute(CreateVendorInvoiceCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        LocalDate invoiceDate = command.invoiceDate() != null ? command.invoiceDate() : LocalDate.now();

        VendorInvoiceNumber number = numberGenerator.generate(tenantId, companyId, invoiceDate.getYear());

        List<VendorInvoiceLine> lines = command.lines() != null
                ? command.lines().stream()
                .map(lc -> new VendorInvoiceLine(
                        VendorInvoiceLineId.newId(), null,
                        lc.poLineId(), lc.itemId(), lc.itemDescription(),
                        lc.unitCode(), lc.quantity(), lc.unitPrice(),
                        lc.taxCode(), lc.taxRate(), lc.accountId()
                ))
                .toList()
                : List.of();

        VendorInvoiceId id = VendorInvoiceId.newId();
        VendorInvoice invoice = new VendorInvoice(
                id, tenantId, companyId, number,
                command.vendorInvoiceRef(), command.purchaseOrderId(),
                command.vendorId(), command.vendorName(),
                command.accountingPeriodId(), invoiceDate, command.dueDate(),
                command.currencyCode(), command.exchangeRate() != null ? command.exchangeRate() : BigDecimal.ONE,
                VendorInvoiceStatus.DRAFT, BigDecimal.ZERO, lines
        );

        vendorInvoiceRepository.save(invoice);
        return id;
    }

    @Override
    public VendorInvoice execute(VendorInvoiceId id) {
        TenantId tenantId = TenantContext.get();
        return vendorInvoiceRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("VendorInvoice not found: " + id));
    }

    @Override
    public List<VendorInvoice> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return vendorInvoiceRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<VendorInvoice> execute(List<VendorInvoiceId> ids) {
        return vendorInvoiceRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void post(String invoiceId) {
        TenantId tenantId = TenantContext.get();
        VendorInvoice invoice = vendorInvoiceRepository.findById(VendorInvoiceId.of(invoiceId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("VendorInvoice not found: " + invoiceId));
        invoice.post();
        saveAndPublish(invoice);
    }

    @Override
    @Transactional
    public void cancel(String invoiceId, String reason) {
        TenantId tenantId = TenantContext.get();
        VendorInvoice invoice = vendorInvoiceRepository.findById(VendorInvoiceId.of(invoiceId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("VendorInvoice not found: " + invoiceId));
        invoice.cancel(reason);
        saveAndPublish(invoice);
    }

    private void saveAndPublish(VendorInvoice invoice) {
        vendorInvoiceRepository.save(invoice);
        eventPublisher.publishAll(invoice.getDomainEvents());
        invoice.clearDomainEvents();
    }
}

