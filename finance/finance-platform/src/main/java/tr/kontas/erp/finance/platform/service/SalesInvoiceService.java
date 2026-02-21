package tr.kontas.erp.finance.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.finance.application.port.InvoiceNumberGeneratorPort;
import tr.kontas.erp.finance.application.salesinvoice.*;
import tr.kontas.erp.finance.domain.salesinvoice.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesInvoiceService implements
        CreateSalesInvoiceUseCase, GetSalesInvoiceByIdUseCase,
        GetSalesInvoicesByCompanyUseCase, PostSalesInvoiceUseCase,
        CancelSalesInvoiceUseCase {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final InvoiceNumberGeneratorPort invoiceNumberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public SalesInvoiceId execute(CreateSalesInvoiceCommand cmd) {
        TenantId tenantId = TenantContext.get();
        LocalDate invoiceDate = cmd.invoiceDate() != null ? cmd.invoiceDate() : LocalDate.now();
        String number = invoiceNumberGenerator.generate(tenantId, cmd.companyId(), invoiceDate.getYear());

        SalesInvoiceId id = SalesInvoiceId.newId();
        List<SalesInvoiceLine> lines = cmd.lines().stream()
                .map(l -> new SalesInvoiceLine(
                        SalesInvoiceLineId.newId(), id, l.salesOrderLineId(),
                        l.itemId(), l.itemDescription(), l.unitCode(),
                        l.quantity(), l.unitPrice(), l.taxCode(), l.taxRate(),
                        l.revenueAccountId()
                )).toList();

        SalesInvoice invoice = new SalesInvoice(id, tenantId, cmd.companyId(),
                new InvoiceNumber(number),
                cmd.invoiceType() != null ? InvoiceType.valueOf(cmd.invoiceType()) : InvoiceType.STANDARD,
                cmd.salesOrderId(), cmd.salesOrderNumber(), cmd.customerId(), cmd.customerName(),
                invoiceDate, cmd.dueDate(), cmd.currencyCode(), cmd.exchangeRate(),
                InvoiceStatus.DRAFT, null, lines);

        salesInvoiceRepository.save(invoice);
        return id;
    }

    @Override
    public SalesInvoice execute(SalesInvoiceId id) {
        TenantId tenantId = TenantContext.get();
        return salesInvoiceRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("SalesInvoice not found: " + id));
    }

    @Override
    public List<SalesInvoice> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return salesInvoiceRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    @Transactional
    public void execute(String invoiceId) {
        SalesInvoice invoice = loadInvoice(invoiceId);
        invoice.post();
        saveAndPublish(invoice);
    }

    @Override
    @Transactional
    public void execute(String invoiceId, String reason) {
        SalesInvoice invoice = loadInvoice(invoiceId);
        invoice.cancel(reason);
        salesInvoiceRepository.save(invoice);
    }

    private SalesInvoice loadInvoice(String invoiceId) {
        TenantId tenantId = TenantContext.get();
        return salesInvoiceRepository.findById(SalesInvoiceId.of(invoiceId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("SalesInvoice not found: " + invoiceId));
    }

    private void saveAndPublish(SalesInvoice invoice) {
        salesInvoiceRepository.save(invoice);
        eventPublisher.publishAll(invoice.getDomainEvents());
        invoice.clearDomainEvents();
    }
}
