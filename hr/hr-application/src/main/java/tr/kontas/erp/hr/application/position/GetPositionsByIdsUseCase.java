package tr.kontas.erp.hr.application.position;

import tr.kontas.erp.hr.domain.position.Position;
import tr.kontas.erp.hr.domain.position.PositionId;

import java.util.List;

public interface GetPositionsByIdsUseCase {
    List<Position> execute(List<PositionId> ids);
}
