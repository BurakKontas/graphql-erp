package tr.kontas.erp.app.businesspartner.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import tr.kontas.erp.app.businesspartner.dtos.BusinessPartnerPayload;
import tr.kontas.erp.app.businesspartner.dtos.CreateBusinessPartnerInput;
import tr.kontas.erp.app.company.dtos.CompanyPayload;
import tr.kontas.erp.core.application.businesspartner.CreateBusinessPartnerCommand;
import tr.kontas.erp.core.application.businesspartner.CreateBusinessPartnerUseCase;
import tr.kontas.erp.core.application.businesspartner.GetBusinessPartnerByIdUseCase;
import tr.kontas.erp.core.application.businesspartner.GetBusinessPartnersByCompanyIdsUseCase;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartner;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerId;
import tr.kontas.erp.core.domain.businesspartner.BusinessPartnerRole;
import tr.kontas.erp.core.domain.company.CompanyId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class BusinessPartnerGraphql {

    private final CreateBusinessPartnerUseCase createBusinessPartnerUseCase;
    private final GetBusinessPartnerByIdUseCase getBusinessPartnerByIdUseCase;
    private final GetBusinessPartnersByCompanyIdsUseCase getBusinessPartnersByCompanyIdsUseCase;

    public static BusinessPartnerPayload toPayload(BusinessPartner bp) {
        Set<String> roles = bp.getRoles().stream()
                .map(BusinessPartnerRole::name)
                .collect(Collectors.toSet());

        return new BusinessPartnerPayload(
                bp.getId().asUUID().toString(),
                bp.getCode().getValue(),
                bp.getName().getValue(),
                roles,
                bp.getTaxNumber() != null ? bp.getTaxNumber().getValue() : null,
                bp.isActive(),
                bp.getCompanyId().asUUID().toString()
        );
    }

    @DgsMutation
    public BusinessPartnerPayload createBusinessPartner(@InputArgument("input") CreateBusinessPartnerInput input) {
        CompanyId companyId = CompanyId.of(input.getCompanyId());

        Set<BusinessPartnerRole> roles = input.getRoles().stream()
                .map(BusinessPartnerRole::valueOf)
                .collect(Collectors.toSet());

        CreateBusinessPartnerCommand command = new CreateBusinessPartnerCommand(
                companyId,
                input.getCode(),
                input.getName(),
                roles,
                input.getTaxNumber()
        );

        BusinessPartnerId id = createBusinessPartnerUseCase.execute(command);

        return new BusinessPartnerPayload(
                id.asUUID().toString(),
                input.getCode(),
                input.getName(),
                input.getRoles(),
                input.getTaxNumber(),
                true,
                input.getCompanyId()
        );
    }

    @DgsQuery
    public List<BusinessPartnerPayload> businessPartners(@InputArgument("companyId") String id) {
        CompanyId companyId = CompanyId.of(id);

        var businessPartners = getBusinessPartnersByCompanyIdsUseCase.executeByCompanyIds(List.of(companyId))
                .get(companyId);

        if (businessPartners == null) return List.of();

        return businessPartners
                .stream()
                .map(BusinessPartnerGraphql::toPayload)
                .toList();
    }

    @DgsQuery
    public BusinessPartnerPayload businessPartner(@InputArgument("id") String id) {
        BusinessPartnerId businessPartnerId = BusinessPartnerId.of(id);
        return toPayload(getBusinessPartnerByIdUseCase.execute(businessPartnerId));
    }

    @DgsEntityFetcher(name = "BusinessPartnerPayload")
    public BusinessPartnerPayload businessPartnerEntity(Map<String, Object> values) {
        String id = values.get("id").toString();
        return businessPartner(id);
    }

    @DgsData(parentType = "BusinessPartnerPayload")
    public CompletableFuture<CompanyPayload> company(DgsDataFetchingEnvironment dfe) {
        BusinessPartnerPayload businessPartner = dfe.getSource();

        DataLoader<String, CompanyPayload> dataLoader = dfe.getDataLoader("companyLoader");

        if (dataLoader == null || businessPartner == null) {
            return CompletableFuture.completedFuture(null);
        }

        return dataLoader.load(businessPartner.getCompanyId());
    }
}
