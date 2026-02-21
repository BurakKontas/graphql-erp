package tr.kontas.erp.hr.domain.payrollconfig;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PayrollConfig extends AggregateRoot<PayrollConfigId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private String countryCode;
    private String name;
    private int validYear;
    private BigDecimal minimumWage;
    private String currencyCode;
    private final List<TaxBracket> taxBrackets;
    private final List<DeductionRule> deductions;
    private boolean active;

    public PayrollConfig(PayrollConfigId id, TenantId tenantId, CompanyId companyId,
                         String countryCode, String name, int validYear,
                         BigDecimal minimumWage, String currencyCode,
                         List<TaxBracket> taxBrackets, List<DeductionRule> deductions,
                         boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.countryCode = countryCode;
        this.name = name;
        this.validYear = validYear;
        this.minimumWage = minimumWage;
        this.currencyCode = currencyCode;
        this.taxBrackets = new ArrayList<>(taxBrackets != null ? taxBrackets : List.of());
        this.deductions = new ArrayList<>(deductions != null ? deductions : List.of());
        this.active = active;
    }


    public List<TaxBracket> getTaxBrackets() {
        return Collections.unmodifiableList(taxBrackets);
    }


    public List<DeductionRule> getDeductions() {
        return Collections.unmodifiableList(deductions);
    }


    public void addTaxBracket(TaxBracket bracket) {
        this.taxBrackets.add(bracket);
    }


    public void addDeductionRule(DeductionRule rule) {
        this.deductions.add(rule);
    }


    public void deactivate() {
        this.active = false;
    }
}

