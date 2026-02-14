package tr.kontas.erp.app.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.context.DgsContext;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.dtos.CompanyPayload;
import tr.kontas.erp.app.dtos.CreateCompanyInput;
import tr.kontas.erp.core.application.company.CreateCompanyCommand;
import tr.kontas.erp.core.application.company.CreateCompanyUseCase;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.context.Context;

@DgsComponent
@RequiredArgsConstructor
public class CompanyMutation {

    private final CreateCompanyUseCase createCompanyUseCase;

    @DgsMutation
    public CompanyPayload createCompany(@InputArgument("input") CreateCompanyInput input, DgsDataFetchingEnvironment dfe) {
        Context context = DgsContext.getCustomContext(dfe);

        TenantId tenantId = TenantId.of(context.getTenantId());

        CreateCompanyCommand command = new CreateCompanyCommand(tenantId, input.getName());

        CompanyId companyId = createCompanyUseCase.execute(command);

        return new CompanyPayload(
                tenantId.asUUID().toString(),
                companyId.asUUID().toString(),
                input.getName()
        );
    }
}
