package tr.kontas.erp.hr.platform.persistence.leavepolicy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "leave_policies")
@Getter
@Setter
@NoArgsConstructor
public class LeavePolicyJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "leave_types_json", columnDefinition = "TEXT")
    private String leaveTypesJson;

    @Column(name = "active", nullable = false)
    private boolean active;
}
