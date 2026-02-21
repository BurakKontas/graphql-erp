package tr.kontas.erp.app.purchase.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.businesspartner.dtos.BusinessPartnerPayload;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.purchase.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.purchase.application.goodsreceipt.*;
import tr.kontas.erp.purchase.application.purchaseorder.*;
import tr.kontas.erp.purchase.application.purchaserequest.*;
import tr.kontas.erp.purchase.application.purchasereturn.*;
import tr.kontas.erp.purchase.application.vendorcatalog.*;
import tr.kontas.erp.purchase.application.vendorinvoice.*;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceipt;
import tr.kontas.erp.purchase.domain.goodsreceipt.GoodsReceiptId;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrder;
import tr.kontas.erp.purchase.domain.purchaseorder.PurchaseOrderId;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequest;
import tr.kontas.erp.purchase.domain.purchaserequest.PurchaseRequestId;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturn;
import tr.kontas.erp.purchase.domain.purchasereturn.PurchaseReturnId;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalog;
import tr.kontas.erp.purchase.domain.vendorcatalog.VendorCatalogId;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoice;
import tr.kontas.erp.purchase.domain.vendorinvoice.VendorInvoiceId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class PurchaseGraphql {

    private final CreatePurchaseRequestUseCase createPurchaseRequestUseCase;
    private final GetPurchaseRequestByIdUseCase getPurchaseRequestByIdUseCase;
    private final GetPurchaseRequestsByCompanyUseCase getPurchaseRequestsByCompanyUseCase;
    private final SubmitPurchaseRequestUseCase submitPurchaseRequestUseCase;
    private final ApprovePurchaseRequestUseCase approvePurchaseRequestUseCase;
    private final RejectPurchaseRequestUseCase rejectPurchaseRequestUseCase;
    private final CancelPurchaseRequestUseCase cancelPurchaseRequestUseCase;

    private final CreatePurchaseOrderUseCase createPurchaseOrderUseCase;
    private final GetPurchaseOrderByIdUseCase getPurchaseOrderByIdUseCase;
    private final GetPurchaseOrdersByCompanyUseCase getPurchaseOrdersByCompanyUseCase;
    private final SendPurchaseOrderUseCase sendPurchaseOrderUseCase;
    private final ConfirmPurchaseOrderUseCase confirmPurchaseOrderUseCase;
    private final CancelPurchaseOrderUseCase cancelPurchaseOrderUseCase;

    private final CreateGoodsReceiptUseCase createGoodsReceiptUseCase;
    private final GetGoodsReceiptByIdUseCase getGoodsReceiptByIdUseCase;
    private final GetGoodsReceiptsByCompanyUseCase getGoodsReceiptsByCompanyUseCase;
    private final PostGoodsReceiptUseCase postGoodsReceiptUseCase;

    private final CreateVendorInvoiceUseCase createVendorInvoiceUseCase;
    private final GetVendorInvoiceByIdUseCase getVendorInvoiceByIdUseCase;
    private final GetVendorInvoicesByCompanyUseCase getVendorInvoicesByCompanyUseCase;
    private final PostVendorInvoiceUseCase postVendorInvoiceUseCase;
    private final CancelVendorInvoiceUseCase cancelVendorInvoiceUseCase;

    private final CreatePurchaseReturnUseCase createPurchaseReturnUseCase;
    private final GetPurchaseReturnByIdUseCase getPurchaseReturnByIdUseCase;
    private final GetPurchaseReturnsByCompanyUseCase getPurchaseReturnsByCompanyUseCase;
    private final PostPurchaseReturnUseCase postPurchaseReturnUseCase;
    private final CompletePurchaseReturnUseCase completePurchaseReturnUseCase;

    private final CreateVendorCatalogUseCase createVendorCatalogUseCase;
    private final GetVendorCatalogByIdUseCase getVendorCatalogByIdUseCase;
    private final GetVendorCatalogsByCompanyUseCase getVendorCatalogsByCompanyUseCase;
    private final DeactivateVendorCatalogUseCase deactivateVendorCatalogUseCase;

    // ─── Payload Converters ───

    public static PurchaseRequestPayload toPayload(PurchaseRequest req) {
        List<PurchaseRequestLinePayload> lines = req.getLines().stream()
                .map(l -> new PurchaseRequestLinePayload(
                        l.getId().asUUID().toString(), l.getItemId(), l.getItemDescription(),
                        l.getUnitCode(), l.getQuantity(), l.getPreferredVendorId(), l.getNotes()
                ))
                .toList();
        return new PurchaseRequestPayload(
                req.getId().asUUID().toString(), req.getCompanyId().asUUID().toString(),
                req.getRequestNumber().getValue(), req.getRequestedBy(), req.getApprovedBy(),
                req.getRequestDate() != null ? req.getRequestDate().toString() : null,
                req.getNeededBy() != null ? req.getNeededBy().toString() : null,
                req.getStatus().name(), req.getNotes(), lines
        );
    }

    public static PurchaseOrderPayload toPayload(PurchaseOrder order) {
        AddressPayload addressPayload = null;
        Address addr = order.getDeliveryAddress();
        if (addr != null) {
            addressPayload = new AddressPayload(
                    addr.getAddressLine1(), addr.getAddressLine2(), addr.getCity(),
                    addr.getStateOrProvince(), addr.getPostalCode(), addr.getCountryCode()
            );
        }
        List<PurchaseOrderLinePayload> lines = order.getLines().stream()
                .map(l -> new PurchaseOrderLinePayload(
                        l.getId().asUUID().toString(), l.getRequestLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(), l.getOrderedQty(), l.getReceivedQty(),
                        l.getRemainingQty(), l.getUnitPrice(), l.getTaxCode(), l.getTaxRate(),
                        l.getLineTotal(), l.getTaxAmount(), l.getExpenseAccountId()
                ))
                .toList();
        return new PurchaseOrderPayload(
                order.getId().asUUID().toString(), order.getCompanyId().asUUID().toString(),
                order.getOrderNumber().getValue(), order.getRequestId(), order.getVendorId(),
                order.getVendorName(),
                order.getOrderDate() != null ? order.getOrderDate().toString() : null,
                order.getExpectedDeliveryDate() != null ? order.getExpectedDeliveryDate().toString() : null,
                order.getCurrencyCode(), order.getPaymentTermCode(), addressPayload,
                order.getStatus().name(), order.getSubtotal(), order.getTaxTotal(), order.getTotal(), lines
        );
    }

    public static GoodsReceiptPayload toPayload(GoodsReceipt receipt) {
        List<GoodsReceiptLinePayload> lines = receipt.getLines().stream()
                .map(l -> new GoodsReceiptLinePayload(
                        l.getId().asUUID().toString(), l.getPoLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(), l.getQuantity(), l.getBatchNote()
                ))
                .toList();
        return new GoodsReceiptPayload(
                receipt.getId().asUUID().toString(), receipt.getCompanyId().asUUID().toString(),
                receipt.getReceiptNumber().getValue(), receipt.getPurchaseOrderId(), receipt.getVendorId(),
                receipt.getWarehouseId(),
                receipt.getReceiptDate() != null ? receipt.getReceiptDate().toString() : null,
                receipt.getStatus().name(), receipt.getVendorDeliveryNote(), lines
        );
    }

    public static VendorInvoicePayload toPayload(VendorInvoice invoice) {
        List<VendorInvoiceLinePayload> lines = invoice.getLines().stream()
                .map(l -> new VendorInvoiceLinePayload(
                        l.getId().asUUID().toString(), l.getPoLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(), l.getQuantity(), l.getUnitPrice(),
                        l.getTaxCode(), l.getTaxRate(), l.getLineTotal(), l.getTaxAmount(),
                        l.getLineTotalWithTax(), l.getAccountId()
                ))
                .toList();
        return new VendorInvoicePayload(
                invoice.getId().asUUID().toString(), invoice.getCompanyId().asUUID().toString(),
                invoice.getInvoiceNumber().getValue(), invoice.getVendorInvoiceRef(),
                invoice.getPurchaseOrderId(), invoice.getVendorId(), invoice.getVendorName(),
                invoice.getAccountingPeriodId(),
                invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().toString() : null,
                invoice.getDueDate() != null ? invoice.getDueDate().toString() : null,
                invoice.getCurrencyCode(), invoice.getExchangeRate(), invoice.getStatus().name(),
                invoice.getSubtotal(), invoice.getTaxTotal(), invoice.getTotal(),
                invoice.getPaidAmount(), invoice.getRemainingAmount(), lines
        );
    }

    public static PurchaseReturnPayload toPayload(PurchaseReturn ret) {
        List<PurchaseReturnLinePayload> lines = ret.getLines().stream()
                .map(l -> new PurchaseReturnLinePayload(
                        l.getId().asUUID().toString(), l.getReceiptLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(), l.getQuantity(), l.getLineReason()
                ))
                .toList();
        return new PurchaseReturnPayload(
                ret.getId().asUUID().toString(), ret.getCompanyId().asUUID().toString(),
                ret.getReturnNumber().getValue(), ret.getPurchaseOrderId(), ret.getGoodsReceiptId(),
                ret.getVendorId(), ret.getWarehouseId(),
                ret.getReturnDate() != null ? ret.getReturnDate().toString() : null,
                ret.getReason().name(), ret.getStatus().name(), lines
        );
    }

    public static VendorCatalogPayload toPayload(VendorCatalog catalog) {
        List<VendorCatalogEntryPayload> entries = catalog.getEntries().stream()
                .map(e -> new VendorCatalogEntryPayload(
                        e.getId().asUUID().toString(), e.getItemId(), e.getItemDescription(),
                        e.getUnitCode(), e.getUnitPrice(), e.getMinimumOrderQty(),
                        e.getPriceBreakQty(), e.getPriceBreakUnitPrice()
                ))
                .toList();
        return new VendorCatalogPayload(
                catalog.getId().asUUID().toString(), catalog.getCompanyId().asUUID().toString(),
                catalog.getVendorId(), catalog.getVendorName(), catalog.getCurrencyCode(),
                catalog.getValidFrom() != null ? catalog.getValidFrom().toString() : null,
                catalog.getValidTo() != null ? catalog.getValidTo().toString() : null,
                catalog.isActive(), entries
        );
    }

    // ─── PurchaseRequest Queries ───

    @DgsQuery
    public PurchaseRequestPayload purchaseRequest(@InputArgument("id") String id) {
        return toPayload(getPurchaseRequestByIdUseCase.execute(PurchaseRequestId.of(id)));
    }

    @DgsQuery
    public List<PurchaseRequestPayload> purchaseRequests(@InputArgument("companyId") String companyId) {
        return getPurchaseRequestsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(PurchaseGraphql::toPayload).toList();
    }

    // ─── PurchaseRequest Mutations ───

    @DgsMutation
    public PurchaseRequestPayload createPurchaseRequest(@InputArgument("input") CreatePurchaseRequestInput input) {
        List<CreatePurchaseRequestCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreatePurchaseRequestCommand.LineCommand(
                        l.getItemId(), l.getItemDescription(),
                        l.getUnitCode(), l.getQuantity(),
                        l.getPreferredVendorId(), l.getNotes()
                ))
                .toList();

        PurchaseRequestId id = createPurchaseRequestUseCase.execute(new CreatePurchaseRequestCommand(
                CompanyId.of(input.getCompanyId()), input.getRequestedBy(),
                input.getRequestDate() != null ? LocalDate.parse(input.getRequestDate()) : null,
                input.getNeededBy() != null ? LocalDate.parse(input.getNeededBy()) : null,
                input.getNotes(), lines
        ));
        return toPayload(getPurchaseRequestByIdUseCase.execute(id));
    }

    @DgsMutation
    public PurchaseRequestPayload submitPurchaseRequest(@InputArgument("requestId") String requestId) {
        submitPurchaseRequestUseCase.submit(requestId);
        return toPayload(getPurchaseRequestByIdUseCase.execute(PurchaseRequestId.of(requestId)));
    }

    @DgsMutation
    public PurchaseRequestPayload approvePurchaseRequest(@InputArgument("requestId") String requestId,
                                                         @InputArgument("approverId") String approverId) {
        approvePurchaseRequestUseCase.approve(requestId, approverId);
        return toPayload(getPurchaseRequestByIdUseCase.execute(PurchaseRequestId.of(requestId)));
    }

    @DgsMutation
    public PurchaseRequestPayload rejectPurchaseRequest(@InputArgument("requestId") String requestId,
                                                        @InputArgument("approverId") String approverId,
                                                        @InputArgument("reason") String reason) {
        rejectPurchaseRequestUseCase.reject(requestId, approverId, reason);
        return toPayload(getPurchaseRequestByIdUseCase.execute(PurchaseRequestId.of(requestId)));
    }

    @DgsMutation
    public PurchaseRequestPayload cancelPurchaseRequest(@InputArgument("requestId") String requestId) {
        cancelPurchaseRequestUseCase.cancel(requestId);
        return toPayload(getPurchaseRequestByIdUseCase.execute(PurchaseRequestId.of(requestId)));
    }

    // ─── PurchaseOrder Queries ───

    @DgsQuery
    public PurchaseOrderPayload purchaseOrder(@InputArgument("id") String id) {
        return toPayload(getPurchaseOrderByIdUseCase.execute(PurchaseOrderId.of(id)));
    }

    @DgsQuery
    public List<PurchaseOrderPayload> purchaseOrders(@InputArgument("companyId") String companyId) {
        return getPurchaseOrdersByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(PurchaseGraphql::toPayload).toList();
    }

    // ─── PurchaseOrder Mutations ───

    @DgsMutation
    public PurchaseOrderPayload createPurchaseOrder(@InputArgument("input") CreatePurchaseOrderInput input) {
        List<CreatePurchaseOrderCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreatePurchaseOrderCommand.LineCommand(
                        l.getRequestLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(),
                        l.getOrderedQty(), l.getUnitPrice(),
                        l.getTaxCode(), l.getTaxRate(),
                        l.getExpenseAccountId()
                ))
                .toList();

        PurchaseOrderId id = createPurchaseOrderUseCase.execute(new CreatePurchaseOrderCommand(
                CompanyId.of(input.getCompanyId()), input.getRequestId(),
                input.getVendorId(), input.getVendorName(),
                input.getOrderDate() != null ? LocalDate.parse(input.getOrderDate()) : null,
                input.getExpectedDeliveryDate() != null ? LocalDate.parse(input.getExpectedDeliveryDate()) : null,
                input.getCurrencyCode(), input.getPaymentTermCode(),
                input.getAddressLine1(), input.getAddressLine2(),
                input.getCity(), input.getStateOrProvince(),
                input.getPostalCode(), input.getCountryCode(), lines
        ));
        return toPayload(getPurchaseOrderByIdUseCase.execute(id));
    }

    @DgsMutation
    public PurchaseOrderPayload sendPurchaseOrder(@InputArgument("orderId") String orderId) {
        sendPurchaseOrderUseCase.send(orderId);
        return toPayload(getPurchaseOrderByIdUseCase.execute(PurchaseOrderId.of(orderId)));
    }

    @DgsMutation
    public PurchaseOrderPayload confirmPurchaseOrder(@InputArgument("orderId") String orderId) {
        confirmPurchaseOrderUseCase.confirm(orderId);
        return toPayload(getPurchaseOrderByIdUseCase.execute(PurchaseOrderId.of(orderId)));
    }

    @DgsMutation
    public PurchaseOrderPayload cancelPurchaseOrder(@InputArgument("input") CancelPurchaseOrderInput input) {
        cancelPurchaseOrderUseCase.cancel(input.getOrderId(), input.getReason());
        return toPayload(getPurchaseOrderByIdUseCase.execute(PurchaseOrderId.of(input.getOrderId())));
    }

    // ─── GoodsReceipt Queries & Mutations ───

    @DgsQuery
    public GoodsReceiptPayload goodsReceipt(@InputArgument("id") String id) {
        return toPayload(getGoodsReceiptByIdUseCase.execute(GoodsReceiptId.of(id)));
    }

    @DgsQuery
    public List<GoodsReceiptPayload> goodsReceipts(@InputArgument("companyId") String companyId) {
        return getGoodsReceiptsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(PurchaseGraphql::toPayload).toList();
    }

    @DgsMutation
    public GoodsReceiptPayload createGoodsReceipt(@InputArgument("input") CreateGoodsReceiptInput input) {
        List<CreateGoodsReceiptCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreateGoodsReceiptCommand.LineCommand(
                        l.getPoLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(),
                        l.getQuantity(), l.getBatchNote()
                ))
                .toList();

        GoodsReceiptId id = createGoodsReceiptUseCase.execute(new CreateGoodsReceiptCommand(
                CompanyId.of(input.getCompanyId()), input.getPurchaseOrderId(),
                input.getVendorId(), input.getWarehouseId(),
                input.getReceiptDate() != null ? LocalDate.parse(input.getReceiptDate()) : null,
                input.getVendorDeliveryNote(), lines
        ));
        return toPayload(getGoodsReceiptByIdUseCase.execute(id));
    }

    @DgsMutation
    public GoodsReceiptPayload postGoodsReceipt(@InputArgument("receiptId") String receiptId) {
        postGoodsReceiptUseCase.post(receiptId);
        return toPayload(getGoodsReceiptByIdUseCase.execute(GoodsReceiptId.of(receiptId)));
    }

    // ─── VendorInvoice Queries & Mutations ───

    @DgsQuery
    public VendorInvoicePayload vendorInvoice(@InputArgument("id") String id) {
        return toPayload(getVendorInvoiceByIdUseCase.execute(VendorInvoiceId.of(id)));
    }

    @DgsQuery
    public List<VendorInvoicePayload> vendorInvoices(@InputArgument("companyId") String companyId) {
        return getVendorInvoicesByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(PurchaseGraphql::toPayload).toList();
    }

    @DgsMutation
    public VendorInvoicePayload createVendorInvoice(@InputArgument("input") CreateVendorInvoiceInput input) {
        List<CreateVendorInvoiceCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreateVendorInvoiceCommand.LineCommand(
                        l.getPoLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(),
                        l.getQuantity(), l.getUnitPrice(),
                        l.getTaxCode(), l.getTaxRate(),
                        l.getAccountId()
                ))
                .toList();

        VendorInvoiceId id = createVendorInvoiceUseCase.execute(new CreateVendorInvoiceCommand(
                CompanyId.of(input.getCompanyId()), input.getVendorInvoiceRef(),
                input.getPurchaseOrderId(), input.getVendorId(),
                input.getVendorName(), input.getAccountingPeriodId(),
                input.getInvoiceDate() != null ? LocalDate.parse(input.getInvoiceDate()) : null,
                input.getDueDate() != null ? LocalDate.parse(input.getDueDate()) : null,
                input.getCurrencyCode(), input.getExchangeRate(),
                lines
        ));
        return toPayload(getVendorInvoiceByIdUseCase.execute(id));
    }

    @DgsMutation
    public VendorInvoicePayload postVendorInvoice(@InputArgument("invoiceId") String invoiceId) {
        postVendorInvoiceUseCase.post(invoiceId);
        return toPayload(getVendorInvoiceByIdUseCase.execute(VendorInvoiceId.of(invoiceId)));
    }

    @DgsMutation
    public VendorInvoicePayload cancelVendorInvoice(@InputArgument("input") CancelVendorInvoiceInput input) {
        cancelVendorInvoiceUseCase.cancel(input.getInvoiceId(), input.getReason());
        return toPayload(getVendorInvoiceByIdUseCase.execute(VendorInvoiceId.of(input.getInvoiceId())));
    }

    // ─── PurchaseReturn Queries & Mutations ───

    @DgsQuery
    public PurchaseReturnPayload purchaseReturn(@InputArgument("id") String id) {
        return toPayload(getPurchaseReturnByIdUseCase.execute(PurchaseReturnId.of(id)));
    }

    @DgsQuery
    public List<PurchaseReturnPayload> purchaseReturns(@InputArgument("companyId") String companyId) {
        return getPurchaseReturnsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(PurchaseGraphql::toPayload).toList();
    }

    @DgsMutation
    public PurchaseReturnPayload createPurchaseReturn(@InputArgument("input") CreatePurchaseReturnInput input) {
        List<CreatePurchaseReturnCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreatePurchaseReturnCommand.LineCommand(
                        l.getReceiptLineId(), l.getItemId(),
                        l.getItemDescription(), l.getUnitCode(),
                        l.getQuantity(), l.getLineReason()
                ))
                .toList();

        PurchaseReturnId id = createPurchaseReturnUseCase.execute(new CreatePurchaseReturnCommand(
                CompanyId.of(input.getCompanyId()), input.getPurchaseOrderId(),
                input.getGoodsReceiptId(), input.getVendorId(),
                input.getWarehouseId(),
                input.getReturnDate() != null ? LocalDate.parse(input.getReturnDate()) : null,
                input.getReason(), lines
        ));
        return toPayload(getPurchaseReturnByIdUseCase.execute(id));
    }

    @DgsMutation
    public PurchaseReturnPayload postPurchaseReturn(@InputArgument("returnId") String returnId) {
        postPurchaseReturnUseCase.post(returnId);
        return toPayload(getPurchaseReturnByIdUseCase.execute(PurchaseReturnId.of(returnId)));
    }

    @DgsMutation
    public PurchaseReturnPayload completePurchaseReturn(@InputArgument("returnId") String returnId) {
        completePurchaseReturnUseCase.complete(returnId);
        return toPayload(getPurchaseReturnByIdUseCase.execute(PurchaseReturnId.of(returnId)));
    }

    // ─── VendorCatalog Queries & Mutations ───

    @DgsQuery
    public VendorCatalogPayload vendorCatalog(@InputArgument("id") String id) {
        return toPayload(getVendorCatalogByIdUseCase.execute(VendorCatalogId.of(id)));
    }

    @DgsQuery
    public List<VendorCatalogPayload> vendorCatalogs(@InputArgument("companyId") String companyId) {
        return getVendorCatalogsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(PurchaseGraphql::toPayload).toList();
    }

    @DgsMutation
    public VendorCatalogPayload createVendorCatalog(@InputArgument("input") CreateVendorCatalogInput input) {
        List<CreateVendorCatalogCommand.EntryCommand> entries = input.getEntries().stream()
                .map(e -> new CreateVendorCatalogCommand.EntryCommand(
                        e.getItemId(), e.getItemDescription(),
                        e.getUnitCode(), e.getUnitPrice(),
                        e.getMinimumOrderQty(),
                        e.getPriceBreakQty(),
                        e.getPriceBreakUnitPrice()
                ))
                .toList();

        VendorCatalogId id = createVendorCatalogUseCase.execute(new CreateVendorCatalogCommand(
                CompanyId.of(input.getCompanyId()), input.getVendorId(), input.getVendorName(),
                input.getCurrencyCode(),
                LocalDate.parse(input.getValidFrom()),
                input.getValidTo() != null ? LocalDate.parse(input.getValidTo()) : null,
                entries
        ));
        return toPayload(getVendorCatalogByIdUseCase.execute(id));
    }

    @DgsMutation
    public VendorCatalogPayload deactivateVendorCatalog(@InputArgument("catalogId") String catalogId) {
        deactivateVendorCatalogUseCase.deactivate(catalogId);
        return toPayload(getVendorCatalogByIdUseCase.execute(VendorCatalogId.of(catalogId)));
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "PurchaseOrderPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        PurchaseOrderPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return dataLoader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "PurchaseOrderPayload")
    public CompletableFuture<BusinessPartnerPayload> vendor(DgsDataFetchingEnvironment dfe) {
        PurchaseOrderPayload payload = dfe.getSource();
        DataLoader<String, BusinessPartnerPayload> dataLoader = dfe.getDataLoader("businessPartnerLoader");
        if (dataLoader == null || payload.getVendorId() == null) return CompletableFuture.completedFuture(null);
        return dataLoader.load(payload.getVendorId());
    }
}

