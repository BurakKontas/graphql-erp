package tr.kontas.erp.hr.application.position;

import tr.kontas.erp.hr.domain.position.Position;
import tr.kontas.erp.hr.domain.position.PositionId;

public interface GetPositionByIdUseCase {
    Position execute(PositionId id);
}
