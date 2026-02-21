package tr.kontas.erp.hr.platform.persistence.jobposting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "job_postings")
@Getter
@Setter
@NoArgsConstructor
public class JobPostingJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "posting_number", nullable = false)
    private String postingNumber;

    @Column(name = "position_id", nullable = false)
    private String positionId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirements_json", columnDefinition = "TEXT")
    private String requirementsJson;

    @Column(name = "employment_type", nullable = false)
    private String employmentType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @Column(name = "closing_date")
    private LocalDate closingDate;
}
