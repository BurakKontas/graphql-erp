package tr.kontas.erp.app.shipment.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.shipment.dtos.DeliveryOrderPayload;
import tr.kontas.erp.app.shipment.graphql.DeliveryOrderGraphql;
import tr.kontas.erp.shipment.application.deliveryorder.GetDeliveryOrdersByIdsUseCase;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrder;
import tr.kontas.erp.shipment.domain.deliveryorder.DeliveryOrderId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "deliveryOrderLoader")
@RequiredArgsConstructor
public class DeliveryOrderDataLoader implements BatchLoader<String, DeliveryOrderPayload> {

    private final GetDeliveryOrdersByIdsUseCase getByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<DeliveryOrderPayload>> load(@NonNull List<String> ids) {
        List<DeliveryOrderId> idList = ids.stream().map(DeliveryOrderId::of).toList();

        Map<String, DeliveryOrder> map = getByIdsUseCase.execute(idList).stream()
                .collect(Collectors.toMap(o -> o.getId().asUUID().toString(), Function.identity()));

        List<DeliveryOrderPayload> result = ids.stream()
                .map(id -> {
                    DeliveryOrder o = map.get(id);
                    return o != null ? DeliveryOrderGraphql.toPayload(o) : null;
                }).toList();

        return CompletableFuture.completedFuture(result);
    }
}

