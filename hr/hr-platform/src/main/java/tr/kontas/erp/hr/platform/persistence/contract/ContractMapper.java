package tr.kontas.erp.hr.platform.persistence.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.contract.*;

import java.util.List;

public class ContractMapper {
    private static final ObjectMapper JSON = new ObjectMapper();

    @SneakyThrows
    public static ContractJpaEntity toEntity(Contract c) {
        ContractJpaEntity e = new ContractJpaEntity();
        e.setId(c.getId().asUUID());
        e.setTenantId(c.getTenantId().asUUID());
        e.setCompanyId(c.getCompanyId().asUUID());
        e.setContractNumber(c.getContractNumber().getValue());
        e.setEmployeeId(c.getEmployeeId());
        e.setStartDate(c.getStartDate());
        e.setEndDate(c.getEndDate());
        e.setContractType(c.getContractType().name());
        e.setGrossSalary(c.getGrossSalary());
        e.setCurrencyCode(c.getCurrencyCode());
        e.setPayrollConfigId(c.getPayrollConfigId());
        e.setStatus(c.getStatus().name());
        e.setComponentsJson(JSON.writeValueAsString(c.getComponents()));
        return e;
    }

    @SneakyThrows
    public static Contract toDomain(ContractJpaEntity e) {
        List<SalaryComponent> components = e.getComponentsJson() != null
                ? JSON.readValue(e.getComponentsJson(), new TypeReference<>() {})
                : List.of();
        return new Contract(
                ContractId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                new ContractNumber(e.getContractNumber()), e.getEmployeeId(),
                e.getStartDate(), e.getEndDate(), ContractType.valueOf(e.getContractType()),
                e.getGrossSalary(), e.getCurrencyCode(), e.getPayrollConfigId(),
                ContractStatus.valueOf(e.getStatus()), components);
    }
}
