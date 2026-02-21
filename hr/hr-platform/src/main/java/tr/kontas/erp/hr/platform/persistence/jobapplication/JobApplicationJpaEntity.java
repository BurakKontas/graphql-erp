package tr.kontas.erp.hr.platform.persistence.jobapplication;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
public class JobApplicationJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "job_posting_id", nullable = false)
    private String jobPostingId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cv_ref")
    private String cvRef;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "current_stage_note")
    private String currentStageNote;

    @Column(name = "interviews_json", columnDefinition = "TEXT")
    private String interviewsJson;

    @Column(name = "applied_at", nullable = false)
    private LocalDate appliedAt;
}
