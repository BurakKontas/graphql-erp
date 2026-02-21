package tr.kontas.erp.hr.platform.persistence.jobapplication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import tr.kontas.erp.core.domain.company.CompanyId;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.hr.domain.jobapplication.*;

import java.util.List;

public class JobApplicationMapper {
    private static final ObjectMapper JSON = new ObjectMapper();

    @SneakyThrows
    public static JobApplicationJpaEntity toEntity(JobApplication ja) {
        JobApplicationJpaEntity e = new JobApplicationJpaEntity();
        e.setId(ja.getId().asUUID());
        e.setTenantId(ja.getTenantId().asUUID());
        e.setCompanyId(ja.getCompanyId().asUUID());
        e.setJobPostingId(ja.getJobPostingId());
        if (ja.getApplicantInfo() != null) {
            e.setFirstName(ja.getApplicantInfo().getFirstName());
            e.setLastName(ja.getApplicantInfo().getLastName());
            e.setEmail(ja.getApplicantInfo().getEmail());
            e.setPhone(ja.getApplicantInfo().getPhone());
            e.setCvRef(ja.getApplicantInfo().getCvRef());
        }
        e.setStatus(ja.getStatus().name());
        e.setCurrentStageNote(ja.getCurrentStageNote());
        e.setInterviewsJson(JSON.writeValueAsString(ja.getInterviews()));
        e.setAppliedAt(ja.getAppliedAt());
        return e;
    }

    @SneakyThrows
    public static JobApplication toDomain(JobApplicationJpaEntity e) {
        ApplicantInfo ai = new ApplicantInfo(
                e.getFirstName(), e.getLastName(), e.getEmail(), e.getPhone(), e.getCvRef());
        List<InterviewRecord> interviews = e.getInterviewsJson() != null
                ? JSON.readValue(e.getInterviewsJson(), new TypeReference<>() {}) : List.of();
        return new JobApplication(
                JobApplicationId.of(e.getId()), TenantId.of(e.getTenantId()), CompanyId.of(e.getCompanyId()),
                e.getJobPostingId(), ai, ApplicationStatus.valueOf(e.getStatus()),
                e.getCurrentStageNote(), interviews, e.getAppliedAt());
    }
}
