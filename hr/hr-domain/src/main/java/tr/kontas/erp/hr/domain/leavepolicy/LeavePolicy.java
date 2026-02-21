package tr.kontas.erp.hr.domain.leavepolicy;

import lombok.Getter;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.domain.model.AggregateRoot;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class LeavePolicy extends AggregateRoot<LeavePolicyId> {

    private final TenantId tenantId;
    private final CompanyId companyId;
    private String name;
    private String countryCode;
    private final List<LeaveTypeDef> leaveTypes;
    private boolean active;

    public LeavePolicy(LeavePolicyId id, TenantId tenantId, CompanyId companyId,
                       String name, String countryCode, List<LeaveTypeDef> leaveTypes,
                       boolean active) {
        super(id);
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.name = name;
        this.countryCode = countryCode;
        this.leaveTypes = new ArrayList<>(leaveTypes != null ? leaveTypes : List.of());
        this.active = active;
    }


    public List<LeaveTypeDef> getLeaveTypes() {
        return Collections.unmodifiableList(leaveTypes);
    }


    public void updateLeaveType(LeaveType type, LeaveTypeDef def) {
        leaveTypes.removeIf(lt -> lt.getLeaveType() == type);
        leaveTypes.add(def);
    }


    public void deactivate() {
        this.active = false;
    }


    public void activate() {
        this.active = true;
    }
}

