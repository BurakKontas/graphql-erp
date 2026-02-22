package tr.kontas.erp.hr.platform.persistence.payrollconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;
import tr.kontas.erp.hr.domain.payrollconfig.*;

import java.util.List;

public class PayrollConfigMapper {

    public static PayrollConfigJpaEntity toEntity(PayrollConfig pc) {
        PayrollConfigJpaEntity e = new PayrollConfigJpaEntity();
        e.setId(pc.getId().asUUID());
        e.setTenantId(pc.getTenantId().asUUID());
        e.setCompanyId(pc.getCompanyId().asUUID());
        e.setCountryCode(pc.getCountryCode());
        e.setName(pc.getName());
        e.setValidYear(pc.getValidYear());
        e.setMinimumWage(pc.getMinimumWage());
        e.setCurrencyCode(pc.getCurrencyCode());
        e.setTaxBracketsJson(JacksonProvider.serialize(pc.getTaxBrackets()));
        e.setDeductionsJson(JacksonProvider.serialize(pc.getDeductions()));
        e.setActive(pc.isActive());
        return e;
    }

    public static PayrollConfig toDomain(PayrollConfigJpaEntity e) {
        List<TaxBracket> brackets = e.getTaxBracketsJson() != null
                ? JacksonProvider.deserialize(e.getTaxBracketsJson(), new TypeReference<>() {}) : List.of();
        List<DeductionRule> deductions = e.getDeductionsJson() != null
                ? JacksonProvider.deserialize(e.getDeductionsJson(), new TypeReference<>() {}) : List.of();
        return new PayrollConfig(
                PayrollConfigId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getCountryCode(), e.getName(), e.getValidYear(), e.getMinimumWage(), e.getCurrencyCode(),
                brackets, deductions, e.isActive());
    }
}
