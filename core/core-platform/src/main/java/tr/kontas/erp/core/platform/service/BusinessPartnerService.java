package tr.kontas.erp.core.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.kontas.erp.core.application.businesspartner.*;
import tr.kontas.erp.core.domain.businesspartner.*;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessPartnerService implements
        CreateBusinessPartnerUseCase,
        GetBusinessPartnerByIdUseCase,
        GetBusinessPartnersUseCase,
        GetBusinessPartnersByCompanyIdsUseCase,
        GetBusinessPartnersByIdsUseCase {

    private final BusinessPartnerRepository businessPartnerRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public BusinessPartnerId execute(CreateBusinessPartnerCommand command) {
        TenantId tenantId = TenantContext.get();

        BusinessPartnerCode code = new BusinessPartnerCode(command.code());
        BusinessPartnerName name = new BusinessPartnerName(command.name());
        BusinessPartnerTaxNumber taxNumber = command.taxNumber() != null
                ? new BusinessPartnerTaxNumber(command.taxNumber())
                : null;

        if (businessPartnerRepository.existsByCode(tenantId, command.companyId(), code)) {
            throw new IllegalArgumentException("BusinessPartner with code " + command.code() + " already exists");
        }

        BusinessPartnerId id = BusinessPartnerId.newId();
        BusinessPartner businessPartner = new BusinessPartner(
                id,
                tenantId,
                command.companyId(),
                code,
                name,
                command.roles(),
                taxNumber
        );

        businessPartnerRepository.save(businessPartner);
        eventPublisher.publishAll(businessPartner.getDomainEvents());
        businessPartner.clearDomainEvents();

        return id;
    }

    @Override
    public BusinessPartner execute(BusinessPartnerId id) {
        return businessPartnerRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<BusinessPartner> execute(CompanyId companyId) {
        TenantId tenantId = TenantContext.get();
        return businessPartnerRepository.findByCompany(tenantId, companyId);
    }

    @Override
    @Transactional
    public Map<CompanyId, List<BusinessPartner>> executeByCompanyIds(List<CompanyId> ids) {
        TenantId tenantId = TenantContext.get();
        Map<CompanyId, List<BusinessPartner>> resultMap = new HashMap<>();

        List<BusinessPartner> businessPartners = businessPartnerRepository.findByCompanyIds(tenantId, ids);

        businessPartners.forEach(bp -> {
            resultMap.computeIfAbsent(bp.getCompanyId(), _ -> new ArrayList<>())
                    .add(bp);
        });

        return resultMap;
    }

    @Override
    public List<BusinessPartner> execute(List<BusinessPartnerId> ids) {
        return businessPartnerRepository.findByIds(ids);
    }
}
