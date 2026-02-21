package tr.kontas.erp.app.hr.loaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import tr.kontas.erp.app.hr.dtos.PositionPayload;
import tr.kontas.erp.hr.application.position.GetPositionsByIdsUseCase;
import tr.kontas.erp.hr.domain.position.Position;
import tr.kontas.erp.hr.domain.position.PositionId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "positionLoader")
@RequiredArgsConstructor
public class PositionDataLoader implements MappedBatchLoader<String, PositionPayload> {

    private final GetPositionsByIdsUseCase getPositionsByIdsUseCase;

    @NonNull
    @Override
    public CompletionStage<Map<String, PositionPayload>> load(@NonNull Set<String> ids) {
        List<PositionId> positionIds = ids.stream().map(PositionId::of).toList();
        List<Position> positions = getPositionsByIdsUseCase.execute(positionIds);
        Map<String, PositionPayload> result = new HashMap<>();
        for (Position p : positions) {
            result.put(p.getId().asUUID().toString(), new PositionPayload(
                    p.getId().asUUID().toString(), p.getCompanyId().asUUID().toString(),
                    p.getCode().getValue(), p.getTitle().getValue(), p.getDepartmentId(),
                    p.getLevel() != null ? p.getLevel().name() : null,
                    p.getSalaryGrade() != null ? p.getSalaryGrade().getValue() : null,
                    p.getHeadcount(), p.getFilledCount(), p.getStatus().name()));
        }
        return CompletableFuture.completedFuture(result);
    }
}

