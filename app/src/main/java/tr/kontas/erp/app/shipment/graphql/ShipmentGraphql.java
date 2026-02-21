package tr.kontas.erp.app.shipment.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.inventory.dtos.WarehousePayload;
import tr.kontas.erp.app.salesorder.dtos.ShippingAddressPayload;
import tr.kontas.erp.app.shipment.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.domain.shared.Address;
import tr.kontas.erp.shipment.application.shipment.*;
import tr.kontas.erp.shipment.domain.shipment.Shipment;
import tr.kontas.erp.shipment.domain.shipment.ShipmentId;
import tr.kontas.erp.shipment.domain.shipment.ShipmentLine;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class ShipmentGraphql {

    private final CreateShipmentUseCase createShipmentUseCase;
    private final GetShipmentByIdUseCase getShipmentByIdUseCase;
    private final GetShipmentsByCompanyUseCase getShipmentsByCompanyUseCase;
    private final SetTrackingInfoUseCase setTrackingInfoUseCase;
    private final DispatchShipmentUseCase dispatchShipmentUseCase;
    private final DeliverShipmentUseCase deliverShipmentUseCase;

    public static ShipmentPayload toPayload(Shipment s) {
        ShippingAddressPayload saPayload = null;
        Address a = s.getShippingAddress();
        if (a != null) {
            saPayload = new ShippingAddressPayload(
                    a.getAddressLine1(), a.getAddressLine2(),
                    a.getCity(), a.getStateOrProvince(),
                    a.getPostalCode(), a.getCountryCode()
            );
        }

        List<ShipmentLinePayload> linePayloads = s.getLines().stream()
                .map(ShipmentGraphql::toLinePayload)
                .collect(Collectors.toList());

        return new ShipmentPayload(
                s.getId().asUUID().toString(),
                s.getCompanyId().asUUID().toString(),
                s.getNumber().getValue(),
                s.getDeliveryOrderId(),
                s.getSalesOrderId(),
                s.getWarehouseId(),
                saPayload,
                s.getTrackingNumber(),
                s.getCarrierName(),
                s.getStatus().name(),
                s.getDispatchedAt() != null ? s.getDispatchedAt().toString() : null,
                s.getDeliveredAt() != null ? s.getDeliveredAt().toString() : null,
                linePayloads
        );
    }

    public static ShipmentLinePayload toLinePayload(ShipmentLine line) {
        return new ShipmentLinePayload(
                line.getId().asUUID().toString(),
                line.getDeliveryOrderLineId(),
                line.getItemId(),
                line.getItemDescription(),
                line.getUnitCode(),
                line.getQuantity()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public ShipmentPayload shipment(@InputArgument("id") String id) {
        Shipment s = getShipmentByIdUseCase.execute(ShipmentId.of(id));
        return toPayload(s);
    }

    @DgsQuery
    public List<ShipmentPayload> shipments(@InputArgument("companyId") String companyId) {
        return getShipmentsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(ShipmentGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public ShipmentPayload createShipment(@InputArgument("input") CreateShipmentInput input) {
        List<CreateShipmentCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreateShipmentCommand.LineCommand(
                        l.getDeliveryOrderLineId(), l.getItemId(), l.getItemDescription(),
                        l.getUnitCode(), l.getQuantity()
                )).toList();

        CreateShipmentCommand command = new CreateShipmentCommand(
                CompanyId.of(input.getCompanyId()),
                input.getDeliveryOrderId(), input.getSalesOrderId(), input.getWarehouseId(),
                input.getAddressLine1(), input.getAddressLine2(),
                input.getCity(), input.getStateOrProvince(),
                input.getPostalCode(), input.getCountryCode(),
                lines
        );

        ShipmentId id = createShipmentUseCase.execute(command);
        Shipment s = getShipmentByIdUseCase.execute(id);
        return toPayload(s);
    }

    @DgsMutation
    public ShipmentPayload setShipmentTrackingInfo(@InputArgument("input") SetTrackingInfoInput input) {
        setTrackingInfoUseCase.execute(input.getShipmentId(), input.getCarrierName(), input.getTrackingNumber());
        Shipment s = getShipmentByIdUseCase.execute(ShipmentId.of(input.getShipmentId()));
        return toPayload(s);
    }

    @DgsMutation
    public ShipmentPayload dispatchShipment(@InputArgument("shipmentId") String shipmentId) {
        dispatchShipmentUseCase.dispatch(shipmentId);
        Shipment s = getShipmentByIdUseCase.execute(ShipmentId.of(shipmentId));
        return toPayload(s);
    }

    @DgsMutation
    public ShipmentPayload deliverShipment(@InputArgument("shipmentId") String shipmentId) {
        deliverShipmentUseCase.deliver(shipmentId);
        Shipment s = getShipmentByIdUseCase.execute(ShipmentId.of(shipmentId));
        return toPayload(s);
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "ShipmentPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        ShipmentPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "ShipmentPayload")
    public CompletableFuture<DeliveryOrderPayload> deliveryOrder(DgsDataFetchingEnvironment dfe) {
        ShipmentPayload payload = dfe.getSource();
        DataLoader<String, DeliveryOrderPayload> loader = dfe.getDataLoader("deliveryOrderLoader");
        if (loader == null || payload.getDeliveryOrderId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getDeliveryOrderId());
    }

    @DgsData(parentType = "ShipmentPayload")
    public CompletableFuture<WarehousePayload> warehouse(DgsDataFetchingEnvironment dfe) {
        ShipmentPayload payload = dfe.getSource();
        DataLoader<String, WarehousePayload> loader = dfe.getDataLoader("warehouseLoader");
        if (loader == null || payload.getWarehouseId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getWarehouseId());
    }
}

