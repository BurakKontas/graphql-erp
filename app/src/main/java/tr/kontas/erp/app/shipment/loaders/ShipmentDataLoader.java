package tr.kontas.erp.app.shipment.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.shipment.dtos.ShipmentPayload;
import tr.kontas.erp.app.shipment.graphql.ShipmentGraphql;
import tr.kontas.erp.shipment.application.shipment.GetShipmentsByIdsUseCase;
import tr.kontas.erp.shipment.domain.shipment.Shipment;
import tr.kontas.erp.shipment.domain.shipment.ShipmentId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "shipmentLoader")
@RequiredArgsConstructor
public class ShipmentDataLoader implements BatchLoader<String, ShipmentPayload> {

    private final GetShipmentsByIdsUseCase getByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<ShipmentPayload>> load(@NonNull List<String> ids) {
        List<ShipmentId> idList = ids.stream().map(ShipmentId::of).toList();

        Map<String, Shipment> map = getByIdsUseCase.execute(idList).stream()
                .collect(Collectors.toMap(s -> s.getId().asUUID().toString(), Function.identity()));

        List<ShipmentPayload> result = ids.stream()
                .map(id -> {
                    Shipment s = map.get(id);
                    return s != null ? ShipmentGraphql.toPayload(s) : null;
                }).toList();

        return CompletableFuture.completedFuture(result);
    }
}

