package tr.kontas.erp.hr.domain.contract;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Contract extends AggregateRoot<ContractId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private final ContractNumber contractNumber;
    private final String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractType contractType;
    private BigDecimal grossSalary;
    private String currencyCode;
    private String payrollConfigId;
    private ContractStatus status;
    private final List<SalaryComponent> components;

    public Contract(ContractId id, TenantId tenantId, CompanyId companyId,
                    ContractNumber contractNumber, String employeeId,
                    LocalDate startDate, LocalDate endDate, ContractType contractType,
                    BigDecimal grossSalary, String currencyCode, String payrollConfigId,
                    ContractStatus status, List<SalaryComponent> components) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.contractNumber = contractNumber;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractType = contractType;
        this.grossSalary = grossSalary;
        this.currencyCode = currencyCode;
        this.payrollConfigId = payrollConfigId;
        this.status = status;
        this.components = new ArrayList<>(components != null ? components : List.of());
    }


    public List<SalaryComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }


    public void activate() {
        if (status != ContractStatus.DRAFT) {
            throw new IllegalStateException("Can only activate DRAFT contracts");
        }
        this.status = ContractStatus.ACTIVE;
    }


    public void terminate(LocalDate date, String reason) {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException("Can only terminate ACTIVE contracts");
        }
        this.endDate = date;
        this.status = ContractStatus.TERMINATED;
    }


    public void updateSalary(BigDecimal newGross) {
        if (status != ContractStatus.ACTIVE) {
            throw new IllegalStateException("Can only update salary on ACTIVE contracts");
        }
        this.grossSalary = newGross;
    }


    public void addComponent(SalaryComponent component) {
        this.components.add(component);
    }


    public void removeComponent(ComponentType type) {
        this.components.removeIf(c -> c.getType() == type);
    }
}

