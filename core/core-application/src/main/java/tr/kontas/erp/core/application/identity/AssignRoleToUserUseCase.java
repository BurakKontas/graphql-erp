package tr.kontas.erp.core.application.identity;

import tr.kontas.erp.core.domain.identity.valueobjects.UserId;

public interface AssignRoleToUserUseCase {
    void execute(AssignRoleToUserCommand command);
}
