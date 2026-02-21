package tr.kontas.erp.hr.platform.persistence.leavebalance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "leave_balances")
@Getter
@Setter
@NoArgsConstructor
public class LeaveBalanceJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "entitlement_days", nullable = false)
    private int entitlementDays;

    @Column(name = "used_days", nullable = false)
    private int usedDays;

    @Column(name = "carryover_days", nullable = false)
    private int carryoverDays;

    @Column(name = "pending_days", nullable = false)
    private int pendingDays;
}
