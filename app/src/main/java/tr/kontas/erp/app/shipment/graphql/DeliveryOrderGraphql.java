package tr.kontas.erp.app.shipment.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.salesorder.dtos.SalesOrderPayload;
import tr.kontas.erp.app.salesorder.dtos.ShippingAddressPayload;
import tr.kontas.erp.app.shipment.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.shipment.application.deliveryorder.*;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrder;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class DeliveryOrderGraphql {

    private final CreateDeliveryOrderUseCase createDeliveryOrderUseCase;
    private final GetDeliveryOrderByIdUseCase getDeliveryOrderByIdUseCase;
    private final GetDeliveryOrdersByCompanyUseCase getDeliveryOrdersByCompanyUseCase;
    private final CancelDeliveryOrderUseCase cancelDeliveryOrderUseCase;

    public static DeliveryOrderPayload toPayload(DeliveryOrder order) {
        ShippingAddressPayload saPayload = null;
        Address a = order.getShippingAddress();
        if (a != null) {
            saPayload = new ShippingAddressPayload(
                    a.getAddressLine1(), a.getAddressLine2(),
                    a.getCity(), a.getStateOrProvince(),
                    a.getPostalCode(), a.getCountryCode()
            );
        }

        List<DeliveryOrderLinePayload> linePayloads = order.getLines().stream()
                .map(DeliveryOrderGraphql::toLinePayload)
                .collect(Collectors.toList());

        return new DeliveryOrderPayload(
                order.getId().asUUID().toString(),
                order.getCompanyId().asUUID().toString(),
                order.getNumber().getValue(),
                order.getSalesOrderId(),
                order.getSalesOrderNumber(),
                order.getCustomerId(),
                saPayload,
                order.getStatus().name(),
                linePayloads
        );
    }

    public static DeliveryOrderLinePayload toLinePayload(DeliveryOrderLine line) {
        BigDecimal remaining = line.getOrderedQty().subtract(line.getShippedQty());
        if (remaining.compareTo(BigDecimal.ZERO) < 0) remaining = BigDecimal.ZERO;
        return new DeliveryOrderLinePayload(
                line.getId().asUUID().toString(),
                line.getSalesOrderLineId(),
                line.getItemId(),
                line.getItemDescription(),
                line.getUnitCode(),
                line.getOrderedQty(),
                line.getShippedQty(),
                remaining
        );
    }

    // ─── Queries ───

    @DgsQuery
    public DeliveryOrderPayload deliveryOrder(@InputArgument("id") String id) {
        DeliveryOrder order = getDeliveryOrderByIdUseCase.execute(DeliveryOrderId.of(id));
        return toPayload(order);
    }

    @DgsQuery
    public List<DeliveryOrderPayload> deliveryOrders(@InputArgument("companyId") String companyId) {
        return getDeliveryOrdersByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(DeliveryOrderGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public DeliveryOrderPayload createDeliveryOrder(@InputArgument("input") CreateDeliveryOrderInput input) {
        List<CreateDeliveryOrderCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreateDeliveryOrderCommand.LineCommand(
                        l.getSalesOrderLineId(), l.getItemId(), l.getItemDescription(),
                        l.getUnitCode(), l.getOrderedQty()
                )).toList();

        CreateDeliveryOrderCommand command = new CreateDeliveryOrderCommand(
                CompanyId.of(input.getCompanyId()),
                input.getSalesOrderId(), input.getSalesOrderNumber(), input.getCustomerId(),
                input.getAddressLine1(), input.getAddressLine2(),
                input.getCity(), input.getStateOrProvince(),
                input.getPostalCode(), input.getCountryCode(),
                lines
        );

        DeliveryOrderId id = createDeliveryOrderUseCase.execute(command);
        DeliveryOrder order = getDeliveryOrderByIdUseCase.execute(id);
        return toPayload(order);
    }

    @DgsMutation
    public DeliveryOrderPayload cancelDeliveryOrder(@InputArgument("input") CancelDeliveryOrderInput input) {
        cancelDeliveryOrderUseCase.cancel(input.getDeliveryOrderId(), input.getReason());
        DeliveryOrder order = getDeliveryOrderByIdUseCase.execute(DeliveryOrderId.of(input.getDeliveryOrderId()));
        return toPayload(order);
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "DeliveryOrderPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        DeliveryOrderPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "DeliveryOrderPayload")
    public CompletableFuture<SalesOrderPayload> salesOrder(DgsDataFetchingEnvironment dfe) {
        DeliveryOrderPayload payload = dfe.getSource();
        DataLoader<String, SalesOrderPayload> loader = dfe.getDataLoader("salesOrderLoader");
        if (loader == null || payload.getSalesOrderId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getSalesOrderId());
    }
}

