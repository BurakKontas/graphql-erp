package tr.kontas.erp.app.salesorder.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.businesspartner.dtos.BusinessPartnerPayload;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.salesorder.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.sales.application.salesorder.*;
import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderLine;
import tr.kontas.erp.core.domain.shared.Address;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class SalesOrderGraphql {

    private final CreateSalesOrderUseCase createSalesOrderUseCase;
    private final GetSalesOrderByIdUseCase getSalesOrderByIdUseCase;
    private final GetSalesOrdersByCompanyUseCase getSalesOrdersByCompanyUseCase;
    private final AddSalesOrderLineUseCase addSalesOrderLineUseCase;
    private final UpdateSalesOrderLineUseCase updateSalesOrderLineUseCase;
    private final RemoveSalesOrderLineUseCase removeSalesOrderLineUseCase;
    private final UpdateSalesOrderHeaderUseCase updateSalesOrderHeaderUseCase;
    private final SendSalesOrderUseCase sendSalesOrderUseCase;
    private final ConfirmSalesOrderUseCase confirmSalesOrderUseCase;
    private final CancelSalesOrderUseCase cancelSalesOrderUseCase;
    private final AcceptSalesOrderUseCase acceptSalesOrderUseCase;

    public static SalesOrderPayload toPayload(SalesOrder order) {
        ShippingAddressPayload shippingAddressPayload = null;
        Address shippingAddress = order.getShippingAddress();
        if (shippingAddress != null) {
            shippingAddressPayload = new ShippingAddressPayload(
                    shippingAddress.getAddressLine1(),
                    shippingAddress.getAddressLine2(),
                    shippingAddress.getCity(),
                    shippingAddress.getStateOrProvince(),
                    shippingAddress.getPostalCode(),
                    shippingAddress.getCountryCode()
            );
        }

        List<SalesOrderLinePayload> linePayloads = order.getLines().stream()
                .map(SalesOrderGraphql::toLinePayload)
                .collect(Collectors.toList());

        return new SalesOrderPayload(
                order.getId().asUUID().toString(),
                order.getCompanyId().asUUID().toString(),
                order.getOrderNumber().getValue(),
                order.getOrderDate() != null ? order.getOrderDate().toString() : null,
                order.getExpiryDate() != null ? order.getExpiryDate().toString() : null,
                order.getCustomerId() != null ? order.getCustomerId().asUUID().toString() : null,
                order.getCurrency() != null ? order.getCurrency().getId().getValue() : null,
                order.getPaymentTerm() != null ? order.getPaymentTerm().getId().getValue() : null,
                shippingAddressPayload,
                order.getStatus().name(),
                order.getFulfillmentStatus().name(),
                order.getInvoicingStatus().name(),
                order.getInvoicedAmount(),
                order.getSubtotal(),
                order.getTaxTotal(),
                order.getTotal(),
                linePayloads
        );
    }

    public static SalesOrderLinePayload toLinePayload(SalesOrderLine line) {
        return new SalesOrderLinePayload(
                line.getId().asUUID().toString(),
                line.getSequence(),
                line.getItemId(),
                line.getItemDescription(),
                line.getUnitCode(),
                line.getQuantity().getValue(),
                line.getUnitPrice(),
                line.getTax() != null ? line.getTax().getId().getValue() : null,
                line.getTax() != null ? line.getTax().getRate().getValue() : null,
                line.getLineTotal(),
                line.getTaxAmount(),
                line.getLineTotalWithTax()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public SalesOrderPayload salesOrder(@InputArgument("id") String id) {
        SalesOrderId salesOrderId = SalesOrderId.of(id);
        SalesOrder order = getSalesOrderByIdUseCase.execute(salesOrderId);
        return toPayload(order);
    }

    @DgsQuery
    public List<SalesOrderPayload> salesOrders(@InputArgument("companyId") String companyId) {
        CompanyId cid = CompanyId.of(companyId);
        return getSalesOrdersByCompanyUseCase.execute(cid)
                .stream()
                .map(SalesOrderGraphql::toPayload)
                .toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public SalesOrderPayload createSalesOrder(@InputArgument("input") CreateSalesOrderInput input) {
        CompanyId companyId = CompanyId.of(input.getCompanyId());

        CreateSalesOrderCommand command = new CreateSalesOrderCommand(
                companyId,
                input.getCustomerId(),
                input.getCurrencyCode(),
                input.getPaymentTermCode(),
                input.getOrderDate() != null ? LocalDate.parse(input.getOrderDate()) : null
        );

        SalesOrderId id = createSalesOrderUseCase.execute(command);
        SalesOrder order = getSalesOrderByIdUseCase.execute(id);
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload addSalesOrderLine(@InputArgument("input") AddSalesOrderLineInput input) {
        AddSalesOrderLineCommand command = new AddSalesOrderLineCommand(
                input.getOrderId(),
                input.getItemId(),
                input.getItemDescription(),
                input.getUnitCode(),
                input.getQuantity(),
                input.getUnitPrice(),
                input.getTaxCode()
        );

        addSalesOrderLineUseCase.execute(command);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(input.getOrderId()));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload updateSalesOrderLine(@InputArgument("input") UpdateSalesOrderLineInput input) {
        UpdateSalesOrderLineCommand command = new UpdateSalesOrderLineCommand(
                input.getOrderId(),
                input.getLineId(),
                input.getQuantity(),
                input.getUnitPrice()
        );

        updateSalesOrderLineUseCase.execute(command);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(input.getOrderId()));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload removeSalesOrderLine(
            @InputArgument("orderId") String orderId,
            @InputArgument("lineId") String lineId) {

        removeSalesOrderLineUseCase.removeLine(orderId, lineId);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(orderId));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload updateSalesOrderHeader(@InputArgument("input") UpdateSalesOrderHeaderInput input) {
        UpdateSalesOrderHeaderCommand.ShippingAddressInput shippingAddressInput = null;
        if (input.getShippingAddress() != null) {
            var shippingAddress = input.getShippingAddress();
            shippingAddressInput = new UpdateSalesOrderHeaderCommand.ShippingAddressInput(
                    shippingAddress.getAddressLine1(),
                    shippingAddress.getAddressLine2(),
                    shippingAddress.getCity(),
                    shippingAddress.getStateOrProvince(),
                    shippingAddress.getPostalCode(),
                    shippingAddress.getCountryCode()
            );
        }

        UpdateSalesOrderHeaderCommand command = new UpdateSalesOrderHeaderCommand(
                input.getOrderId(),
                input.getOrderDate() != null ? LocalDate.parse(input.getOrderDate()) : null,
                input.getExpiryDate() != null ? LocalDate.parse(input.getExpiryDate()) : null,
                input.getPaymentTermCode(),
                shippingAddressInput
        );

        updateSalesOrderHeaderUseCase.execute(command);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(input.getOrderId()));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload sendSalesOrder(@InputArgument("orderId") String orderId) {
        sendSalesOrderUseCase.send(orderId);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(orderId));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload acceptSalesOrder(@InputArgument("orderId") String orderId) {
        acceptSalesOrderUseCase.accept(orderId);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(orderId));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload confirmSalesOrder(@InputArgument("orderId") String orderId) {
        confirmSalesOrderUseCase.confirm(orderId);

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(orderId));
        return toPayload(order);
    }

    @DgsMutation
    public SalesOrderPayload cancelSalesOrder(@InputArgument("input") CancelSalesOrderInput input) {
        cancelSalesOrderUseCase.cancel(input.getOrderId(), input.getReason());

        SalesOrder order = getSalesOrderByIdUseCase.execute(SalesOrderId.of(input.getOrderId()));
        return toPayload(order);
    }

    // ─── Nested resolvers via DataLoaders ───

    @DgsData(parentType = "SalesOrderPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        SalesOrderPayload payload = dfe.getSource();

        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");
        if (dataLoader == null || payload.getCompanyId() == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "SalesOrderPayload")
    public CompletableFuture<BusinessPartnerPayload> customer(DgsDataFetchingEnvironment dfe) {
        SalesOrderPayload payload = dfe.getSource();

        DataLoader<String, BusinessPartnerPayload> dataLoader = dfe.getDataLoader("businessPartnerLoader");
        if (dataLoader == null || payload.getCustomerId() == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(payload.getCustomerId());
    }
}
