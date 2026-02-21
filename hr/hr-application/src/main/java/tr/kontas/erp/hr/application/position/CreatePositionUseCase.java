package tr.kontas.erp.hr.application.position;

import tr.kontas.erp.hr.domain.position.PositionId;

public interface CreatePositionUseCase {
    PositionId execute(CreatePositionCommand command);
}
