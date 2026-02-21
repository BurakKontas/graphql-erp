package tr.kontas.erp.hr.platform.persistence.leavepolicy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.leavepolicy.*;

import java.util.List;

public class LeavePolicyMapper {
    private static final ObjectMapper JSON = new ObjectMapper();

    @SneakyThrows
    public static LeavePolicyJpaEntity toEntity(LeavePolicy lp) {
        LeavePolicyJpaEntity e = new LeavePolicyJpaEntity();
        e.setId(lp.getId().asUUID());
        e.setTenantId(lp.getTenantId().asUUID());
        e.setCompanyId(lp.getCompanyId().asUUID());
        e.setName(lp.getName());
        e.setCountryCode(lp.getCountryCode());
        e.setLeaveTypesJson(JSON.writeValueAsString(lp.getLeaveTypes()));
        e.setActive(lp.isActive());
        return e;
    }

    @SneakyThrows
    public static LeavePolicy toDomain(LeavePolicyJpaEntity e) {
        List<LeaveTypeDef> types = e.getLeaveTypesJson() != null
                ? JSON.readValue(e.getLeaveTypesJson(), new TypeReference<>() {})
                : List.of();
        return new LeavePolicy(
                LeavePolicyId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getName(), e.getCountryCode(), types, e.isActive());
    }
}
