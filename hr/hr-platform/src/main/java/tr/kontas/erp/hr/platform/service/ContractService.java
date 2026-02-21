package tr.kontas.erp.hr.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.event.DomainEventPublisher;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.application.contract.*;
import tr.kontas.erp.hr.application.port.ContractNumberGeneratorPort;
import tr.kontas.erp.hr.domain.contract.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractService implements CreateContractUseCase, GetContractByIdUseCase,
        GetContractsByCompanyUseCase, GetContractsByIdsUseCase {

    private final ContractRepository contractRepository;
    private final ContractNumberGeneratorPort numberGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    public ContractId execute(CreateContractCommand cmd) {
        TenantId tenantId = TenantContext.get();
        ContractId id = ContractId.newId();
        ContractNumber number = numberGenerator.generate(tenantId, cmd.companyId(), LocalDate.now().getYear());
        Contract contract = new Contract(id, tenantId, cmd.companyId(), number, cmd.employeeId(),
                cmd.startDate(), cmd.endDate(), ContractType.valueOf(cmd.contractType()),
                cmd.grossSalary(), cmd.currencyCode(), cmd.payrollConfigId(),
                ContractStatus.DRAFT, List.of());
        contractRepository.save(contract);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Contract execute(ContractId id) {
        return contractRepository.findById(id, TenantContext.get())
                .orElseThrow(() -> new IllegalArgumentException("Contract not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contract> execute(CompanyId companyId) {
        return contractRepository.findByCompanyId(TenantContext.get(), companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contract> execute(List<ContractId> ids) {
        return contractRepository.findByIds(ids);
    }
}
