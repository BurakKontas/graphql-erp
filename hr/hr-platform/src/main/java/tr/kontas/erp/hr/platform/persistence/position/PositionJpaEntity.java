package tr.kontas.erp.hr.platform.persistence.position;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "positions")
@Getter
@Setter

@NoArgsConstructor
public class PositionJpaEntity {
    @Id

    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "level")
    private String level;

    @Column(name = "salary_grade")
    private String salaryGrade;

    @Column(name = "headcount", nullable = false)
    private int headcount;

    @Column(name = "filled_count", nullable = false)
    private int filledCount;
    @Column(name = "status", nullable = false)
    private String status;
}
