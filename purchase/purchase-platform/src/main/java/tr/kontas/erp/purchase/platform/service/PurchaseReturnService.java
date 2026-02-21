package tr.kontas.erp.purchase.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.purchase.application.port.PurchaseReturnNumberGeneratorPort;
import tr.kontas.erp.purchase.application.purchasereturn.*;
import tr.kontas.erp.purchase.domain.purchasereturn.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseReturnService implements
        CreatePurchaseReturnUseCase,
        GetPurchaseReturnByIdUseCase,
        GetPurchaseReturnsByCompanyUseCase,
        GetPurchaseReturnsByIdsUseCase,
        PostPurchaseReturnUseCase,
        CompletePurchaseReturnUseCase {

    private final PurchaseReturnRepository purchaseReturnRepository;
    private final PurchaseReturnNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public PurchaseReturnId execute(CreatePurchaseReturnCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        LocalDate returnDate = command.returnDate() != null ? command.returnDate() : LocalDate.now();

        PurchaseReturnNumber number = numberGenerator.generate(tenantId, companyId, returnDate.getYear());

        List<PurchaseReturnLine> lines = command.lines() != null
                ? command.lines().stream()
                .map(lc -> new PurchaseReturnLine(
                        PurchaseReturnLineId.newId(), null,
                        lc.receiptLineId(), lc.itemId(), lc.itemDescription(),
                        lc.unitCode(), lc.quantity(), lc.lineReason()
                ))
                .toList()
                : List.of();

        PurchaseReturnId id = PurchaseReturnId.newId();
        PurchaseReturn ret = new PurchaseReturn(
                id, tenantId, companyId, number,
                command.purchaseOrderId(), command.goodsReceiptId(),
                command.vendorId(), command.warehouseId(),
                returnDate, ReturnReason.valueOf(command.reason()),
                PurchaseReturnStatus.DRAFT, lines
        );

        purchaseReturnRepository.save(ret);
        return id;
    }

    @Override
    public PurchaseReturn execute(PurchaseReturnId id) {
        TenantId tenantId = TenantContext.get();
        return purchaseReturnRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseReturn not found: " + id));
    }

    @Override
    public List<PurchaseReturn> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return purchaseReturnRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<PurchaseReturn> execute(List<PurchaseReturnId> ids) {
        return purchaseReturnRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void post(String returnId) {
        PurchaseReturn ret = loadReturn(returnId);
        ret.post();
        saveAndPublish(ret);
    }

    @Override
    @Transactional
    public void complete(String returnId) {
        PurchaseReturn ret = loadReturn(returnId);
        ret.complete();
        saveAndPublish(ret);
    }

    private PurchaseReturn loadReturn(String returnId) {
        TenantId tenantId = TenantContext.get();
        return purchaseReturnRepository.findById(PurchaseReturnId.of(returnId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseReturn not found: " + returnId));
    }

    private void saveAndPublish(PurchaseReturn ret) {
        purchaseReturnRepository.save(ret);
        eventPublisher.publishAll(ret.getDomainEvents());
        ret.clearDomainEvents();
    }
}

