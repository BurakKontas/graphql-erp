package tr.kontas.erp.hr.platform.persistence.payrollconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.payrollconfig.*;

import java.util.List;

public class PayrollConfigMapper {
    private static final ObjectMapper JSON = new ObjectMapper();

    @SneakyThrows
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
        e.setTaxBracketsJson(JSON.writeValueAsString(pc.getTaxBrackets()));
        e.setDeductionsJson(JSON.writeValueAsString(pc.getDeductions()));
        e.setActive(pc.isActive());
        return e;
    }

    @SneakyThrows
    public static PayrollConfig toDomain(PayrollConfigJpaEntity e) {
        List<TaxBracket> brackets = e.getTaxBracketsJson() != null
                ? JSON.readValue(e.getTaxBracketsJson(), new TypeReference<>() {}) : List.of();
        List<DeductionRule> deductions = e.getDeductionsJson() != null
                ? JSON.readValue(e.getDeductionsJson(), new TypeReference<>() {}) : List.of();
        return new PayrollConfig(
                PayrollConfigId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getCountryCode(), e.getName(), e.getValidYear(), e.getMinimumWage(), e.getCurrencyCode(),
                brackets, deductions, e.isActive());
    }
}
