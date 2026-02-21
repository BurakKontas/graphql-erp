package tr.kontas.erp.app.shipment.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.app.shipment.dtos.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.shipment.application.shipmentreturn.*;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturn;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnLine;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class ShipmentReturnGraphql {

    private final CreateShipmentReturnUseCase createShipmentReturnUseCase;
    private final GetShipmentReturnByIdUseCase getShipmentReturnByIdUseCase;
    private final GetShipmentReturnsByCompanyUseCase getShipmentReturnsByCompanyUseCase;
    private final ReceiveShipmentReturnUseCase receiveShipmentReturnUseCase;
    private final CompleteShipmentReturnUseCase completeShipmentReturnUseCase;
    private final CancelShipmentReturnUseCase cancelShipmentReturnUseCase;

    public static ShipmentReturnPayload toPayload(ShipmentReturn sr) {
        List<ShipmentReturnLinePayload> linePayloads = sr.getLines().stream()
                .map(ShipmentReturnGraphql::toLinePayload)
                .collect(Collectors.toList());

        return new ShipmentReturnPayload(
                sr.getId().asUUID().toString(),
                sr.getCompanyId().asUUID().toString(),
                sr.getNumber().getValue(),
                sr.getShipmentId(),
                sr.getSalesOrderId(),
                sr.getWarehouseId(),
                sr.getReason().getValue(),
                sr.getStatus().name(),
                sr.getReceivedAt() != null ? sr.getReceivedAt().toString() : null,
                linePayloads
        );
    }

    public static ShipmentReturnLinePayload toLinePayload(ShipmentReturnLine line) {
        return new ShipmentReturnLinePayload(
                line.getId().asUUID().toString(),
                line.getShipmentLineId(),
                line.getItemId(),
                line.getItemDescription(),
                line.getUnitCode(),
                line.getQuantity(),
                line.getLineReason()
        );
    }

    // ─── Queries ───

    @DgsQuery
    public ShipmentReturnPayload shipmentReturn(@InputArgument("id") String id) {
        ShipmentReturn sr = getShipmentReturnByIdUseCase.execute(ShipmentReturnId.of(id));
        return toPayload(sr);
    }

    @DgsQuery
    public List<ShipmentReturnPayload> shipmentReturns(@InputArgument("companyId") String companyId) {
        return getShipmentReturnsByCompanyUseCase.execute(CompanyId.of(companyId))
                .stream().map(ShipmentReturnGraphql::toPayload).toList();
    }

    // ─── Mutations ───

    @DgsMutation
    public ShipmentReturnPayload createShipmentReturn(@InputArgument("input") CreateShipmentReturnInput input) {
        List<CreateShipmentReturnCommand.LineCommand> lines = input.getLines().stream()
                .map(l -> new CreateShipmentReturnCommand.LineCommand(
                        l.getShipmentLineId(), l.getItemId(), l.getItemDescription(),
                        l.getUnitCode(), l.getQuantity(), l.getLineReason()
                )).toList();

        CreateShipmentReturnCommand command = new CreateShipmentReturnCommand(
                CompanyId.of(input.getCompanyId()),
                input.getShipmentId(), input.getSalesOrderId(), input.getWarehouseId(),
                input.getReason(), lines
        );

        ShipmentReturnId id = createShipmentReturnUseCase.execute(command);
        ShipmentReturn sr = getShipmentReturnByIdUseCase.execute(id);
        return toPayload(sr);
    }

    @DgsMutation
    public ShipmentReturnPayload receiveShipmentReturn(@InputArgument("shipmentReturnId") String id) {
        receiveShipmentReturnUseCase.receive(id);
        ShipmentReturn sr = getShipmentReturnByIdUseCase.execute(ShipmentReturnId.of(id));
        return toPayload(sr);
    }

    @DgsMutation
    public ShipmentReturnPayload completeShipmentReturn(@InputArgument("shipmentReturnId") String id) {
        completeShipmentReturnUseCase.complete(id);
        ShipmentReturn sr = getShipmentReturnByIdUseCase.execute(ShipmentReturnId.of(id));
        return toPayload(sr);
    }

    @DgsMutation
    public ShipmentReturnPayload cancelShipmentReturn(@InputArgument("shipmentReturnId") String id) {
        cancelShipmentReturnUseCase.cancel(id);
        ShipmentReturn sr = getShipmentReturnByIdUseCase.execute(ShipmentReturnId.of(id));
        return toPayload(sr);
    }

    // ─── Nested resolvers ───

    @DgsData(parentType = "ShipmentReturnPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        ShipmentReturnPayload payload = dfe.getSource();
        DataLoader<String, CompanyPayload> loader = dfe.getDataLoader("companyLoader");
        if (loader == null || payload.getCompanyId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getCompanyId());
    }

    @DgsData(parentType = "ShipmentReturnPayload")
    public CompletableFuture<ShipmentPayload> shipment(DgsDataFetchingEnvironment dfe) {
        ShipmentReturnPayload payload = dfe.getSource();
        DataLoader<String, ShipmentPayload> loader = dfe.getDataLoader("shipmentLoader");
        if (loader == null || payload.getShipmentId() == null) return CompletableFuture.completedFuture(null);
        return loader.load(payload.getShipmentId());
    }
}

