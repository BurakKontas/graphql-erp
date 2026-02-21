package tr.kontas.erp.purchase.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.purchase.application.goodsreceipt.*;
import tr.kontas.erp.purchase.application.port.GoodsReceiptNumberGeneratorPort;
import tr.kontas.erp.purchase.domain.goodsreceipt.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReceiptService implements
        CreateGoodsReceiptUseCase,
        GetGoodsReceiptByIdUseCase,
        GetGoodsReceiptsByCompanyUseCase,
        GetGoodsReceiptsByIdsUseCase,
        PostGoodsReceiptUseCase {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public GoodsReceiptId execute(CreateGoodsReceiptCommand command) {
        TenantId tenantId = TenantContext.get();
        CompanyId companyId = command.companyId();
        LocalDate receiptDate = command.receiptDate() != null ? command.receiptDate() : LocalDate.now();

        GoodsReceiptNumber number = numberGenerator.generate(tenantId, companyId, receiptDate.getYear());

        List<GoodsReceiptLine> lines = command.lines() != null
                ? command.lines().stream()
                .map(lc -> new GoodsReceiptLine(
                        GoodsReceiptLineId.newId(), null,
                        lc.poLineId(), lc.itemId(), lc.itemDescription(),
                        lc.unitCode(), lc.quantity(), lc.batchNote()
                ))
                .toList()
                : List.of();

        GoodsReceiptId id = GoodsReceiptId.newId();
        GoodsReceipt receipt = new GoodsReceipt(
                id, tenantId, companyId, number,
                command.purchaseOrderId(), command.vendorId(), command.warehouseId(),
                receiptDate, GoodsReceiptStatus.DRAFT, command.vendorDeliveryNote(),
                lines
        );

        goodsReceiptRepository.save(receipt);
        return id;
    }

    @Override
    public GoodsReceipt execute(GoodsReceiptId id) {
        TenantId tenantId = TenantContext.get();
        return goodsReceiptRepository.findById(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("GoodsReceipt not found: " + id));
    }

    @Override
    public List<GoodsReceipt> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return goodsReceiptRepository.findByCompanyId(tenantId, companyId);
    }

    @Override
    public List<GoodsReceipt> execute(List<GoodsReceiptId> ids) {
        return goodsReceiptRepository.findByIds(ids);
    }

    @Override
    @Transactional
    public void post(String receiptId) {
        TenantId tenantId = TenantContext.get();
        GoodsReceipt receipt = goodsReceiptRepository.findById(GoodsReceiptId.of(receiptId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("GoodsReceipt not found: " + receiptId));
        receipt.post();
        saveAndPublish(receipt);
    }

    private void saveAndPublish(GoodsReceipt receipt) {
        goodsReceiptRepository.save(receipt);
        eventPublisher.publishAll(receipt.getDomainEvents());
        receipt.clearDomainEvents();
    }
}

