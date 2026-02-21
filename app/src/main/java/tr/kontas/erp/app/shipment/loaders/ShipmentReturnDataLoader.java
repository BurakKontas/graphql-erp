package tr.kontas.erp.app.shipment.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.shipment.dtos.ShipmentReturnPayload;
import tr.kontas.erp.app.shipment.graphql.ShipmentReturnGraphql;
import tr.kontas.erp.shipment.application.shipmentreturn.GetShipmentReturnsByIdsUseCase;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturn;
import tr.kontas.erp.shipment.domain.shipmentreturn.ShipmentReturnId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "shipmentReturnLoader")
@RequiredArgsConstructor
public class ShipmentReturnDataLoader implements BatchLoader<String, ShipmentReturnPayload> {

    private final GetShipmentReturnsByIdsUseCase getByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<ShipmentReturnPayload>> load(@NonNull List<String> ids) {
        List<ShipmentReturnId> idList = ids.stream().map(ShipmentReturnId::of).toList();

        Map<String, ShipmentReturn> map = getByIdsUseCase.execute(idList).stream()
                .collect(Collectors.toMap(sr -> sr.getId().asUUID().toString(), Function.identity()));

        List<ShipmentReturnPayload> result = ids.stream()
                .map(id -> {
                    ShipmentReturn sr = map.get(id);
                    return sr != null ? ShipmentReturnGraphql.toPayload(sr) : null;
                }).toList();

        return CompletableFuture.completedFuture(result);
    }
}

