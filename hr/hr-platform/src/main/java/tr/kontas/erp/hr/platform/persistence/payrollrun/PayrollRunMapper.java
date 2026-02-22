package tr.kontas.erp.hr.platform.persistence.payrollrun;

import com.fasterxml.jackson.core.type.TypeReference;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;
import tr.kontas.erp.hr.domain.payrollrun.*;

import java.util.List;

public class PayrollRunMapper {

    public static PayrollRunJpaEntity toEntity(PayrollRun pr) {
        PayrollRunJpaEntity e = new PayrollRunJpaEntity();
        e.setId(pr.getId().asUUID());
        e.setTenantId(pr.getTenantId().asUUID());
        e.setCompanyId(pr.getCompanyId().asUUID());
        e.setRunNumber(pr.getRunNumber().getValue());
        e.setYear(pr.getYear());
        e.setMonth(pr.getMonth());
        e.setStatus(pr.getStatus().name());
        e.setPaymentDate(pr.getPaymentDate());
        e.setPayrollConfigId(pr.getPayrollConfigId());
        e.setEntriesJson(JacksonProvider.serialize(pr.getEntries()));
        return e;
    }

    public static PayrollRun toDomain(PayrollRunJpaEntity e) {
        List<PayrollEntry> entries = e.getEntriesJson() != null
                ? JacksonProvider.deserialize(e.getEntriesJson(), new TypeReference<>() {}) : List.of();
        return new PayrollRun(
                PayrollRunId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                new PayrollRunNumber(e.getRunNumber()), e.getYear(), e.getMonth(),
                PayrollRunStatus.valueOf(e.getStatus()), e.getPaymentDate(), e.getPayrollConfigId(), entries);
    }
}
