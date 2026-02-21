package tr.kontas.erp.app.salesorder.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import tr.kontas.erp.app.salesorder.dtos.SalesOrderPayload;
import tr.kontas.erp.app.salesorder.graphql.SalesOrderGraphql;
import tr.kontas.erp.sales.application.salesorder.GetSalesOrdersByIdsUseCase;
import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "salesOrderLoader")
@RequiredArgsConstructor
public class SalesOrderDataLoader implements BatchLoader<String, SalesOrderPayload> {

    private final GetSalesOrdersByIdsUseCase getSalesOrdersByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<List<SalesOrderPayload>> load(@NonNull List<String> orderIds) {
        List<SalesOrderId> idList = orderIds.stream()
                .map(SalesOrderId::of)
                .toList();

        Map<String, SalesOrder> orderMap = getSalesOrdersByIdsUseCase.execute(idList)
                .stream()
                .collect(Collectors.toMap(
                        o -> o.getId().asUUID().toString(),
                        Function.identity()
                ));

        List<SalesOrderPayload> result = orderIds.stream()
                .map(id -> {
                    SalesOrder order = orderMap.get(id);
                    return order != null ? SalesOrderGraphql.toPayload(order) : null;
                })
                .toList();

        return CompletableFuture.completedFuture(result);
    }
}
