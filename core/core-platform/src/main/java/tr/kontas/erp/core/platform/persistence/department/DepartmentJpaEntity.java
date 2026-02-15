package tr.kontas.erp.core.platform.persistence.department;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id")
    private UUID parentId;

    @ElementCollection
    @CollectionTable(
            name = "department_jpa_entity_sub_departments",
            joinColumns = @JoinColumn(name = "department_jpa_entity_id")
    )
    @Column(name = "sub_departments")
    private Set<UUID> subDepartments = new HashSet<>();

    @Column(name = "active", nullable = false)
    private boolean active;
}
