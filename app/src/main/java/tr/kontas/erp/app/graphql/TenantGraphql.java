package tr.kontas.erp.app.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.dtos.CreateTenantInput;
import tr.kontas.erp.app.dtos.TenantPayload;
import tr.kontas.erp.core.application.tenant.CreateTenantCommand;
import tr.kontas.erp.core.application.tenant.CreateTenantUseCase;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

@DgsComponent
@RequiredArgsConstructor
public class TenantGraphql {

    private final CreateTenantUseCase createTenantUseCase;

    @DgsMutation
    public TenantPayload createTenant(@InputArgument("input") CreateTenantInput input) {

        CreateTenantCommand command = new CreateTenantCommand(input.getName(), input.getCode());

        TenantId tenantId = createTenantUseCase.execute(command);

        return new TenantPayload(
                tenantId.asUUID().toString(),
                input.getName(),
                input.getCode()
        );
    }
}
