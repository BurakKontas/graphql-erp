package tr.kontas.erp.purchase.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.purchase.application.port.PurchaseRequestNumberGeneratorPort;
import tr.kontas.erp.purchase.application.purchaserequest.*;
import tr.kontas.erp.purchase.domain.purchaserequest.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService implements
        CreatePurchaseRequestUseCase,
        GetPurchaseRequestByIdUseCase,
        GetPurchaseRequestsByCompanyUseCase,
        GetPurchaseRequestsByIdsUseCase,
        SubmitPurchaseRequestUseCase,
        ApprovePurchaseRequestUseCase,
        RejectPurchaseRequestUseCase,
        CancelPurchaseRequestUseCase {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public PurchaseRequestId execute(CreatePurchaseRequestCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        LocalDate requestDate = command.requestDate() != null ? command.requestDate() : LocalDate.now();

        PurchaseRequestNumber number = numberGenerator.generate(tenantId, companyId, requestDate.getYear());

        List<PurchaseRequestLine> lines = command.lines() != null
                ? command.lines().stream()
                .map(lc -> new PurchaseRequestLine(
                        PurchaseRequestLineId.newId(),
                        null,
                        lc.itemId(),
                        lc.itemDescription(),
                        lc.unitCode(),
                        lc.quantity(),
                        lc.preferredVendorId(),
                        lc.notes()
                ))
                .toList()
                : List.of();

        PurchaseRequestId id = PurchaseRequestId.newId();
        PurchaseRequest request = new PurchaseRequest(
                id, tenantId, companyId, number,
                command.requestedBy(), null,
                requestDate, command.neededBy(),
                PurchaseRequestStatus.DRAFT, command.notes(),
                lines
        );

        purchaseRequestRepository.save(request);
        return id;
    }

    @Override
    public PurchaseRequest execute(PurchaseRequestId id) {
        TenantId tenantId = TenantContext.get();
        return purchaseRequestRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseRequest not found: " + id));
    }

    @Override
    public List<PurchaseRequest> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return purchaseRequestRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<PurchaseRequest> execute(List<PurchaseRequestId> ids) {
        return purchaseRequestRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void submit(String requestId) {
        PurchaseRequest request = loadRequest(requestId);
        request.submit();
        purchaseRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void approve(String requestId, String approverId) {
        PurchaseRequest request = loadRequest(requestId);
        request.approve(approverId);
        purchaseRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void reject(String requestId, String approverId, String reason) {
        PurchaseRequest request = loadRequest(requestId);
        request.reject(approverId, reason);
        purchaseRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void cancel(String requestId) {
        PurchaseRequest request = loadRequest(requestId);
        request.cancel();
        purchaseRequestRepository.save(request);
    }

    private PurchaseRequest loadRequest(String requestId) {
        TenantId tenantId = TenantContext.get();
        return purchaseRequestRepository.findById(PurchaseRequestId.of(requestId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("PurchaseRequest not found: " + requestId));
    }
}

